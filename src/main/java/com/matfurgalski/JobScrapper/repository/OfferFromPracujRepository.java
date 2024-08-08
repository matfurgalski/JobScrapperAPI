package com.matfurgalski.JobScrapper.repository;

import com.matfurgalski.JobScrapper.model.PracujOffer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OfferFromPracujRepository extends JpaRepository<PracujOffer.Offer, Long> {

}
