package me.hyoputer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

public class Gist {
    private String accessToken;

    private static String GIST_URL = "https://api.github.com/gists";

    public Gist(String accessToken) {
        this.accessToken = accessToken;
    }

    public JsonArray getGists() throws IOException {

        URL url = new URL(GIST_URL);

        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "token " + accessToken);
        connection.setRequestProperty("Accept", "application/vnd.github.v3+json");
        connection.setDoOutput(true);

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        return JsonParser.parseReader(in).getAsJsonArray();
    }
    
}
