package com.matfurgalski.JobScrapper.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class AllOffer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private long id;
    private String site;
    @ElementCollection
    @Fetch(FetchMode.SUBSELECT)
    @CollectionTable(name="all_offers_technologies", joinColumns=@JoinColumn(name="all_offer_id"))
    @Column(name="name")
    private List<String> technologies = new ArrayList<>();
    private String jobTitle;
    private String companyProfileAbsoluteUri;
    private String lastPublicated;
    private String expirationDate;
    private String salaryDisplayText;
    @Column(length = 5000)
    private String jobDescription;
    private String status;
    @ElementCollection
    @Fetch(FetchMode.SUBSELECT)
    @CollectionTable(name="all_offers_position_levels", joinColumns=@JoinColumn(name="all_offer_id"))
    @Column(name="name")
    private List<String> positionLevels = new ArrayList<>();
    @ElementCollection
    @Fetch(FetchMode.SUBSELECT)
    @CollectionTable(name="all_offers_types_of_contract", joinColumns=@JoinColumn(name="all_offer_id"))
    @Column(name="name")
    private List<String> typesOfContract = new ArrayList<>();
    @ElementCollection
    @Fetch(FetchMode.SUBSELECT)
    @CollectionTable(name="all_offers_work_schedules", joinColumns=@JoinColumn(name="all_offer_id"))
    @Column(name="name")
    private List<String> workSchedules = new ArrayList<>();
    @ElementCollection
    @Fetch(FetchMode.SUBSELECT)
    @CollectionTable(name="all_offers_work_modes", joinColumns=@JoinColumn(name="all_offer_id"))
    @Column(name="name")
    private List<String> workModes = new ArrayList<>();
    @JsonManagedReference
    @Fetch(FetchMode.SUBSELECT)
    @OneToMany(mappedBy = "allOffer", cascade = CascadeType.ALL, fetch=FetchType.LAZY, orphanRemoval = true)
    private List<JobOffer> jobOffers = new ArrayList<>();


    @Getter
    @Setter
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    @NoArgsConstructor
    @AllArgsConstructor
    @Entity
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class JobOffer implements Serializable {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @EqualsAndHashCode.Include
        private long id;
        private String offerAbsoluteUri;
        private String displayWorkplace;
        @JsonBackReference
        @ManyToOne(targetEntity = AllOffer.class, fetch = FetchType.LAZY)
        @JoinColumn(name = "all_offer_id", referencedColumnName = "id", updatable = true, insertable = true)
        AllOffer allOffer;
    }
}