package ru.itmo.soa.grammyservice.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;
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
            @WebParam(name = "bandId") Long bandId,
            @WebParam(name = "single") Single single
    ) {
        String url = String.format("https://localhost:1111/api/v1/bands/%d/singles", bandId);

        ResponseEntity<Band> response = restTemplate.postForEntity(url, single, Band.class);
        return response.getBody();
    }

    public Single changeSingle(
            @WebParam(name = "bandId") Long bandId,
            @WebParam(name = "singleId") Long singleId,
            @WebParam(name = "single") Single single
    ) {
        String url = String.format("https://localhost:1111/api/v1/bands/%d/singles/%d", bandId, singleId);

        ResponseEntity<Single> response = restTemplate.exchange(
                url, HttpMethod.PUT, new HttpEntity<>(single), Single.class);
        return response.getBody();
    }

    public Person addParticipant(
            @WebParam(name = "bandId") Long bandId,
            @WebParam(name = "participant") Person participant
    ) {
        String url = String.format("https://localhost:1111/api/v1/bands/%d/participants", bandId);

        ResponseEntity<Person> response = restTemplate.postForEntity(url, participant, Person.class);
        return response.getBody();
    }
}
