package me.hyoputer;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;

/**
 * Hello world!
 *
 */
public class SteamFollowedGameNewsLetter {
    public static void main(String[] args) throws Exception {

        Steam steamClient = new Steam(System.getenv("STEAM_USER_ID"));
        Gist gistClient = new Gist(System.getenv("GIST_ACCESS_KEY"));

        //get AppIds by parsing html
        List<String> appIds = Jsoup.connect(steamClient.getFollowedURL()).get().select(".gameListRowItemName > a")
                .eachAttr("href").stream().map(str -> Pattern.compile("\\D*").matcher(str).replaceAll(""))
                .collect(Collectors.toList());

        JsonArray gists = gistClient.getGists();
        StringBuilder gistId = new StringBuilder();

        //get gistId that contains followed games' newsIds
        gists.forEach(json -> {
            if (json.getAsJsonObject().get("description").getAsString().equals(System.getenv("GIST_DESCRIPTION"))) {
                gistId.append(json.getAsJsonObject().get("id").getAsString());
            }
        });

        if (StringUtils.isNotBlank(gistId)) {
            JsonObject gistContent = JsonParser
                    .parseString(gistClient.getGist(gistId.toString()).get("files").getAsJsonObject()
                            .get(System.getenv("GIST_FILE_NAME")).getAsJsonObject().get("content").getAsString())
                    .getAsJsonObject();

            JsonObject newGistContent = new JsonObject();

            appIds.forEach(appId -> {
                try {
                    JsonObject appNews = steamClient.getAppNews(appId);

                    JsonElement newsIdInGist = gistContent.get(appId);

                    String newsIdLatest = appNews.get("appnews").getAsJsonObject().get("newsitems").getAsJsonArray()
                            .get(0).getAsJsonObject().get("gid").getAsString();

                    if (newsIdInGist == null || !(newsIdInGist.getAsString().equals(newsIdLatest))) {
                        // System.out.println(appId + " not equal! " + newsIdLatest);
                    }
                    
                    newGistContent.addProperty(appId, newsIdLatest);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}