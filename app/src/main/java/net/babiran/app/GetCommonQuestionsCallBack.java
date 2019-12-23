package net.babiran.app;


public interface GetCommonQuestionsCallBack {

    public void onGetCommonQuestionsSuccessAction(CommonQuestion[] commonQuestions);

    public void onGetCommonQuestionsErrorAction(String error);
}