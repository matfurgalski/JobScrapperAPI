package com.matfurgalski.JobScrapper.repository;

import com.matfurgalski.JobScrapper.model.AllOffer;
import com.matfurgalski.JobScrapper.model.PracujOffer;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PracujOfferRepository extends JpaRepository<PracujOffer,Long> {
    List<PracujOffer> findByStatusNotLike(String status);
}
