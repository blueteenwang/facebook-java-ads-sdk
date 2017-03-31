package com.facebook.ads.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tangp on 31/03/2017.
 */
public class QueryParameterUtil {

    public static Map<String, String> getQueryMap(String query) {
        if (query == null) {
            return null;
        }
        int lastIndexOfQuestionMark = query.lastIndexOf("?");
        if (lastIndexOfQuestionMark == query.length() - 1) {
            return null;
        }
        if (lastIndexOfQuestionMark >= 0) {
            query = query.substring(lastIndexOfQuestionMark + 1);
        }
        String[] params = query.split("&");
        Map<String, String> map = new HashMap<String, String>();
        for (String param : params) {
            String[] p = param.split("=");
            String name = p[0];
            if (p.length > 1) {
                String value = p[1];
                map.put(name, value);
            }
        }
        return map;
    }
}
