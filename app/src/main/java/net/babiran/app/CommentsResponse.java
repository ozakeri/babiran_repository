package net.babiran.app;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Alireza on 1/24/2017.
 */

public class CommentsResponse extends Response {

    @SerializedName("comments")
    Comment[] comments;

    public Comment[] getComments() {
        return comments;
    }

    public void setComments(Comment[] comments) {
        this.comments = comments;
    }
}
