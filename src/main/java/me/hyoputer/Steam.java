package me.hyoputer;

public class Steam {
    private static String URL_FORMAT = "https://steamcommunity.com/profiles/%s/followedgames/";

    private String userId;

    public Steam(String userId) {
        this.userId = userId;
    }

    public String getFollowedURL() {
        return String.format(URL_FORMAT, userId);
    }
}
