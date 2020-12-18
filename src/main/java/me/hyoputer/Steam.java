package me.hyoputer;

public class Steam {
    private static String FOLLOWED_GAMES_URL_FORMAT = "https://steamcommunity.com/profiles/%s/followedgames/";

    private String userId;

    public Steam(String userId) {
        this.userId = userId;
    }

    public String getFollowedURL() {
        return String.format(FOLLOWED_GAMES_URL_FORMAT, userId);
    }
}
