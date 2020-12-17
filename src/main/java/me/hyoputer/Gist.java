package me.hyoputer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import javafx.util.Pair;

public class Gist {
    private String accessToken;

    private static String GIST_URL = "https://api.github.com/gists";

    public Gist(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getGistId(String description) throws Exception {
        JsonArray gists = getGists();

        List<Pair<String, String>> steamGameIDs = new ArrayList<>();
        gists.forEach(json -> steamGameIDs
                .add(new Pair<String, String>(json.getAsJsonObject().get("description").getAsString(),
                        json.getAsJsonObject().get("id").getAsString())));

        for (Pair<String, String> pair : steamGameIDs) {
            if (pair.getKey().equals(description))
                return pair.getValue();
        }
        
        throw new Exception();
    }

    private JsonArray getGists() throws IOException {

        URL url = new URL(GIST_URL);

        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "token " + accessToken);
        connection.setDoOutput(true);

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        return JsonParser.parseReader(in).getAsJsonArray();
    }
    
}
