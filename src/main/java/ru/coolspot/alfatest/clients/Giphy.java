package ru.coolspot.alfatest.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@FeignClient(value = "giphy", url = "${feign.giphy.url}")
public interface Giphy {
    @GetMapping("?api_key=${giphy.key}&tag={tag}&rating=g")
    Map getRandomGifByTag(@PathVariable("tag") String tag);
}