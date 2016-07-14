//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class CnnScrape {
    private String domain = null;

    public static void main(String[] args) throws IOException {
        CnnScrape cnn = new CnnScrape("cnn.com");
        ArrayList hrefs = new ArrayList(100);
        Document doc = Jsoup.connect("http://cnn.com").get();
        Element navExpanded = doc.getElementById("nav-expanded-menu");
        Elements links = navExpanded.children().select("a[href]");

        for (Element articles : links) {
            String absHref = articles.attr("href");
            absHref = absHref.charAt(0) == 47 ? doc.location() + absHref.substring(1) : absHref;
            hrefs.add(absHref);
        }

        for(int i = 1; i < hrefs.size(); ++i) {
            try {
                doc = Jsoup.connect((String)hrefs.get(i)).get();
                Elements articles = doc.getElementsByTag("a[href]");

                for (Element article : articles) {
                    Element a = article.getElementsByTag("a").first();
                    String articleLink = null;

                    try {
                        articleLink = a.attr("href");
                    } catch (NullPointerException var17) {
                        continue;
                    }

                    doc = Jsoup.connect(cnn.getDomain() + articleLink).timeout(0).get();
                    Elements bodyParagraphs = doc.getElementsByClass("zn-body__paragraph");
                    String p = bodyParagraphs.text();
                    if (p.substring(0, (int) Math.floor(0.25D * (double) p.length())).contains("(CNN)")) {
                        System.out.println((String) hrefs.get(i));
                        System.out.println(cnn.getDomain() + articleLink);
                        Element image = doc.getElementsByAttributeValue("itemprop", "image").first();
                        String imageSrc = image.attr("content");
                        System.out.println(imageSrc);
                        String articleTitle = doc.getElementsByClass("pg-headline").text();
                        if (articleTitle.length() != 0) {
                            System.out.println(articleTitle);
                            System.out.println(p);
                            System.out.println();
                        }
                    }
                }
            } catch (Exception var18) {
                ;
            }

            System.out.println("\n-----------------------------------------------------------------------------------------------------------------------\n");
        }

    }

    public CnnScrape(String domain) {
        this.domain = this.configDomain(domain);
    }

    private String configDomain(String domain) {
        String http = "http://";
        return !domain.substring(0, http.length()).equals(http)?http + domain:domain;
    }

    public void setDomain(String domain) {
        this.domain = this.configDomain(domain);
    }

    public String getDomain() {
        return this.domain;
    }
}
