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
@JsonIgnoreProperties(ignoreUnknown = true)

public class PracujOffer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private long id;
    @ElementCollection
    @Fetch(FetchMode.SUBSELECT)
    @CollectionTable(name = "technologies", joinColumns = @JoinColumn(name = "pracuj_offers_id"))
    @Column(name = "name")
    private List<String> technologies = new ArrayList<>();
    private String aboutProjectShortDescription;

    private String groupId;
    private String jobTitle;
    private String companyName;
    private String companyProfileAbsoluteUri;
    private long companyId;
    private String companyLogoUri;
    private String lastPublicated;
    private String expirationDate;
    private String salaryDisplayText;
    @Column(length = 5000)
    private String jobDescription;
    private String status = "active";
    private boolean isSuperOffer;
    private boolean isFranchise;
    private boolean isOptionalCv;
    private boolean isOneClickApply;
    private boolean isJobiconCompany;
    @ElementCollection
    @Fetch(FetchMode.SUBSELECT)
    @CollectionTable(name = "pracuj_offer_position_levels", joinColumns = @JoinColumn(name = "pracuj_offer_id"))
    @Column(name = "position_level")
    private List<String> positionLevels = new ArrayList<>();
    @ElementCollection
    @Fetch(FetchMode.SUBSELECT)
    @CollectionTable(name = "pracuj_offer_types_of_contract", joinColumns = @JoinColumn(name = "pracuj_offer_id"))
    @Column(name = "type_of_contract")
    private List<String> typesOfContract = new ArrayList<>();
    @ElementCollection
    @Fetch(FetchMode.SUBSELECT)
    @CollectionTable(name = "pracuj_offer_work_schedules", joinColumns = @JoinColumn(name = "pracuj_offer_id"))
    @Column(name = "work_schedule")
    private List<String> workSchedules = new ArrayList<>();
    @ElementCollection
    @Fetch(FetchMode.SUBSELECT)
    @CollectionTable(name = "pracuj_offer_work_modes", joinColumns = @JoinColumn(name = "pracuj_offer_id"))
    @Column(name = "work_mode")
    private List<String> workModes = new ArrayList<>();
    @JsonManagedReference
    @Fetch(FetchMode.SUBSELECT)
    @OneToMany(mappedBy = "pracujOffer", cascade = CascadeType.ALL, fetch=FetchType.LAZY, orphanRemoval = true)
    private List<Offer> offers = new ArrayList<>();

    @Getter
    @Setter
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    @NoArgsConstructor
    @AllArgsConstructor
    @Entity
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Offer implements Serializable {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @EqualsAndHashCode.Include
        private long id;
        private long partitionId;
        private String offerAbsoluteUri;
        private String displayWorkplace;
        private boolean isWholePoland;
        @JsonBackReference
        @ManyToOne(targetEntity = PracujOffer.class, fetch = FetchType.LAZY)
        @JoinColumn(name = "pracuj_offer_id", referencedColumnName = "id", updatable = true, insertable = true)
        PracujOffer pracujOffer;

    }
}
