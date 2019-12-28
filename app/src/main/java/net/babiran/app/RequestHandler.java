package net.babiran.app;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import Handlers.DatabaseHandler;
import tools.AppConfig;


/**
 * Created by Alireza on 1/20/2017.
 */

public class RequestHandler {

    public static String SubmitComment(final Context context, String p_id, String comment, float rate, final SubmitCommentCallBack commentCallBack) {
        String id = null;
        DatabaseHandler db;
        db = new DatabaseHandler(context);
        if (db.getRowCount() > 0) {
            HashMap<String, String> userDetailsHashMap = db.getUserDetails();
            id = userDetailsHashMap.get("id");
        }
        final String url = AppConfig.BASE_URL + "api/add_comment";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", id);
            jsonObject.put("product_id", String.valueOf(p_id));
            jsonObject.put("comment", comment);
            jsonObject.put("rate", String.valueOf(rate));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest customRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                commentCallBack.onSubmitCommentSuccessAction();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                commentCallBack.onSubmitCommentErrorAction(error.getMessage());
            }
        });
        AppController.getInstance().addToRequestQueue(customRequest, url);
        return url;
    }

    public static String getComments(final Context context, long productId, int page, final GetCommentsCallBack callBack) {
        final String url = AppConfig.BASE_URL + "api/product_comments/" + productId;
        Log.e("~!~!url", url);
        JsonArrayRequest customRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.e("response", response.toString());
                callBack.onGetCommentsSuccessAction(new Gson().fromJson(response.toString(), Comment[].class));
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error", error.getMessage());
                callBack.onGetCommentsErrorAction(error.getMessage());
            }
        });
        AppController.getInstance().addToRequestQueue(customRequest, url);
        return url;
    }

    public static String getCommonQuestions(final Context context, final GetCommonQuestionsCallBack callBack) {

        final String url = AppConfig.BASE_URL + "api/faq";

        Map<String, String> map = new HashMap<>();
        JsonArrayRequest customRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (response != null)
                    callBack.onGetCommonQuestionsSuccessAction(new Gson().fromJson(response.toString(), CommonQuestion[].class));
                else callBack.onGetCommonQuestionsErrorAction("");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callBack.onGetCommonQuestionsErrorAction(error.getMessage());
            }
        });
        AppController.getInstance().addToRequestQueue(customRequest, url);
        return url;
    }

    public static String getToken(final GetTokenCallback callback) {
        final String url = "http://ws.sms.ir/api/Token";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("UserApiKey", "6ad44d427979110c515e4d42");
            jsonObject.put("SecretKey", "it66)%#teBC!@*&");
            jsonObject.put("System", "opencart_2_3_v_1_1");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response == null || TextUtils.isEmpty(response.toString())) {
                    callback.onGetTokenErrorAction("invalid response.");
                } else {
                    GetTokenResponse getTokenResponse = new Gson().fromJson(response.toString(), GetTokenResponse.class);
                    if (getTokenResponse.isSuccessful) {
                        callback.onGetTokenSuccessAction(getTokenResponse.tokenKey);
                    } else {
                        callback.onGetTokenErrorAction(getTokenResponse.message);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onGetTokenErrorAction(error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
//                headers.put("Content-Type", "application/json");
                return headers;
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

        };
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, url);
        System.out.println("url====" + url);
        return url;
    }

    public static String sendSMS(final String token, String code, String mobile, final SendSmsCallback callback) {
        final String url = "http://ws.sms.ir/api/MessageSend";
        JSONObject jsonObject = new JSONObject();
        try {
            JSONArray jsonArrayM = new JSONArray();
            String s = "بابیران" + "\n" + "کد فعالسازی:" + "\n";
            jsonArrayM.put(s + code);
            jsonObject.put("Messages", jsonArrayM);
            JSONArray jsonArray = new JSONArray();
            jsonArray.put(mobile);
            jsonObject.put("MobileNumbers", jsonArray);
            jsonObject.put("LineNumber", "30005364900900");
            jsonObject.put("CanContinueInCaseOfError", false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response == null || TextUtils.isEmpty(response.toString())) {
                    callback.onSendSmsErrorAction("invalid response.");
                } else {
                    GetTokenResponse getTokenResponse = new Gson().fromJson(response.toString(), GetTokenResponse.class);
                    if (getTokenResponse.isSuccessful) {
                        callback.onSendSmsSuccessAction();

                    } else {
                        callback.onSendSmsErrorAction(getTokenResponse.message);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onSendSmsErrorAction(error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("x-sms-ir-secure-token", token);
                return headers;
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

        };
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, url);
        return url;
    }

    public static String sendSMS2(final String token, String code, String mobile, final SendSmsCallback callback) {
        final String url = "http://RestfulSms.com/api/UltraFastSend";


        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject2 = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try {
            jsonObject.put("Parameter", "VerificationCode");
            jsonObject.put("ParameterValue", code);


            jsonArray.put(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            jsonObject2.put("ParameterArray", jsonArray);
            jsonObject2.put("TemplateId", "1233 ");

            jsonObject2.put("Mobile", mobile);


            //
            Log.e("STR", jsonObject2.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

    /*    JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Code", code);
            jsonObject.put("MobileNumber", mobile);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        */


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject2, new Response.Listener<JSONObject>() {


            //  JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                if (response == null || TextUtils.isEmpty(response.toString())) {
                    callback.onSendSmsErrorAction("invalid response.");
                } else {
                    SendSms2Response getTokenResponse = new Gson().fromJson(response.toString(), SendSms2Response.class);
                    if (getTokenResponse.isSuccessful) {
                        callback.onSendSmsSuccessAction();

                    } else {
                        callback.onSendSmsErrorAction(getTokenResponse.message);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onSendSmsErrorAction(error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("x-sms-ir-secure-token", token);
                return headers;
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

        };
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, url);

        System.out.println("jsonObjectRequest====" + jsonObjectRequest);
        System.out.println("jsonObjectRequest====" + url);
        return url;
    }
}