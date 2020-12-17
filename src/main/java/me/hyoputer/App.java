package me.hyoputer;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) throws Exception {

        Steam steam = new Steam(System.getenv("STEAM_USER_ID"));
        Gist gist = new Gist(System.getenv("GIST_ACCESS_KEY"));

        List<String> gameIDs = Jsoup.connect(steam.getFollowedURL()).get().select(".gameListRowItemName > a").eachAttr("href")
                .stream().map(str -> Pattern.compile("\\D*").matcher(str).replaceAll("")).collect(Collectors.toList());

        System.out.println(gist.getGistId(System.getenv("GIST_DESCRIPTION")));
        
    }
}
