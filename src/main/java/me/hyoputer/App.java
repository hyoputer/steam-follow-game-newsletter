package me.hyoputer;

import java.io.IOException;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) throws IOException {
        Jsoup.connect(Configs.STEAM_FOLLOWED_URL).get().select(".gameListRowItemName > a").eachAttr("href")
                .stream().map(str -> Pattern.compile("\\D*").matcher(str).replaceAll("")).forEach(System.out::println);
    }
}
