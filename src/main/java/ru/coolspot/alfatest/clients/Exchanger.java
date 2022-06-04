package ru.coolspot.alfatest.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.coolspot.alfatest.model.ExchangeRates;

@FeignClient(value = "exchanger", url = "${feign.exchanger.url}")
public interface Exchanger {

    @GetMapping("latest.json?app_id=${exchanger.id}&base=${base}")
    ExchangeRates getLatest();

    @GetMapping("historical/{date}.json?app_id=${exchanger.id}&base=${base}")
    ExchangeRates getHistorical(@PathVariable("date") String date);
}


