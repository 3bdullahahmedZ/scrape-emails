import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    static int count =0;
    public static void main(String[] args) throws IOException {

        System.out.println("Hello world!");
        String url = "https://cfy.ksu.edu.sa/en/node/1146";

        crawl(1,url, new ArrayList<String>());
        System.out.println(count);
    }

    private static void crawl(int i, String url, ArrayList<String> visited) throws IOException {
        if(i<=5) {
            Document doc = request(url, visited);
            if (doc != null) {
                String regex = "(.+)@(.)ksu.edu.sa";
                for (Element link : doc.select("a[href]")) {
                    String next_link = link.absUrl("href");
                    if (visited.contains(next_link) == false)
                        crawl(++i, next_link, visited);
                }
            }
        }
    }

    private static Document request(String url, ArrayList<String> visited) throws IOException {
        Connection connection = Jsoup.connect(url);
        Document doc = connection.get();
        if(connection.response().statusCode() ==200){
            Pattern p = Pattern.compile("[_A-Za-z0-9-]+(\\\\.[_A-Za-z0-9-]+)*@(.*)+[ksu.edu.sa]");
//            System.out.println(doc.title());
            Matcher matcher = p.matcher(doc.body().html());
            while (matcher.find()) {
                System.out.println(matcher.group());
                count++;
            }
            visited.add(url);
            return doc;
        }
        return null;

    }
}