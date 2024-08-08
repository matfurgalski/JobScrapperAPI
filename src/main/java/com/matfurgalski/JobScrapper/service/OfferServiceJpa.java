package com.matfurgalski.JobScrapper.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.matfurgalski.JobScrapper.dto.AllOfferDto;
import com.matfurgalski.JobScrapper.mapper.AllOfferMapper;
import com.matfurgalski.JobScrapper.model.JoinItOffer;
import com.matfurgalski.JobScrapper.model.AllOffer;
import com.matfurgalski.JobScrapper.model.PracujOffer;
import com.matfurgalski.JobScrapper.repository.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class OfferServiceJpa implements OfferService {

    private final JoinItOfferRepository joinItOfferRepository;
    private final PracujOfferRepository pracujOfferRepository;
    private final OfferFromPracujRepository offerFromPracujRepository;
    private final AllOfferRepository allOfferRepository;
    private final ObjectMapper mapper;

    public OfferServiceJpa(JoinItOfferRepository joinItOfferRepository, PracujOfferRepository pracujOfferRepository, OfferFromPracujRepository offerFromPracujRepository, AllOfferRepository allOfferRepository,  ObjectMapper mapper) {
        this.joinItOfferRepository = joinItOfferRepository;
        this.pracujOfferRepository = pracujOfferRepository;
        this.offerFromPracujRepository = offerFromPracujRepository;
        this.allOfferRepository = allOfferRepository;
        this.mapper = mapper;
    }

    @Override
    public List<JoinItOffer> findAllJoinItOffers(){
        return joinItOfferRepository.findAll();
    };

    @Override
    public List<PracujOffer>findAllPracujOffers(){
        return pracujOfferRepository.findAll();
    };

    @Override
    public Iterable<PracujOffer> saveAllPracujOffers(String data){
        List<PracujOffer> offers;

        try {
            offers = mapper.readValue(data, new TypeReference<List<PracujOffer>>(){});
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        List<PracujOffer> offerToSave = offers;

        if(offerToSave.isEmpty()){
            return null;
        }
        List<String> offerGroupId= new ArrayList<>();
        List<PracujOffer.Offer> offersFromDb = offerFromPracujRepository.findAll();

        if(!offersFromDb.isEmpty()) {
            pracujOfferRepository.findAll().stream().forEach(offer -> offerGroupId.add(offer.getGroupId()));
            offerToSave = offers.stream().filter(offer -> !offerGroupId.contains(offer.getGroupId())).toList();
        }
        return pracujOfferRepository.saveAll(offerToSave);
    };

    @Override
    public Iterable<JoinItOffer>  saveAllJoinItOffers(String data) {
        List<JoinItOffer> offers;
        List<String> slugArrayFromDb = new ArrayList<>();

        try {
            offers = mapper.readValue(data, new TypeReference<List<JoinItOffer>>(){});
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        List<JoinItOffer> offerToSave = offers;
        List<JoinItOffer> offersFromDb = joinItOfferRepository.findAll();

        if(!offersFromDb.isEmpty()) {
            offersFromDb.stream().forEach(offer -> slugArrayFromDb.add(offer.getSlug()));
            offerToSave = offers.stream().filter(offer -> !slugArrayFromDb.contains(offer.getSlug())).toList();
        }

       return joinItOfferRepository.saveAll(offerToSave);
    }

    public Iterable<AllOffer> mergeOffers(){

        List<AllOffer> allPracujAllOffers = pracujOfferRepository.findByStatusNotLike("expired").stream().map(AllOfferMapper::pracujToAllOffer).collect(Collectors.toList());
        List<AllOffer> allJoinItAllOffers = joinItOfferRepository.findByStatusNotLike("expired").stream().map(AllOfferMapper::joinItToAllOffer).collect(Collectors.toList());

        List<AllOffer> allAllOffers = new ArrayList<>();
        allAllOffers.addAll(allPracujAllOffers);
        allAllOffers.addAll(allJoinItAllOffers);

        List<String> offerTitles = new ArrayList<>();
        List<AllOffer> offersFromDb = allOfferRepository.findAll();

                if(!offersFromDb.isEmpty()) {
                    offersFromDb.stream().forEach(offer -> offerTitles.add(offer.getJobTitle()));
                    allAllOffers = allAllOffers.stream().filter(offer -> !offerTitles.contains(offer.getJobTitle())).toList();
        }
        return allOfferRepository.saveAll(allAllOffers);
    }
}


