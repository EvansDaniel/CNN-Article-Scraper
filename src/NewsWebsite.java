import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by evansdb0 on 5/25/16.
 */
public class NewsWebsite {

    /*
    The domain of the website to scrape. The functionality provided by this
    class is likely to only work on news websites, blogs, or other article sites
    */
    private String domain;

    /*
    The selector that uniquely identifies the container
    for the categories of the articles
    ex: politics, life, tech
    -> nav-expanded-menu <-
    */
    private String articleCategoryContainerSelector;

    /*
    Defines the tag/id that contains at least the title and the body of the
    article
    It is used to get all the links to the article pages in an article list,
    which is the page pointed to by the category article links
    -> article <-
    */
    private String mainArticleSelector;

    /*
    The selector that will be used to extract the body text of the article
    */
    private String bodyParagraphSelector;

    /*
    The unique attribute of the container containing the image for the article
    */
    private String imageContainerAttr;

    /*
    The unique attribute value of the container containing the image for the article
    */
    private String imageContainerVal;

    /*
    Another way to uniquely identify the imageContainer
    */
    private String imageContainerSelector;

    /*
    The url that points to the image for the article
    */
    private String imageSrcAttr;

    /*
    Used to validate that the article found is in fact an article
    Should be set to a string that identifies the article returned
    as your definition of an article
    For example, on the CNN website, all of its articles start with (CNN). That
    would be the article validator for that website
     */
    private String articleValidator;

    /*
    Unique selector for the article title
    */
    private String articleTitleSelector;

    /*
    Container for the articles extracted from the website pointed to by domain
    */
    private ArrayList<Article> articleContainer = new ArrayList<>(500);

    /**
     * neccessary setters to set up the tags and selections to be made on
     * the website. These are the things that narrow down the search for
     * @param domain
     */
    public void setDomain(String domain) {
        this.domain = domain;
    }

    public void setArticleCategoryContainerSelector(String articleCategoryContainerSelector) {
        this.articleCategoryContainerSelector = articleCategoryContainerSelector;
    }

    public void setMainArticleSelector(String mainArticleSelector) {
        this.mainArticleSelector = mainArticleSelector;
    }

    public void setBodyParagraphSelector(String bodyParagraphSelector) {
        this.bodyParagraphSelector = bodyParagraphSelector;
    }

    public void setImageContainerAttr(String imageContainerAttr) {
        this.imageContainerAttr = imageContainerAttr;
    }

    public void setImageContainerVal(String imageContainerVal) {
        this.imageContainerVal = imageContainerVal;
    }

    public void setImageContainerSelector(String imageContainerSelector) {
        this.imageContainerSelector = imageContainerSelector;
    }

    public void setImageSrcAttr(String imageSrcAttr) {
        this.imageSrcAttr = imageSrcAttr;
    }

    public void setArticleValidator(String articleValidator) {
        this.articleValidator = articleValidator;
    }

    public void setArticleTitleSelector(String articleTitleSelector) {
        this.articleTitleSelector = articleTitleSelector;
    }

