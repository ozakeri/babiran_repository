package net.babiran.app;



public interface GetCommentsCallBack {

    public void onGetCommentsSuccessAction(Comment[] comments);

    public void onGetCommentsErrorAction(String error);
}