package me.hyoputer;

import java.io.IOException;
import java.util.List;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Hello world!
 *
 */
public class SteamFollowedGameNewsLetter {
    public static void main(String[] args) throws Exception {

        Steam steamClient = new Steam(Configs.STEAM_USER_ID);
        GithubClient githubClient = new GithubClient(Configs.GIST_ACCESS_KEY);
        Mail mailClient = new Mail(Configs.EMAIL_ID, Configs.EMAIL_PASSWORD);

        //get AppIds by parsing html
        List<String> appIds = steamClient.getFollowedAppIds();

        String gistId = githubClient.getGistId(Configs.GIST_DESCRIPTION);

        if (gistId != null) {

            JsonObject gistContent = githubClient.getGistContent(gistId, Configs.GIST_FILE_NAME);
            JsonObject newGistContent = new JsonObject();

            appIds.forEach(appId -> {
                try {

                    JsonObject appNews = steamClient.getAppNews(appId);
                    String newsIdLatest = appNews.get("gid").getAsString();

                    JsonElement newsIdInGist = gistContent.get(appId);

                    if (newsIdInGist == null || !(newsIdInGist.getAsString().equals(newsIdLatest))) {
                        // System.out.println(appNews.get("title").getAsString() + " not equal! " + appNews.get("url")
                        //         .getAsString());
                        mailClient.sendMail(appNews.get("title").getAsString(), appNews.get("url").getAsString());
                    }
                    
                    newGistContent.addProperty(appId, newsIdLatest);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            
            if (!gistContent.equals(newGistContent))
                githubClient.updateGist(gistId, Configs.GIST_FILE_NAME, newGistContent);
        }
    }
}