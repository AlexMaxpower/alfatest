package ru.coolspot.alfatest.controllers;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.coolspot.alfatest.clients.Giphy;
import ru.coolspot.alfatest.exceptions.ValidationException;
import ru.coolspot.alfatest.services.Service;
import ru.coolspot.alfatest.utils.Utils;

import java.io.FileInputStream;
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

    public String imageUrl;
    @Value("${uptag}")
    private String upTag;
    @Value("${downtag}")
    private String downTag;
    @Value("${base}")
    private String baseCurrency;

    @Value("${nochange.image}")
    private String noChangeImage;

    @Value("${up.image}")
    private String upImage;

    @Value("${down.image}")
    private String downImage;

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
        Boolean isLocal = true;

        if (rateIncreased == null) {
            imageUrl = noChangeImage;
        } else if (rateIncreased) {
            try {
                imageUrl = Utils.getUrlGif(giphy.getRandomGifByTag(upTag));
                isLocal = false;
            } catch (Exception e) {
                log.warn(e.toString());
                imageUrl = upImage;
            }

        } else {
            try {
                imageUrl = Utils.getUrlGif(giphy.getRandomGifByTag(downTag));
                isLocal = false;
            } catch (Exception e) {
                log.warn(e.toString());
                imageUrl = downImage;
            }
        }

        InputStream in;
        if (isLocal) {
            Resource resource = new ClassPathResource("./static/" + imageUrl);
            in = resource.getInputStream();
        } else {
            in = new URL(imageUrl).openStream();
        }
        return IOUtils.toByteArray(in);
    }
}
