package net.babiran.app.Rss;

/**
 * Created by D on 2/12/2018.
 */

public class RssFeedModel
{
    public String title;
    public String link;
    public String description;
    public String img_url;

    public RssFeedModel(String title, String link, String description) {
        this.title = title;
        this.link = link;
        this.description = description;
    }

    public RssFeedModel(String title, String link, String description, String img_url) {
        this.title = title;
        this.link = link;
        this.description = description;
        this.img_url = img_url;
    }
}
