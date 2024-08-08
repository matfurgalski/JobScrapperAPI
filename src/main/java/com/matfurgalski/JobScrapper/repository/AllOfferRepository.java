package com.matfurgalski.JobScrapper.repository;

import com.matfurgalski.JobScrapper.model.AllOffer;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AllOfferRepository extends JpaRepository<AllOffer, Long>, JpaSpecificationExecutor<AllOffer> {

}
