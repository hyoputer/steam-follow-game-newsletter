package me.hyoputer;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonObject;

public class GistContentCreator {
    private Map<String, Map<String, String>> files = new HashMap<>();

    private String description = Configs.GIST_DESCRIPTION;

    public GistContentCreator(JsonObject content) {
        Map<String, String> file = new HashMap<>();
        file.put("content", content.toString());
        files.put(Configs.GIST_FILE_NAME, file);
    }
}
