package net.babiran.app;



public interface SendSmsCallback {

    public void onSendSmsSuccessAction();

    public void onSendSmsErrorAction(String error);
}