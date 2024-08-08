package com.matfurgalski.JobScrapper.service;

import com.matfurgalski.JobScrapper.dto.AllOfferDto;
import com.matfurgalski.JobScrapper.model.JoinItOffer;
import com.matfurgalski.JobScrapper.model.AllOffer;
import com.matfurgalski.JobScrapper.model.PracujOffer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OfferService {

    Iterable<JoinItOffer> saveAllJoinItOffers(String data);
    Iterable<PracujOffer> saveAllPracujOffers(String data);
    Iterable<AllOffer> mergeOffers();
    List<JoinItOffer> findAllJoinItOffers();
    List<PracujOffer>findAllPracujOffers();

}
