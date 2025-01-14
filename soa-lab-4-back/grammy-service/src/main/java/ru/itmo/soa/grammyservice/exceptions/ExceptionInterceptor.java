package ru.itmo.soa.grammyservice.exceptions;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.interceptor.FaultOutInterceptor;
import org.apache.cxf.logging.FaultListener;
import org.apache.cxf.message.Message;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import com.fasterxml.jackson.databind.ObjectMapper;


public class ExceptionInterceptor extends FaultOutInterceptor implements FaultListener {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean faultOccurred(Exception exception, String s, Message message) {
        var error = mapException(exception.getCause());
        if (error == null) {
            return true;
        }
        return error.getCode() == 500;
    }

    @Override
    public void handleMessage(Message message) throws Fault {
        if (message == null) {
            return;
        }

        var exception = (Fault) message.getContent(Exception.class);
        if (exception == null) {
            return;
        }

        var error = mapException(exception.getCause());
        if (error == null) {
            return;
        }

        error.setMessage(error.getMessage().replaceAll(":", " –") + ":" + error.getCode());
        Fault f = new Fault(error);
        message.getExchange().getOutFaultMessage().setContent(Exception.class, f);
    }

    public ErrorWithCode mapException(Throwable e) {
        if (e == null) {
            return null;
        }
        if (e instanceof HttpRequestMethodNotSupportedException) {
            return new ErrorWithCode(e.getMessage(), 405);
        }
        if (e instanceof ResourceAccessException) {
            return new ErrorWithCode("Service unavailable. Try later", 503);
        }
        if (e instanceof RestClientResponseException) {
            String responseBody = ((RestClientResponseException) e).getResponseBodyAsString();

            try {
                ErrorDetails errorDetails = objectMapper.readValue(responseBody, ErrorDetails.class);
                return new ErrorWithCode(errorDetails.getMessage(), errorDetails.getCode());
            } catch (Exception ex) {
                return new ErrorWithCode("Error parsing faultstring", 500);
            }
        }

        return new ErrorWithCode(e.getMessage(), 500);
    }
}

