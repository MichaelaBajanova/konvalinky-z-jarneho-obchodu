package roihunter.task.utils;

/**
 * @author Michaela Bajanova (469166)
 */
public class UrlParser {

    public static String getIdFromUrl(String url) {
        String[] parsedUrl = url.split("/");
        return parsedUrl[4];
    }
}
