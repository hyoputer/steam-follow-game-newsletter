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

        Steam steamClient = new Steam(Configs.STEAM_USER_ID);
        Gist gistClient = new Gist(Configs.GIST_ACCESS_KEY);
        Mail mailClient = new Mail(Configs.EMAIL_ID, Configs.EMAIL_PASSWORD);

        //get AppIds by parsing html
        List<String> appIds = steamClient.getFollowedAppIds();

        String gistId = gistClient.getGistId(Configs.GIST_DESCRIPTION);

        if (StringUtils.isNotBlank(gistId)) {

            JsonObject gistContent = gistClient.getGistContent(gistId);
            JsonObject newGistContent = new JsonObject();

            appIds.forEach(appId -> {
                try {

                    JsonObject appNews = steamClient.getAppNews(appId);
                    String newsIdLatest = appNews.get("gid").getAsString();

                    JsonElement newsIdInGist = gistContent.get(appId);

                    if (newsIdInGist == null || !(newsIdInGist.getAsString().equals(newsIdLatest))) {
                        System.out.println(appNews.get("title").getAsString() + " not equal! " + appNews.get("url")
                                .getAsString());
                        mailClient.sendMail(appNews.get("title").getAsString(), appNews.get("url").getAsString());

                    }
                    
                    newGistContent.addProperty(appId, newsIdLatest);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}