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

@Getter@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class JoinItOffer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private long id;
    private int categoryId;
    private String city;
    @Column(length = 1000)
    private String companyLogoThumbUrl;
    private String companyName;
    private String experienceLevel;
    private String latitude;
    private String longitude;
    private String publishedAt;
    private String status = "active";
    private boolean remoteInterview;
    @ElementCollection
    @Fetch(FetchMode.SUBSELECT)
    @CollectionTable(name="join_it_offer_required_skills", joinColumns=@JoinColumn(name="join_it_offer_id"))
    @Column(name="name")
    private List<String> requiredSkills = new ArrayList<>();
    @JsonManagedReference
    @Fetch(FetchMode.SUBSELECT)
    @OneToMany(mappedBy = "joinItOffer", cascade = CascadeType.ALL, fetch=FetchType.LAZY, orphanRemoval = true)
    private List<EmploymentTypes> employmentTypes = new ArrayList<>();
    private String slug;
    private String street;
    private String title;
    private String workingTime;
    private String workplaceType;
    @JsonManagedReference
    @Fetch(FetchMode.SUBSELECT)
    @OneToMany(mappedBy = "joinItOffer", cascade = CascadeType.ALL, fetch=FetchType.LAZY, orphanRemoval = true)
    private List<Multilocation> multilocation = new ArrayList<>();

    @Getter
    @Setter
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    @NoArgsConstructor
    @AllArgsConstructor
    @Entity
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class EmploymentTypes implements Serializable {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @EqualsAndHashCode.Include
        private long id;
        private String type;
        private String from_pln;
        private String to_pln;
        @JsonBackReference
        @ManyToOne(targetEntity = JoinItOffer.class, fetch = FetchType.LAZY)
        @JoinColumn(name = "joinit_offer_id", referencedColumnName = "id", updatable = true, insertable = true)
        JoinItOffer joinItOffer;
    }

    @Getter
    @Setter
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    @NoArgsConstructor
    @AllArgsConstructor
    @Entity
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Multilocation implements Serializable {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @EqualsAndHashCode.Include
        private long id;
        private String city;
        private String slug;
        @JsonBackReference
        @ManyToOne(targetEntity = JoinItOffer.class, fetch = FetchType.LAZY)
        @JoinColumn(name = "joinit_offer_id", referencedColumnName = "id", updatable = true, insertable = true)
        JoinItOffer joinItOffer;
    }
}
