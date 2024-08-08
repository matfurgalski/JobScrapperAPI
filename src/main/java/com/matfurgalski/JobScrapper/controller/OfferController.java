package com.matfurgalski.JobScrapper.controller;

import com.matfurgalski.JobScrapper.dto.AllOfferDto;
import com.matfurgalski.JobScrapper.model.AllOffer;
import com.matfurgalski.JobScrapper.service.AllOfferService;
import com.matfurgalski.JobScrapper.service.OfferService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.web.PagedModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/offers")
public class OfferController {

    private final OfferService offerService;
    private final AllOfferService allOfferService;

    public OfferController(OfferService offerService, AllOfferService allOfferService) {
        this.offerService = offerService;
        this.allOfferService = allOfferService;
    }

    @GetMapping("")
    public PagedModel<AllOfferDto> getOffers(
            @RequestParam(required = false) String displayWorkplace,
            @RequestParam(required = false) List<String> technologies,
            @RequestParam(required = false) List<String> positionLevels,
            @RequestParam(required = false) List<String> workModes,
            Pageable page) {
        return new PagedModel<>(allOfferService.findAllOffers(displayWorkplace, technologies, positionLevels, workModes, page));
    }
}
