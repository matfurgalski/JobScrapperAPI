package com.matfurgalski.JobScrapper.service;
import com.matfurgalski.JobScrapper.dto.AllOfferDto;
import com.matfurgalski.JobScrapper.mapper.AllOfferMapper;
import com.matfurgalski.JobScrapper.model.AllOffer;
import com.matfurgalski.JobScrapper.repository.AllOfferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AllOfferService {

    private final AllOfferRepository allOfferRepository;

    @Autowired
    public AllOfferService(AllOfferRepository allOfferRepository) {
        this.allOfferRepository = allOfferRepository;
    }

    public Page<AllOfferDto> findAllOffers(String displayWorkplace, List<String> technologies, List<String> positionLevels, List<String> workModes, Pageable page) {
        Specification<AllOffer> spec = Specification.where(displayWorkplaceEquals(displayWorkplace))
                .and(technologiesIn(technologies))
                .and(positionLevelsIn(positionLevels))
                .and(workModesIn(workModes));

        Pageable pageRequest = PageRequest.of(page.getPageNumber(), 50);
        List<AllOfferDto> AllOffers = allOfferRepository.findAll(spec,Sort.by(Sort.Direction.DESC, "lastPublicated")).stream().map(offer -> AllOfferMapper.toDto(offer)).collect(Collectors.toList());
        int start = (int) pageRequest.getOffset();
        int end = Math.min((start + pageRequest.getPageSize()), AllOffers.size());
        List<AllOfferDto> pageContent = AllOffers.subList(start, end);

        return new PageImpl<>(pageContent, pageRequest, AllOffers.size());
    }

    private Specification<AllOffer> displayWorkplaceEquals(String displayWorkplace) {
        return (root, query, criteriaBuilder) -> {
            if (displayWorkplace == null) {
                return criteriaBuilder.conjunction();
            }
            // Join with jobOffers and filter by displayWorkplace
            return criteriaBuilder.equal(root.join("jobOffers").get("displayWorkplace"), displayWorkplace);
        };
    }

    private Specification<AllOffer> technologiesIn(List<String> technologies) {
        return (root, query, criteriaBuilder) -> {
            if (technologies == null || technologies.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return root.join("technologies").in(technologies);
        };
    }

    private Specification<AllOffer> positionLevelsIn(List<String> positionLevels) {
        return (root, query, criteriaBuilder) -> {
            if (positionLevels == null || positionLevels.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return root.join("positionLevels").in(positionLevels);
        };
    }

    private Specification<AllOffer> workModesIn(List<String> workModes) {
        return (root, query, criteriaBuilder) -> {
            if (workModes == null || workModes.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return root.join("workModes").in(workModes);
        };
    }
}