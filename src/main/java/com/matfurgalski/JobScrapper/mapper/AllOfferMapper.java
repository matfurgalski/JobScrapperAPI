package com.matfurgalski.JobScrapper.mapper;

import com.matfurgalski.JobScrapper.dto.AllOfferDto;
import com.matfurgalski.JobScrapper.model.AllOffer;
import com.matfurgalski.JobScrapper.model.JoinItOffer;
import com.matfurgalski.JobScrapper.model.PracujOffer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AllOfferMapper {
    public static AllOffer pracujToAllOffer(PracujOffer pracujOffer){
        AllOffer allOffer = AllOffer
                .builder()
                .status(pracujOffer.getStatus())
                .site("pracuj.pl")
                .technologies(new ArrayList<>(pracujOffer.getTechnologies()))
                .jobTitle(pracujOffer.getJobTitle())
                .companyProfileAbsoluteUri(pracujOffer.getCompanyProfileAbsoluteUri())
                .lastPublicated(pracujOffer.getLastPublicated())
                .expirationDate(pracujOffer.getExpirationDate())
                .salaryDisplayText(pracujOffer.getSalaryDisplayText())
                .jobDescription(pracujOffer.getJobDescription())
                .positionLevels(new ArrayList<>(pracujOffer.getPositionLevels()))
                .typesOfContract(new ArrayList<>(pracujOffer.getTypesOfContract()))
                .workSchedules(new ArrayList<>(pracujOffer.getWorkSchedules()))
                .workModes(new ArrayList<>(pracujOffer.getWorkModes()))
                .build();

        List<AllOffer.JobOffer> jobOffers = pracujOffer.getOffers().stream().map(offer -> {
            AllOffer.JobOffer jobOffer = AllOffer.JobOffer.builder()
                    .offerAbsoluteUri(offer.getOfferAbsoluteUri())
                    .displayWorkplace(offer.getDisplayWorkplace())
                    .allOffer(allOffer) // Set the foreign key reference
                    .build();
            return jobOffer;
        }).collect(Collectors.toList());

        allOffer.setJobOffers(jobOffers);

        return allOffer;
    }

    public static AllOffer joinItToAllOffer(JoinItOffer joinItOffer){

        AllOffer allOffer = AllOffer
                .builder()
                .status(joinItOffer.getStatus())
                .site("justjoin.it")
                .technologies(new ArrayList<>(joinItOffer.getRequiredSkills()))
                .jobTitle(joinItOffer.getTitle())
                .lastPublicated(joinItOffer.getPublishedAt())
                .salaryDisplayText(joinItOffer.getEmploymentTypes().get(0).getFrom_pln() + "-" + joinItOffer.getEmploymentTypes().get(0).getTo_pln() + " zł / mies. (zal. od umowy)" )
                .jobDescription(joinItOffer.getSlug())
                .positionLevels(new ArrayList<>(List.of(
                        joinItOffer.getExperienceLevel()
                )))
                .typesOfContract(new ArrayList<>(List.of(
                        joinItOffer.getEmploymentTypes().get(0).getType()
                )))
                .workSchedules(new ArrayList<>(List.of(
                        joinItOffer.getWorkingTime()
                )))
                .workModes(new ArrayList<>(List.of(
                        joinItOffer.getWorkplaceType()
                )))
                .build();

        List<AllOffer.JobOffer> jobOffers = joinItOffer.getMultilocation().stream().map(offer -> {
            AllOffer.JobOffer jobOffer = AllOffer.JobOffer.builder()
                    .offerAbsoluteUri("https://justjoin.it/offers/" + offer.getSlug())
                    .displayWorkplace(offer.getCity())
                    .allOffer(allOffer) // Set the foreign key reference
                    .build();
            return jobOffer;
        }).collect(Collectors.toList());

        allOffer.setJobOffers(jobOffers);

        return allOffer;
    }

    public static AllOfferDto toDto(AllOffer allOffer) {
        return AllOfferDto.builder()
                .site(allOffer.getSite())
                .technologies(new ArrayList<>(allOffer.getTechnologies()))
                .jobTitle(allOffer.getJobTitle())
                .companyProfileAbsoluteUri(allOffer.getCompanyProfileAbsoluteUri())
                .lastPublicated(allOffer.getLastPublicated())
                .expirationDate(allOffer.getExpirationDate())
                .salaryDisplayText(allOffer.getSalaryDisplayText())
                .jobDescription(allOffer.getJobDescription())
                .positionLevels(new ArrayList<>(allOffer.getPositionLevels()))
                .typesOfContract(new ArrayList<>(allOffer.getTypesOfContract()))
                .workSchedules(new ArrayList<>(allOffer.getWorkSchedules()))
                .workModes(new ArrayList<>(allOffer.getWorkModes()))
                .jobOffers(allOffer.getJobOffers().stream()
                        .map(AllOfferMapper::toDto)
                        .collect(Collectors.toList()))
                .build();
    }

    private static AllOfferDto.JobOfferDTO toDto(AllOffer.JobOffer jobOffer) {
        return AllOfferDto.JobOfferDTO.builder()
                .offerAbsoluteUri(jobOffer.getOfferAbsoluteUri())
                .displayWorkplace(jobOffer.getDisplayWorkplace())
                .build();
    }
}
