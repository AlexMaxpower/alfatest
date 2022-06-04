package ru.coolspot.alfatest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ru.coolspot.alfatest.clients.Exchanger;
import ru.coolspot.alfatest.clients.Giphy;

import ru.coolspot.alfatest.exceptions.ExternalDataErrorException;
import ru.coolspot.alfatest.exceptions.NotFoundException;
import ru.coolspot.alfatest.exceptions.ValidationException;
import ru.coolspot.alfatest.model.ExchangeRates;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;


@SpringBootTest
@AutoConfigureMockMvc
@MockBeans({@MockBean(Exchanger.class), @MockBean(Giphy.class)})
public class BaseControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Exchanger exchanger;
    @Autowired
    private Giphy giphy;
    @Value("${nochangeimage.url}")
    private String noChange;
    @Value("${uptag}")
    private String upTag;
    @Value("${downtag}")
    private String downTag;
    private ExchangeRates exchangeRatesToday = new ExchangeRates("test", "test",
            Timestamp.valueOf("2000-01-02 00:00:00"), "USD",
            new HashMap<String, Double>() {{
                put("RUB", 63.1);
                put("JPY", 130.5);
                put("AZN", 1.17);
            }});

    private ExchangeRates exchangeRatesYesterday = new ExchangeRates("test", "test",
            Timestamp.valueOf("2000-01-01 00:00:00"), "USD",
            new HashMap<>() {{
                put("RUB", 59.1);
                put("JPY", 128.5);
                put("AZN", 1.17);
            }});

    private Map<String, String> giphyDataMapUp = new LinkedHashMap<>() {{
        put("type", "gif");
        put("id", "wdUYfojBtiosK9UMCY");
        put("url", "https://giphy.com/gifs/discoverychannel-gold-rush-dave-turin-lost-mine-wdUYfojBtiosK9UMCY");
    }};

    private Map<String, LinkedHashMap> giphyMapUp = new LinkedHashMap<>() {{
        put("data", (LinkedHashMap) giphyDataMapUp);
    }};

    private Map<String, String> giphyDataMapDown = new LinkedHashMap<>() {{
        put("type", "gif");
        put("id", "l2Sq8EYhA66vdOfAI");
        put("url", "https://giphy.com/gifs/car-hate-engine-l2Sq8EYhA66vdOfAI");
    }};

    private Map<String, LinkedHashMap> giphyMapDown = new LinkedHashMap<>() {{
        put("data", (LinkedHashMap) giphyDataMapDown);
    }};

    private ExchangeRates exchangeRatesWrong = null;
    private Map<String, LinkedHashMap> giphyMapWrong = null;

    @BeforeEach
    void beforeEach() {
        Mockito.when(exchanger.getLatest()).thenReturn(exchangeRatesToday);
        Mockito.when(exchanger.getHistorical(String.valueOf(LocalDate.now().minusDays(1)))).thenReturn(exchangeRatesYesterday);
        Mockito.when(giphy.getRandomGifByTag(upTag)).thenReturn(giphyMapUp);
        Mockito.when(giphy.getRandomGifByTag(downTag)).thenReturn(giphyMapDown);
    }

    @Test
    void shouldReturnOkAndGifWhenCurrencyLowercase() throws Exception {
        mockMvc.perform(get("/currency/rub"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_GIF_VALUE));
    }

    @Test
    void shouldReturnOkAndGifWhenCurrencyUppercase() throws Exception {
        mockMvc.perform(get("/currency/RUB"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_GIF_VALUE));
    }

    @Test
    void shouldReturnNotFoundWhenCurrencyWrong() throws Exception {
        mockMvc.perform(get("/currency/r1b"))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException))
                .andExpect(result -> assertEquals("Currency = R1B not found!",
                        result.getResolvedException().getMessage()))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnBadRequestWhenCurrencyBlank() throws Exception {
        mockMvc.perform(get("/currency/ "))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ValidationException))
                .andExpect(result -> assertEquals("An empty currency code!",
                        result.getResolvedException().getMessage()))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadGatewayWhenExternalDataErrorAtExchanger() throws Exception {
        Mockito.when(exchanger.getLatest()).thenReturn(exchangeRatesWrong);
        mockMvc.perform(get("/currency/rub"))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ExternalDataErrorException))
                .andExpect(result -> assertEquals("External data error at Exchanger",
                        result.getResolvedException().getMessage()))
                .andExpect(status().isBadGateway());
    }

    @Test
    void shouldReturnBadGatewayWhenExternalDataErrorAtGiphy() throws Exception {
        Mockito.when(giphy.getRandomGifByTag(upTag)).thenReturn(giphyMapWrong);
        mockMvc.perform(get("/currency/rub"))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ExternalDataErrorException))
                .andExpect(result -> assertEquals("External data error at Giphy",
                        result.getResolvedException().getMessage()))
                .andExpect(status().isBadGateway());
    }
}
