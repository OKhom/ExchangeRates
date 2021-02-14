package com.javapro.utils;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Server {
    private static final String URL = "https://api.privatbank.ua/p24api/";
    private static final String endpoint = "exchange_rates?json&date=";

    public static String getUrl = URL + endpoint;

    public static String request (String endpoint) throws IOException {
        URL url = new URL(getUrl + endpoint);
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        String strBuf;
        try (InputStream is = http.getInputStream()) {
            byte[] buf = responseBodyToArray(is);
            strBuf = new String(buf, StandardCharsets.UTF_8);
        }
        return strBuf;
    }

    private static byte[] responseBodyToArray(InputStream is) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[10240];
        int r;
        do {
            r = is.read(buf);
            if (r > 0) bos.write(buf, 0, r);
        } while (r != -1);
        return bos.toByteArray();
    }
}
