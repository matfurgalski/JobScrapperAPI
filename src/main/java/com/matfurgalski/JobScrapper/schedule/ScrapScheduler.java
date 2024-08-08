package com.matfurgalski.JobScrapper.schedule;
import com.matfurgalski.JobScrapper.component.ScrapDataComponent;
import com.matfurgalski.JobScrapper.service.OfferService;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScrapScheduler {

    private final ScrapDataComponent scrapDataComponent;

    private final OfferService offerService;

    public ScrapScheduler(ScrapDataComponent scrapDataComponent, OfferService offerService) {
        this.scrapDataComponent = scrapDataComponent;
        this.offerService = offerService;
    }
    @Transactional
    @Scheduled(fixedRate = 1800000)
    public void scheduleTaskWithInitialDelay() {

        scrapDataComponent.ScrapPracuj();
        scrapDataComponent.ScrapJoinIt();
        offerService.mergeOffers();
        System.out.println("scrap successful");
    }

}