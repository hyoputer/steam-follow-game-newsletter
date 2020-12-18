package me.hyoputer;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

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

        InputStreamReader in = new InputStreamReader(connection.getInputStream());

        return JsonParser.parseReader(in).getAsJsonArray();
    }

    public JsonObject getGist(String id) throws IOException {

        URL url = new URL(GIST_URL + "/" + id);

        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "token " + accessToken);
        connection.setRequestProperty("Accept", "application/vnd.github.v3+json");
        connection.setDoOutput(true);

        InputStreamReader in = new InputStreamReader(connection.getInputStream());

        return JsonParser.parseReader(in).getAsJsonObject();
    }

    public String getGistId(String description) throws IOException {

        JsonArray gists = getGists();

        StringBuilder gistId = new StringBuilder();

        // get gistId that contains followed games' newsIds
        gists.forEach(json -> {
            if (json.getAsJsonObject().get("description").getAsString().equals(System.getenv("GIST_DESCRIPTION"))) {
                gistId.append(json.getAsJsonObject().get("id").getAsString());
            }
        });

        return gistId.toString();
    }

    public JsonObject getGistContent(String id) throws JsonSyntaxException, IOException {
        return JsonParser
                .parseString(getGist(id.toString()).get("files").getAsJsonObject()
                        .get(System.getenv("GIST_FILE_NAME")).getAsJsonObject().get("content").getAsString())
                .getAsJsonObject();
    }
    
}
