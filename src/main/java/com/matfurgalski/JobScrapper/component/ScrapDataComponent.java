package com.matfurgalski.JobScrapper.component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.matfurgalski.JobScrapper.model.JoinItOffer;
import com.matfurgalski.JobScrapper.model.PracujOffer;
import com.matfurgalski.JobScrapper.service.OfferService;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ScrapDataComponent {
    boolean pracujIsBuild = false;
    boolean joinItIsBuild = false;
    private final OfferService offerService;
    private final ObjectMapper mapper;

    public ScrapDataComponent(OfferService offerService, ObjectMapper mapper) {
        this.offerService = offerService;
        this.mapper = mapper;
    }

    public void ScrapPracuj(){

        int page = 1;
        String cookieUri = "https://massachusetts.pracuj.pl/cookiePolicy";
        String xsrfToken = "";
        int totalPages;

        List<PracujOffer> allOffers = new ArrayList<>();
        ResponseEntity<String> responseCookie = getDataFromPracuj(cookieUri,xsrfToken);
        xsrfToken =  responseCookie.getHeaders().getValuesAsList("Set-Cookie").get(1).split(";")[0].split("=")[1];

        do {
           // String uri = "https://massachusetts.pracuj.pl/jobOffers/listing/grouped?et=1&et=3&et=17&pn="+page+"&iwhpl=false&groupBy=ws&groupBy=et&groupBy=tc&groupBy=p&groupBy=ao&groupBy=wm&groupBy=ua&groupBy=wpl&groupBy=its&groupBy=itth&groupBy=ap&groupBy=jobicon&groupBy=hs&subservice=1";
            String uri = "https://massachusetts.pracuj.pl/jobOffers/listing/grouped?et=1&et=3&et=17&pn="+page+"&iwhpl=false&groupBy=ws&groupBy=et&groupBy=tc&groupBy=p&groupBy=ao&groupBy=wm&groupBy=ua&groupBy=wpl&groupBy=its&groupBy=itth&groupBy=ap&groupBy=jobicon&groupBy=hs&subservice=1";
            ResponseEntity<String> response = getDataFromPracuj(uri,xsrfToken);
            xsrfToken = response.getHeaders().getValuesAsList("Set-Cookie").get(1).split(";")[0].split("=")[1];
            JsonObject object = JsonParser.parseString(response.getBody()).getAsJsonObject();
            totalPages = (int) Math.ceil(Double.parseDouble(object.get("groupedOffersTotalCount").toString()) / 50);
            String data = object.get("groupedOffers").getAsJsonArray().toString();
            offerService.saveAllPracujOffers(data);

            try {
                allOffers.addAll(mapper.readValue(data, new TypeReference<List<PracujOffer>>(){}));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            System.out.println("data from Pracuj.pl have been correctly saved");

            if (pracujIsBuild)
                return;

            page++;

        }while (page != totalPages+1);

        pracujIsBuild = true;

        List<String> allGroupId = new ArrayList<>();
        allOffers.stream().forEach(offer -> allGroupId.add(offer.getGroupId()));
        List<PracujOffer> offerToDelete = offerService.findAllPracujOffers().stream().filter(offer -> !allGroupId.contains(offer.getGroupId())).collect(Collectors.toList());
        offerToDelete.stream().forEach(offer -> offer.setStatus("expired"));

        System.out.println("data from pracuj.pl have been correctly saved");
    }

    private ResponseEntity<String> getDataFromPracuj(String uri, String xsrfToken){

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Referer", "https://it.pracuj.pl/praca");
        headers.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/127.0.0.0 Safari/537.36");

        if(!xsrfToken.isEmpty()) {
            headers.set("X-Xsrf-Token", xsrfToken);
        }

        HttpEntity<String> entity = new HttpEntity<>(headers);

        System.out.println("data from Pracuj.pl have been correctly downloaded");

        return restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
    }

    public void ScrapJoinIt(){

        int page = 1;
        int totalPages;
        List<JoinItOffer> allOffers = new ArrayList<>();
        System.out.println("is build in justit value: " + joinItIsBuild);

        do {
            String uri = "https://api.justjoin.it/v2/user-panel/offers?experienceLevels[]=junior&page="+page+"&sortBy=published&orderBy=DESC&perPage=100&salaryCurrencies=PLN";
            String dataFromApi = getDataFromJoinIt(uri);
            JsonObject object = JsonParser.parseString(dataFromApi).getAsJsonObject();
            String data = object.get("data").getAsJsonArray().toString();
            totalPages = Integer.parseInt(object.get("meta").getAsJsonObject().get("totalPages").toString());

            try {
                allOffers.addAll(mapper.readValue(data, new TypeReference<List<JoinItOffer>>(){}));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            offerService.saveAllJoinItOffers(data);

            if(joinItIsBuild)
                return;

            page++;

        }while(page != totalPages+1);

        joinItIsBuild = true;

        List<String> allSlugs = new ArrayList<>();
        allOffers.stream().forEach(offer -> allSlugs.add(offer.getSlug()));
        List<JoinItOffer> offerToDelete = offerService.findAllJoinItOffers().stream().filter(offer -> !allSlugs.contains(offer.getSlug())).collect(Collectors.toList());
        offerToDelete.stream().forEach(offer -> offer.setStatus("expired"));

        System.out.println("data from justjoin.it have been correctly saved");
    }

    private String getDataFromJoinIt(String uri){

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Accept", " */*");
        headers.set("Origin", "https://justjoin.it");
        headers.set("Priority", "u=1, i");
        headers.set("Referer", "https://justjoin.it/");
        headers.set("Sec-CH-UA", "\"Not)A;Brand\";v=\"99\", \"Google Chrome\";v=\"127\", \"Chromium\";v=\"127\"");
        headers.set("Sec-CH-UA-Mobile", "?0");
        headers.set("Sec-CH-UA-Platform", "\"Windows\"");
        headers.set("Sec-Fetch-Dest", "empty");
        headers.set("Sec-Fetch-Mode", "cors");
        headers.set("Sec-Fetch-Site", "same-site");
        headers.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/127.0.0.0 Safari/537.36");
        headers.set("Version", "2");

        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        ResponseEntity<String> response =
                restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);

        System.out.println("data from justjoin.it have been correctly downloaded");
        return response.getBody();

    }
}