    public NewsWebsite() {
        domain = "http://cnn.com";
        articleCategoryContainerSelector = "nav-expanded-menu";
        mainArticleSelector = "article";
        bodyParagraphSelector = "zn-body__paragraph";
        imageContainerAttr = "itemprop";
        imageContainerVal = "image";
        articleTitleSelector = "pg-headline";
        imageSrcAttr = "content";
        articleValidator = "(CNN)";
    }
    private ArrayList<String> getLinksFromArticleCategoryContainer(Element linkContainer) {
        // get the the links in the articleCategoryContainerSelector
        Elements links = linkContainer.children().select("a[href]");
        return configLinks(links);
    }
    // TODO: set this to private
    // TODO: algorithm is dependent on the site using relative links linked from the domain
    public ArrayList<Article> fetchArticlesFromSite() {
        Element navExpanded = null;
        try {
            navExpanded = Jsoup.connect(domain).get()
                    .getElementById(articleCategoryContainerSelector);
        } catch (Exception e) {
            System.out.println("The url given is not working or the article selector does not work");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        // get links of articleCategoryLinkContainer - returns absolute urls because of configLinks(Elements)
        ArrayList<String> hrefs = getLinksFromArticleCategoryContainer(navExpanded);

        // iterate through the articleCatContainerSelector's returned links
        for (int i = 0; i < hrefs.size(); i++) {
            try {
                // get a parent that contains the link to the article page
                // the default tag for this is <article>
                // hrefs.get(i) = link to articleCategory page
                Elements articles = Jsoup.connect(hrefs.get(i))
                                                    .get().getElementsByTag(mainArticleSelector);

                // iterate through the returned article page links
                for (Element article : articles) {
                    // get the first link contained in the parent <article> tag
                    Element a = article.getElementsByTag("a").first();

                    String articleLink = null;
                    try {
                        // get the link from that first <a> tag
                        articleLink = a.attr("href");
                    } catch (NullPointerException var17) {
                        continue;
                    }
                    // store the article page in memory
                    Document doc = Jsoup.connect(this.getDomain() + articleLink).timeout(0).get();
                    // get the body paragraph of the article
                    String bodyParagraph = fetchArticleFromPage(bodyParagraphSelector,doc);

                    // if first 25 percent of the bodyParagraph contains articleValidator
                    if(isValidArticle(this.articleValidator,bodyParagraph,.25)) {
                        // cnn.com
                        System.out.println(hrefs.get(i));
                        // cnn.com + whatever link
                        System.out.println(this.getDomain() + articleLink);
                        // link to the image
                        String imgUrl = fetchArticleImage(imageContainerAttr,imageContainerVal,imageSrcAttr,doc);
                        System.out.println(imgUrl);
                        // get Article title
                        String articleTitle = fetchArticleTitle(doc,articleTitleSelector);
                        if(articleTitle.length() != 0) {
                            // article title
                            System.out.println(articleTitle);
                            // article's entire body
                            System.out.println(bodyParagraph);
                            System.out.println();
                            Article newsArticle = new Article();
                            newsArticle.setArticleBody(bodyParagraph);
                            newsArticle.setImgUrl(imgUrl);
                            // TODO: This will need to be its own table in the database - the setSourceCategoryLink(String)
                            newsArticle.setSourceCategoryLink(hrefs.get(i));
                            newsArticle.setSourceLink(this.domain + articleLink);
                            newsArticle.setTitle(articleTitle);
                            articleContainer.add(newsArticle);
                            // thread sleep so you can read the article's
                            // header and link
                            Thread.sleep(7500);
                        }
                    }
                }
            } catch(Exception e) {}
        }
        // TODO: method will take forever to return - need to add a method that publishes the current results
        return articleContainer;
    }
    /*
    requires articleTitleSelector to be a class in the html tag
     */
    public String fetchArticleTitle(Document doc, String articleTitleSelector) {
        return doc.getElementsByClass(articleTitleSelector).text();
    }
    public String fetchArticleImage(String imgAttr, String imgAttrVal, String imgSrcIdentifier, Document doc) {
        Element image = doc.getElementsByAttributeValue(imgAttr, imgAttrVal).first();
        return image.attr(imgSrcIdentifier);
    }
    /*
    @var zeroToOne refers to the percentage of the article the program should look through
     to find the specified articleValidator
     */
    public boolean isValidArticle(String articleValidator, String p, double zeroToOne) {
        return p.substring(0, (int) Math.floor(zeroToOne * (double) p.length())).contains(articleValidator);
    }
    private String fetchArticleFromPage(String bodyParagraphIdentifier, Document doc) throws IOException {
        return doc.getElementsByClass(bodyParagraphIdentifier).text();
    }
    private ArrayList<String> configLinks(Elements links) {
        ArrayList<String> hrefs = new ArrayList<>(100);
        for (Element articleCategoryLinks : links) {
            String absHref = articleCategoryLinks.attr("href");
            /* if not a absolute link, make it so */
            absHref = absHref.charAt(0) == 47 ? domain + absHref : absHref;
            hrefs.add(absHref);
        }
        return hrefs;
    }

    public NewsWebsite(String domain) {

        this.domain = this.configDomain(domain);
    }

    private String configDomain(String domain) {
        String http = "http://";
        return !domain.substring(0, http.length()).equals(http) ? http + domain : domain;
    }
    public String getDomain() {
        return this.domain;
    }

}
