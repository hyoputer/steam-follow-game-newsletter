package me.hyoputer;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

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

        gists.forEach(json -> {
            if (json.getAsJsonObject().get("description").getAsString().equals(System.getenv("GIST_DESCRIPTION"))) {
                gistId.append(json.getAsJsonObject().get("id").getAsString());
            }
        });

        return gistId.toString();
    }

    public JsonObject getGistContent(String id) throws JsonSyntaxException, IOException {
        return JsonParser.parseString(getGist(id).get("files").getAsJsonObject()
                .get(System.getenv("GIST_FILE_NAME")).getAsJsonObject().get("content").getAsString()).getAsJsonObject();
    }

    public void updateGist(String id, JsonObject content)
            throws URISyntaxException, ClientProtocolException, IOException {
        
        GistContentCreator creator = new GistContentCreator(content);
        // System.out.println(new Gson().toJson(creator));
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPatch httpPatch = new HttpPatch(new URI(GIST_URL + "/" + id));
        httpPatch.addHeader("Authorization", "token " + accessToken);
        httpPatch.addHeader("Accept", "application/vnd.github.v3+json");
        httpPatch.setEntity(new StringEntity(new Gson().toJson(creator)));
        CloseableHttpResponse response = httpClient.execute(httpPatch);

        if (response.getStatusLine().getStatusCode() != 200) {
            //TODO failure management
            System.out.println(response);
        }
    }
    
}
