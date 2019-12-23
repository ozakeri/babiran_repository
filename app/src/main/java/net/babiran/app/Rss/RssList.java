package net.babiran.app.Rss;

/**
 * Created by D on 2/14/2018.
 */

public class RssList
{
    String Title;
    String Link;

    public RssList(String title, String link)
    {
        Title = title;
        Link = link;
    }

    public String getTitle()
    {
        return Title;
    }

    public String getLink()
    {
        return Link;
    }
}
