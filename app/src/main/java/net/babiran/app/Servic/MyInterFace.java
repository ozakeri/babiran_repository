package net.babiran.app.Servic;


import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by amin on 3/4/2017.
 */
public interface MyInterFace {

    @FormUrlEncoded
    @POST("chargPardakht")
    Call<MyMesa> BuySahrj(@Field("mobile") String mobile, @Field("price") String price, @Field("is_direct") String is_direct, @Field("operator_id") String operator_id);

    @FormUrlEncoded
    @POST("creditPardakht")
    Call<MyMesa> BuyCredit(@Field("user_id") int user_id, @Field("price") String price);


    @GET("factor/chargeFactorList/{id}/")
    Call<List<MyModelQu>> HistorySharj(@Path("id") int id);

    @GET("categoryOfBlog/getCategories/")
    Call<List<GETING>> getCategories();

    @GET("blog/getBlogs/")
    Call<List<GETINGBlog>> getBlogs();

    @GET("blog/getBlogs/{id}/")
    Call<List<GETINGBlog>> getBlogById(@Path("id") int id);

    @GET("blog/getBlogCat/{id}/")
    Call<List<GETINGBlog>> getBlogCatById(@Path("id") int id);

    @FormUrlEncoded
    @POST("blog/setLike")
    Call<GetSucc> setLike(@Field("blog_id") int blog_id, @Field("user_id") int user_id, @Field("like") int like);

    @FormUrlEncoded
    @POST("blog/checkLike")
    Call<GetSucc> checkLike(@Field("blog_id") int blog_id, @Field("user_id") int user_id);

    @FormUrlEncoded
    @POST("blog/add_comment")
    Call<GetSucc> add_comment(@Field("blog_id") int blog_id, @Field("user_id") int user_id, @Field("comment") String comment);

    @GET("blog/blog_comments/{blogid}/")
    Call<List<GetComm>> getcomments(@Path("blogid") int id);

    @FormUrlEncoded
    @POST("comment_vote")
    Call<GetSucc> setVote(@Field("comment_id") int blog_id, @Field("user_id") int user_id, @Field("vote") String like);
}