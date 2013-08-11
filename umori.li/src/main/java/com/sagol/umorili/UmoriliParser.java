package com.sagol.umorili;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class UmoriliParser {

    private JSONParser jsonParser = new JSONParser();

    public UmoriliDataContent sources () {

        UmoriliDataContent udc = new UmoriliDataContent();

        JSONArray jsonArray = null;
        final String site = "site";
        final String name = "name";
        final String desc = "desc";

        try {
            jsonArray = jsonParser.getJSONFromUrl("http://www.umori.li/api/sources");
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (jsonArray == null)
            return udc;

        Integer counter = 0;

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONArray a = jsonArray.getJSONArray(i);
                for (int j = 0; j < a.length(); j++) {
                    JSONObject c = a.getJSONObject(j);
                    udc.addItem(new UmoriliDataContent.DataItem(Integer.toString(counter++),
                            c.getString(site), c.getString(name), c.getString(desc), null,
                            null));
                }
            } catch (JSONException e)

            {
                e.printStackTrace();
            }
        }
        udc.addItem(new UmoriliDataContent.DataItem("random", null, null, "Случайные", null, null));

        return udc;
    }

    private UmoriliDataContent get (String url) {
        UmoriliDataContent udc = new UmoriliDataContent();

        JSONArray jsonArray = null;
        final String site = "site";
        final String name = "name";
        final String desc = "desc";
        final String link = "link";
        final String element = "elementPureHtml";

        try {
            jsonArray = jsonParser.getJSONFromUrl(url);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (jsonArray == null)
            return udc;

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject c = jsonArray.getJSONObject(i);
                udc.addItem(new UmoriliDataContent.DataItem(Integer.toString(i), c.getString(site),
                        c.getString(name), c.getString(desc), c.getString(link), c.getString(element)));
            } catch (JSONException e)

            {
                e.printStackTrace();
            }
        }
        return udc;
    }

    public UmoriliDataContent get (String isite, String iname, Integer inum) {

        String url = "http://www.umori.li/api/get?site=";
        try {
            url += URLEncoder.encode(isite, "utf-8") + "&name=" + URLEncoder.encode(iname, "utf-8")
                    + "&num=" + Integer.toString(inum);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return get(url);
    }

    public UmoriliDataContent get (Integer inum) {

        String url = "http://www.umori.li/api/random?num=" + Integer.toString(inum);

        return get(url);
    }

}
