package com.matfurgalski.JobScrapper.repository;

import com.matfurgalski.JobScrapper.model.JoinItOffer;
import com.matfurgalski.JobScrapper.model.PracujOffer;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface JoinItOfferRepository extends JpaRepository<JoinItOffer,Long> {
    List<JoinItOffer> findByStatusNotLike(String status);
}

