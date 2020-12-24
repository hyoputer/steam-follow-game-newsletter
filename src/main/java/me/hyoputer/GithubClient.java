package me.hyoputer;

import java.io.IOException;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import org.kohsuke.github.GHGist;
import org.kohsuke.github.GitHub;

public class GithubClient {

    private GitHub gitHub;

    public GithubClient(String accessToken) throws IOException {
        gitHub = GitHub.connectUsingOAuth(accessToken);
    }

    public String getGistId(String description) throws IOException {

        GHGist[] gists = gitHub.getMyself().listGists().toArray();

        for (GHGist gist : gists) {
            if (gist.getDescription().equals(description)) {
                return gist.getGistId();
            }
        }

        return null;
    }

    public JsonObject getGistContent(String id, String fileName) throws JsonSyntaxException, IOException {

        GHGist targetGist = gitHub.getGist(id);
        return JsonParser.parseString(targetGist.getFile(fileName).getContent()).getAsJsonObject();
    }

    public void updateGist(String id, String fileName, JsonObject content) throws IOException {
        
        gitHub.getGist(id).update().updateFile(fileName, content.toString()).update();
    }
    
}
