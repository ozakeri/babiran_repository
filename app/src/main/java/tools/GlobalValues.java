package tools;

import net.babiran.app.Servic.GETINGBlog;

import java.util.List;

import retrofit2.Response;

public class GlobalValues {

    private retrofit2.Response<List<GETINGBlog>> response;

    public Response<List<GETINGBlog>> getResponse() {
        return response;
    }

    public void setResponse(Response<List<GETINGBlog>> response) {
        this.response = response;
    }
}
