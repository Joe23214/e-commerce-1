/*
package com.company.ecomShop.scheduler;

import com.company.ecomShop.app.CarrelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class AvvisoScheduler {
    private final CarrelloService carrelloService;

    public AvvisoScheduler(CarrelloService carrelloService) {
        this.carrelloService = carrelloService;
    }

    */
/**
     * Controlla ogni 24h gli avvisi e invia email se giacenze > 0
     *//*

    @Scheduled(fixedRate = 86400000) // 24h in millisecondi
    public void controllaAvvisi() {
        carrelloService.processAvvisi();
    }
}*/

package com.company.ecomShop.scheduler;

import com.company.ecomShop.app.CarrelloService;
import groovy.util.logging.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static org.reflections.Reflections.log;

@Component
@Slf4j
public class AvvisoScheduler {

    private final CarrelloService carrelloService;

    public AvvisoScheduler(CarrelloService carrelloService) {
        this.carrelloService = carrelloService;
    }

    /**
     * Controlla ogni giorno a mezzanotte gli avvisi e invia email se giacenze > 0
     */
    @Scheduled(cron = "0 30 * * * *")
    public void controllaAvvisi() {
        log.info("Avvio controllo avvisi prodotti");
        carrelloService.processAvvisi();
        log.info("Fine controllo avvisi prodotti");
    }
}
