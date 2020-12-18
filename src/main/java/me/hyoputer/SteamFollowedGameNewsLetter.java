package me.hyoputer;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.jsoup.Jsoup;

/**
 * Hello world!
 *
 */
public class SteamFollowedGameNewsLetter {
    public static void main(String[] args) throws Exception {

        Steam steamClient = new Steam(System.getenv("STEAM_USER_ID"));
        Gist gistClient = new Gist(System.getenv("GIST_ACCESS_KEY"));

        List<String> gameIDs = Jsoup.connect(steamClient.getFollowedURL()).get().select(".gameListRowItemName > a")
                .eachAttr("href").stream().map(str -> Pattern.compile("\\D*").matcher(str).replaceAll(""))
                .collect(Collectors.toList());

        JsonArray gists = gistClient.getGists();
        StringBuilder gistId = new StringBuilder();
        gists.forEach(json -> {
            if (json.getAsJsonObject().get("description").getAsString().equals(System.getenv("GIST_DESCRIPTION"))) {
                gistId.append(json.getAsJsonObject().get("id").getAsString());
            }
        });

        JsonObject gistContent = JsonParser.parseString(gistClient.getGist(gistId.toString()).get("files").getAsJsonObject()
                                            .get(System.getenv("GIST_FILE_NAME")).getAsJsonObject().get("content").getAsString())
                                            .getAsJsonObject();
        

    }
}