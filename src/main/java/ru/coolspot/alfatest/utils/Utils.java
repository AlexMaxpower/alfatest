package ru.coolspot.alfatest.utils;

import ru.coolspot.alfatest.exceptions.ExternalDataErrorException;

import java.util.LinkedHashMap;
import java.util.Map;

public class Utils {

    public static String getUrlGif(Map o) {

        String urlGifId = "";
        String urlGif = "";

        // 1-й вариант
        if (o != null) {
            Map data = (LinkedHashMap) o.get("data");
            urlGifId = String.valueOf((data.get("id")));
        } else {
            throw new ExternalDataErrorException("External data error at Giphy");
        }

        /* 2-й вариант
        if (o != null) {
            urlGifId = o.toString();
            int indexStartSubstring = urlGifId.indexOf("{data={type=gif, id=") + 20;
            int indexEndSubstring = urlGifId.indexOf(", url=");
            if ((indexStartSubstring > 0) && (indexEndSubstring > indexStartSubstring)) {
                urlGifId = urlGifId.substring(indexStartSubstring, indexEndSubstring);
            }
        }  else {
            throw new ExternalDataErrorException("External data error at Giphy");
        }
        */

        if (!urlGifId.isEmpty()) {
            urlGif = "https://i.giphy.com/media/" + urlGifId + "/giphy.gif";
        } else
            throw new ExternalDataErrorException("External data error at Giphy");
        return urlGif;
    }
}
