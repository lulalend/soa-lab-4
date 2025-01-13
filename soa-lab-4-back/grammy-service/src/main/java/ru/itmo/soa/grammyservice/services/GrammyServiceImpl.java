package ru.itmo.soa.grammyservice.services;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.itmo.soa.grammyservice.model.Band;
import ru.itmo.soa.grammyservice.model.Person;
import ru.itmo.soa.grammyservice.model.Single;

@WebService(serviceName = "GrammyService", targetNamespace = "http://example.com/grammy")
public class GrammyServiceImpl {
    private final RestTemplate restTemplate;

    @Autowired
    public GrammyServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @WebMethod(operationName = "AddSingle")
    public Band addSingle(@WebParam(name = "bandId") Long bandId, @WebParam(name = "single") Single single) {
        String url = String.format("https://localhost:1111/api/v1/bands/%d/singles", bandId);

        try {
            ResponseEntity<Band> response = restTemplate.postForEntity(url, single, Band.class);
            return response.getBody();
        } catch (Exception e) {
            throw new RuntimeException("Failed to add single: " + e.getMessage(), e);
        }
    }

    @WebMethod(operationName = "ChangeSingle")
    public Single changeSingle(
            @WebParam(name = "bandId") Long bandId,
            @WebParam(name = "singleId") Long singleId,
            @WebParam(name = "single") Single single) {
        String url = String.format("https://localhost:1111/api/v1/bands/%d/singles/%d", bandId, singleId);

        try {
            ResponseEntity<Single> response = restTemplate.exchange(
                    url, HttpMethod.PUT, new HttpEntity<>(single), Single.class);
            return response.getBody();
        } catch (Exception e) {
            throw new RuntimeException("Failed to change single: " + e.getMessage(), e);
        }
    }

    @WebMethod(operationName = "AddParticipant")
    public Person addParticipant(@WebParam(name = "bandId") Long bandId, @WebParam(name = "participant") Person participant) {
        String url = String.format("https://localhost:1111/api/v1/bands/%d/participants", bandId);

        try {
            ResponseEntity<Person> response = restTemplate.postForEntity(url, participant, Person.class);
            return response.getBody();
        } catch (Exception e) {
            throw new RuntimeException("Failed to add participant: " + e.getMessage(), e);
        }
    }
}
