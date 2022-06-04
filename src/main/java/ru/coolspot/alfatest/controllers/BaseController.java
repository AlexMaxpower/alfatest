package ru.coolspot.alfatest.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.apache.commons.io.IOUtils;
import ru.coolspot.alfatest.clients.Giphy;
import ru.coolspot.alfatest.exceptions.ValidationException;
import ru.coolspot.alfatest.services.Service;
import ru.coolspot.alfatest.utils.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

@Controller
@RequestMapping("/currency")
@Slf4j
public class BaseController {
    @Autowired
    private Giphy giphy;

    @Autowired
    private Service service;

    @Value("${nochangeimage.url}")
    private String noChange;
    public String imageUrl;
    @Value("${uptag}")
    private String upTag;
    @Value("${downtag}")
    private String downTag;
    @Value("${base}")
    private String baseCurrency;

    @GetMapping(
            value = "/{currencyCode}",
            produces = MediaType.IMAGE_GIF_VALUE
    )

    public @ResponseBody byte[] getGif(@PathVariable String currencyCode) throws IOException {
        if ((currencyCode == null) || (currencyCode.isEmpty() || (currencyCode.isBlank()))) {
            throw new ValidationException("An empty currency code!");
        }
        currencyCode = currencyCode.toUpperCase();
        log.info("Base currency = {}", baseCurrency);
        Boolean rateIncreased = service.isRateIncreased(currencyCode);
        if (rateIncreased == null) {
            imageUrl = noChange;
        } else if (rateIncreased) {
            imageUrl = Utils.getUrlGif(giphy.getRandomGifByTag(upTag));
        } else {
            imageUrl = Utils.getUrlGif(giphy.getRandomGifByTag(downTag));
        }
        InputStream in = new URL(imageUrl).openStream();
        return IOUtils.toByteArray(in);
    }
}
