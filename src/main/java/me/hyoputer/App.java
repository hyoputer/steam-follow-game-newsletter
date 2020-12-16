package me.hyoputer;

import java.io.IOException;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) throws IOException {

        SteamApi steamApi = new SteamApi(System.getenv("STEAM_USER_ID"));

        System.out.println(Jsoup.connect(steamApi.getFollowedURL()).get().select(".gameListRowItemName > a").eachAttr("href")
                .stream().map(str -> Pattern.compile("\\D*").matcher(str).replaceAll("")).collect(Collectors.toList()));
    }
}
