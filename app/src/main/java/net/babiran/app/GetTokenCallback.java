package net.babiran.app;



public interface GetTokenCallback {

    public void onGetTokenSuccessAction(String token);

    public void onGetTokenErrorAction(String error);
}