package ru.itmo.soa.grammyservice.config;

import jakarta.xml.ws.Endpoint;
import org.apache.cxf.Bus;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import ru.itmo.soa.grammyservice.exceptions.ExceptionInterceptor;
import ru.itmo.soa.grammyservice.services.GrammyService;


@Configuration
public class EndpointConfig extends WsConfigurerAdapter {
    private final Bus bus;

    @Autowired
    public EndpointConfig(Bus bus) {
        this.bus = bus;
    }

    @Bean
    public Endpoint endpoint(GrammyService eventEndpoint) {
        EndpointImpl endpoint = new EndpointImpl(bus, eventEndpoint);
        endpoint.publish("/grammy");
        endpoint.getServer().getEndpoint().getInInterceptors().add(new LoggingInInterceptor());
        endpoint.getServer().getEndpoint().getOutInterceptors().add(new LoggingOutInterceptor());
        endpoint.getServer().getEndpoint().getOutFaultInterceptors().add(new ExceptionInterceptor());
        return endpoint;
    }
}


