package tools;

import net.babiran.app.Servic.GETINGBlog;

import java.util.List;

import retrofit2.Response;

public class GlobalValues {

    private static String firstId;
    private static String secondId;

    public String getFirstId() {
        return firstId;
    }

    public void setFirstId(String firstId) {
        this.firstId = firstId;
    }

    public String getSecondId() {
        return secondId;
    }

    public void setSecondId(String secondId) {
        this.secondId = secondId;
    }

    private retrofit2.Response<List<GETINGBlog>> response;

    public Response<List<GETINGBlog>> getResponse() {
        return response;
    }

    public void setResponse(Response<List<GETINGBlog>> response) {
        this.response = response;
    }
}
