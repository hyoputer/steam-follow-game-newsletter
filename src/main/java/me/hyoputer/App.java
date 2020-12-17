package me.hyoputer;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.google.gson.JsonArray;

import org.jsoup.Jsoup;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) throws Exception {

        Steam steam = new Steam(System.getenv("STEAM_USER_ID"));
        Gist gist = new Gist(System.getenv("GIST_ACCESS_KEY"));

        JsonArray gists = gist.getGists();
        StringBuilder gistId = new StringBuilder();
        gists.forEach(json -> {
            if (json.getAsJsonObject().get("description").getAsString().equals(System.getenv("GIST_DESCRIPTION"))) {
                gistId.append(json.getAsJsonObject().get("id").getAsString());
            }
        });
        
        List<String> gameIDs = Jsoup.connect(steam.getFollowedURL()).get().select(".gameListRowItemName > a").eachAttr("href")
                .stream().map(str -> Pattern.compile("\\D*").matcher(str).replaceAll("")).collect(Collectors.toList());

    }
}