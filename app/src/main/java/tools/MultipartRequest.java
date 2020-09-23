package tools;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.util.CharsetUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class MultipartRequest extends Request<String> {
    MultipartEntityBuilder entity = MultipartEntityBuilder.create();
    HttpEntity httpentity;
    private final Response.Listener<String> mListener;
    private final HashMap<String, File> mFileParts;

    public MultipartRequest(String url, Response.ErrorListener errorListener,
                            Response.Listener<String> listener, HashMap<String, File> files
                            ) {
        super(Method.POST, url, errorListener);
        this.mListener = listener;
        this.mFileParts = files;

        entity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        try {
            entity.setCharset(CharsetUtils.get("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        buildMultipartEntity();
        httpentity = entity.build();
    }
    private void buildMultipartEntity() {
        Log.d("mFileparts",mFileParts+"");
        if (mFileParts != null) {
            for (Map.Entry<String, File> entry : mFileParts.entrySet()) {
                entity.addPart(entry.getKey(), new FileBody(entry.getValue(), ContentType.create("audio/mp4"), entry.getValue().getName()));
            }
        }
        //entity.addPart(FILE_PART_NAME, new FileBody(mFilePart, ContentType.create("image/jpeg"), mFilePart.getName()));

    }
    @Override
    public String getBodyContentType() {
        return httpentity.getContentType().getValue();
    }
    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            httpentity.writeTo(bos);
        } catch (IOException e) {
            VolleyLog.e("IOException writing to ByteArrayOutputStream");
        }
        return bos.toByteArray();
    }
    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        try {
            return Response.success(new String(response.data, "UTF-8"),
                    getCacheEntry());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return Response.success(new String(response.data), getCacheEntry());
        }
    }

    @Override
    protected void deliverResponse(String response) {
        mListener.onResponse(response);
    }
}
