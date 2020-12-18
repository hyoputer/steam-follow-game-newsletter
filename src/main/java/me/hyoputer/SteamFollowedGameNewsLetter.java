package me.hyoputer;

import java.io.IOException;
import java.util.List;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.apache.commons.lang3.StringUtils;

/**
 * Hello world!
 *
 */
public class SteamFollowedGameNewsLetter {
    public static void main(String[] args) throws Exception {

        Steam steamClient = new Steam(System.getenv("STEAM_USER_ID"));
        Gist gistClient = new Gist(System.getenv("GIST_ACCESS_KEY"));

        //get AppIds by parsing html
        List<String> appIds = steamClient.getFollowedAppIds();

        String gistId = gistClient.getGistId(System.getenv("GIST_DESCRIPTION"));

        if (StringUtils.isNotBlank(gistId)) {

            JsonObject gistContent = gistClient.getGistContent(gistId);
            JsonObject newGistContent = new JsonObject();

            appIds.forEach(appId -> {
                try {

                    JsonObject appNews = steamClient.getAppNews(appId);
                    String newsIdLatest = appNews.get("appnews").getAsJsonObject().get("newsitems").getAsJsonArray()
                            .get(0).getAsJsonObject().get("gid").getAsString();

                    JsonElement newsIdInGist = gistContent.get(appId);

                    if (newsIdInGist == null || !(newsIdInGist.getAsString().equals(newsIdLatest))) {
                        System.out.println(appId + " not equal! " + newsIdLatest);
                        // Mail.sendMail(appId + " not equal! " + newsIdLatest, "12345");
                    }
                    
                    newGistContent.addProperty(appId, newsIdLatest);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}