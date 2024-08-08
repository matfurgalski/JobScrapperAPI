package com.matfurgalski.JobScrapper.dto;

import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AllOfferDto {
    private String site;
    private List<String> technologies;
    private String jobTitle;
    private String companyProfileAbsoluteUri;
    private String lastPublicated;
    private String expirationDate;
    private String salaryDisplayText;
    private String jobDescription;
    private List<String> positionLevels;
    private List<String> typesOfContract;
    private List<String> workSchedules;
    private List<String> workModes;
    private List<JobOfferDTO> jobOffers;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class JobOfferDTO {
        private String offerAbsoluteUri;
        private String displayWorkplace;
    }
}