package me.hyoputer;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.net.ssl.HttpsURLConnection;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.jsoup.Jsoup;

public class Steam {

    private static final String FOLLOWED_GAMES_URL_FORMAT = "https://steamcommunity.com/profiles/%s/followedgames/";

    private static final String APP_NEWS_URL_FORMAT = "https://api.steampowered.com/ISteamNews/GetNewsForApp/v2/?appid=%s&count=1&maxlength=100&format=json";

    private static final String FOLLOWED_GAME_ITEM_SELECTOR = ".gameListRowItemName > a";

    private String followedGameUrl;

    public Steam(String userId) {
        this.followedGameUrl = String.format(FOLLOWED_GAMES_URL_FORMAT, userId);
    }

    public List<String> getFollowedAppIds() throws IOException {
        return Jsoup.connect(followedGameUrl).get().select(Steam.FOLLOWED_GAME_ITEM_SELECTOR)
                .eachAttr("href").stream().map(str -> Pattern.compile("\\D*").matcher(str).replaceAll(""))
                .collect(Collectors.toList());
    }

    public JsonObject getAppNews(String appId) throws IOException {

        URL url = new URL(String.format(APP_NEWS_URL_FORMAT, appId));

        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        
        InputStreamReader in = new InputStreamReader(connection.getInputStream());

        return JsonParser.parseReader(in).getAsJsonObject();
    }
}
