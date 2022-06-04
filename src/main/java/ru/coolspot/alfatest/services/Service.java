package ru.coolspot.alfatest.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.coolspot.alfatest.clients.Exchanger;
import ru.coolspot.alfatest.exceptions.ExternalDataErrorException;
import ru.coolspot.alfatest.exceptions.NotFoundException;
import ru.coolspot.alfatest.model.ExchangeRates;

import java.time.LocalDate;

@Slf4j
@Component
public class Service {

    @Autowired
    private Exchanger exchanger;

    public Boolean isRateIncreased(String currencyCode) {
        Boolean result;

        ExchangeRates exchangeRatesLatest = exchanger.getLatest();
        LocalDate date = LocalDate.now().minusDays(1);
        ExchangeRates exchangeRatesYesterday = exchanger.getHistorical(date.toString());

        if ((exchangeRatesLatest == null) || (exchangeRatesYesterday == null)) {
            throw new ExternalDataErrorException("External data error at Exchanger");
        }

        Double latestRate = exchangeRatesLatest.getRates().get(currencyCode);
        Double yesterRate = exchangeRatesYesterday.getRates().get(currencyCode);
        if ((latestRate == null) || (yesterRate == null)) {
            throw new NotFoundException("Currency = " + currencyCode + " not found!");
        }

        if (latestRate > yesterRate) {
            log.info("Currency rate {} has increased! Yesterday={}; Today={}", currencyCode, yesterRate, latestRate);
            result = true;
        } else if (latestRate < yesterRate) {
            log.info("Currency rate {} has declined! Yesterday={}; Today={}", currencyCode, yesterRate, latestRate);
            result = false;
        } else {
            log.info("Currency rate {} has not changed! Yesterday={}; Today={}", currencyCode, yesterRate, latestRate);
            result = null;
        }
        return result;
    }
}
