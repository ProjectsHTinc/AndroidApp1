package com.palprotech.heylaapp.servicehelpers;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.palprotech.heylaapp.R;
import com.palprotech.heylaapp.app.AppController;
import com.palprotech.heylaapp.serviceinterfaces.IServiceListener;
import com.palprotech.heylaapp.utils.HeylaAppConstants;
import com.palprotech.heylaapp.utils.PreferenceStorage;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;

/**
 * Created by Admin on 25-09-2017.
 */

public class ServiceHelper {

    private String TAG = "Get Name";
    private Context context;
    private IServiceListener iServiceListener;

    public ServiceHelper(Context context) {
        this.context = context;
    }

    public void setServiceListener(IServiceListener iServiceListener) {
        this.iServiceListener = iServiceListener;
    }

    public void makeGetServiceCall(String params, String urls) {
        Log.d(TAG, "making sign in request" + params);
        String baseURL = "";
        try {
            URI uri = new URI(urls.replace(" ", "%20"));
            baseURL = uri.toString();

            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                    baseURL, params,
                    new com.android.volley.Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, response.toString());
                            iServiceListener.onResponse(response);
                        }
                    }, new com.android.volley.Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error.networkResponse != null && error.networkResponse.data != null) {
                        Log.d(TAG, "error during sign up" + error.getLocalizedMessage());

                        try {
                            String responseBody = new String(error.networkResponse.data, "utf-8");
                            JSONObject jsonObject = new JSONObject(responseBody);
                            iServiceListener.onError(jsonObject.getString(HeylaAppConstants.PARAM_MESSAGE));
                            String status = jsonObject.getString("status");
                            Log.d(TAG, "signup status is" + status);
                        } catch (UnsupportedEncodingException e) {
                            iServiceListener.onError(context.getResources().getString(R.string.error_occurred));
                            e.printStackTrace();
                        } catch (JSONException e) {
                            iServiceListener.onError(context.getResources().getString(R.string.error_occurred));
                            e.printStackTrace();
                        }

                    } else {
                        iServiceListener.onError(context.getResources().getString(R.string.error_occurred));
                    }
                }
            });

            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(jsonObjectRequest);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}
