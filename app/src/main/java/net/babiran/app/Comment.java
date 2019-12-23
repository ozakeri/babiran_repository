package net.babiran.app;

import java.io.Serializable;

/**
 * Created by Alireza on 1/20/2017.
 */
public class Comment implements Serializable {

//    id => آی نظر
//    author = > نویسنده
//    content => متن پیام
//    date = > تاریخ ایجاد

    public int id;
    public int commentable_id;
    public String commentable_type;
    public int commented_id;
    public String commented_type;
    public String comment;
    public boolean approved;
    public float rate;
    public String created_at;
    public String updated_at;
}
