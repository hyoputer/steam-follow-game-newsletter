package me.hyoputer;

public class Configs {
    private static String URL_FORMAT = "https://steamcommunity.com/profiles/%s/followedgames/";

    public static String STEAM_FOLLOWED_URL = String.format(URL_FORMAT, System.getenv("STEAM_USER_ID"));
}
