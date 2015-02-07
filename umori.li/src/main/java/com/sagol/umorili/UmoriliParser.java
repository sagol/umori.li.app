package com.sagol.umorili;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UmoriliParser {

    private JSONParser jsonParser = new JSONParser();

    private static String md5(final String s) {
        try {
            MessageDigest digest = java.security.MessageDigest
                    .getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public UmoriliDataContent sources () {

        UmoriliDataContent udc = UmoriliApplication.getUDCSources();
        if (udc == null)
            udc = new UmoriliDataContent();
        else return udc;

        JSONArray jsonArray = null;
        final String site = "site";
        final String name = "name";
        final String desc = "desc";

        try {
            jsonArray = jsonParser.getJSONFromUrl(UmoriliApplication.getAppContext().getResources().getString(R.string.umorili_api_url) + "sources");
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (jsonArray == null)
        {
            try {
                udc = deserialize(true, null);
            } catch (IOException e) {
                e.printStackTrace();
                udc = null;
            }
            UmoriliApplication.setUDCSources(udc);
            return udc;
        }

        Integer counter = 0;
        udc.addItem(new UmoriliDataContent.DataItem("random", null, null, "Случайные", null, null));

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
        if (udc.ITEMS.size() > 1) {
            try {
                serialize(udc, true, null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            try {
                udc = deserialize(true, null);
            } catch (IOException e) {
                e.printStackTrace();
                udc = null;
            }
        }
        UmoriliApplication.setUDCSources(udc);
        return udc;
    }

    public void caching(UmoriliDataContent udc) {
        if (udc.ITEMS.size() > 1) {
            for (int i = 1; i < udc.ITEMS.size(); i++) {
                UmoriliDataContent.DataItem item = udc.ITEMS.get(i);
                get(item.site, item.name, 100);
            }
            get(100);
        }
    }

    public void caching(UmoriliDataContent.DataItem item) {
        if (item != null)
            get(item.site, item.name, 100);
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

        if (jsonArray == null){
            try {
                udc = deserialize(false, url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return udc;
        }

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
        if (udc.ITEMS.size() > 0) {
            try {
                serialize(udc, false, url);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            try {
                udc = deserialize(false, url);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return udc;
    }

    public UmoriliDataContent get (String isite, String iname, Integer inum) {

        String url = UmoriliApplication.getAppContext().getResources().getString(R.string.umorili_api_url) +"get?site=";
        try {
            url += URLEncoder.encode(isite, "utf-8") + "&name=" + URLEncoder.encode(iname, "utf-8")
                    + "&num=" + Integer.toString(inum);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return get(url);
    }

    public UmoriliDataContent get (Integer inum) {

        String url = UmoriliApplication.getAppContext().getResources().getString(R.string.umorili_api_url) + "random?num=" + Integer.toString(inum);

        return get(url);
    }

    private void serialize(UmoriliDataContent udc, boolean sources, String url) throws IOException {
        String fname = "umorilis.cash";
        if (!sources) fname = md5(url) + ".cash";
        synchronized (this) {
            FileOutputStream fos = new FileOutputStream(new File(
                UmoriliApplication.getAppContext().getFilesDir().getParentFile().getPath() +
                        File.separator + fname), false);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(udc);
            oos.flush();
            oos.close();
        }
    }

    private UmoriliDataContent deserialize(boolean sources, String url) throws IOException {
        String fname = "umorilis.cash";
        if (!sources && url != null) fname = md5(url) + ".cash";
        FileInputStream fis = new FileInputStream(new File(
                UmoriliApplication.getAppContext().getFilesDir().getParentFile().getPath() +
                        File.separator + fname));
        UmoriliDataContent udc = new UmoriliDataContent();
        ObjectInputStream oin = new ObjectInputStream(fis);
        try {
            udc = (UmoriliDataContent) oin.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return udc;
    }
}
