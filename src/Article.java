/**
 * Created by evansdb0 on 5/25/16.
 */
public class Article {

    /*
    The title of the article extracted from the webpage
     */
    private String title;

    /*
    The url of the img extracted from the article webpage
     */
    private String imgUrl;

    /*
    The link to the site on the webpage where the article was extracted
     */
    private String sourceLink;

    /*
    Link of the category the source link was found under
     */
    private String sourceCategoryLink;
    /*
    The actual text extracted from body of the article
     */
    private String articleBody;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getSourceLink() {
        return sourceLink;
    }

    public void setSourceLink(String sourceLink) {
        this.sourceLink = sourceLink;
    }

    public String getSourceCategoryLink() {
        return sourceCategoryLink;
    }

    public void setSourceCategoryLink(String sourceCategoryLink) {
        this.sourceCategoryLink = sourceCategoryLink;
    }

    public String getArticleBody() {
        return articleBody;
    }

    public void setArticleBody(String articleBody) {
        this.articleBody = articleBody;
    }
}
