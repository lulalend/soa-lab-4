package ru.itmo.soa.grammyservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jws.WebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import ru.itmo.soa.grammyservice.exceptions.ErrorWithCode;
import ru.itmo.soa.grammyservice.model.Band;
import ru.itmo.soa.grammyservice.model.Person;
import ru.itmo.soa.grammyservice.model.Single;

@WebService(serviceName = "GrammyService")
@Controller
public class GrammyService {
    private final RestTemplate restTemplate;
    ObjectMapper om ;

    @Autowired
    public GrammyService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.om = new ObjectMapper();
    }

    public Band addSingle(
            Long bandId,
            Single single
    ) {
        String url = String.format("https://localhost:1111/api/v1/bands/%d/singles", bandId);

        ResponseEntity<Band> response = restTemplate.postForEntity(url, single, Band.class);
        return response.getBody();
    }

    public Single changeSingle(
            Long bandId,
            Long singleId,
            Single single
    ) {
        String url = String.format("https://localhost:1111/api/v1/bands/%d/singles/%d", bandId, singleId);

        try {
            ResponseEntity<Single> response = restTemplate.exchange(
                    url, HttpMethod.PUT, new HttpEntity<>(single), Single.class);
            return response.getBody();
        } catch (RestClientResponseException e) {
            throw new RuntimeException("Failed to change single: " + e.getMessage(), e);
        }
    }

    public Person addParticipant(
            Long bandId,
            Person participant
    ) {
        String url = String.format("https://localhost:1111/api/v1/bands/%d/participants", bandId);

        try {
            ResponseEntity<Person> response = restTemplate.postForEntity(url, participant, Person.class);
            return response.getBody();
        } catch (RestClientResponseException e) {
            throw new RuntimeException("Failed to add participant: " + e.getMessage(), e);
        }
    }
}
