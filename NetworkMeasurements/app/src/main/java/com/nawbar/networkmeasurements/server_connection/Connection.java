package com.nawbar.networkmeasurements.server_connection;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.nawbar.networkmeasurements.server_data.Link;
import com.nawbar.networkmeasurements.server_data.Location;
import com.nawbar.networkmeasurements.server_data.Radio;
import com.nawbar.networkmeasurements.view.ConsoleInput;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Bartosz Nawrot on 2017-10-14.
 */

public class Connection {

    private static final String TAG = Connection.class.getSimpleName();

    private static final String URL = "https://measurements-web-alcatras.c9users.io/";
    private static final String URL_BASE = URL + "sessions/";
    private static final String URL_PARAM = "?format=json";
    private static final String SESSIONS_POST = URL + "sessions" + URL_PARAM;
    private static final String LOCATION = "/locations" + URL_PARAM;
    private static final String RADIO = "/measurements" + URL_PARAM;
    private static final String LINK = "/link_measurements" + URL_PARAM;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);

    private RequestQueue queue;

    private ConsoleInput console;

    private String session;
    private long startTime;

    private String locationUrl;
    private String radioUrl;
    private String linkUrl;

    public Connection(Context context, ConsoleInput consoleInput) {
        this.queue = Volley.newRequestQueue(context);
        this.console = consoleInput;
    }

    public void startSession(final Connection.Listener listener) {
        JSONObject args = new JSONObject();
        String name = createSessionName();
        console.putMessage("CON: Starting session \"" + name + "\"");
        try {
            args.put("name", name);
            JsonObjectRequest request = new JsonObjectRequest
                    (Request.Method.POST, SESSIONS_POST, args, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.e(TAG, "onResponse session: " + response.toString());
                            try {
                                session = response.getString("id");
                                startTime = System.currentTimeMillis();
                                locationUrl = URL_BASE + session + LOCATION;
                                radioUrl = URL_BASE + session + RADIO;
                                linkUrl = URL_BASE + session + LINK;
                                listener.onSuccess();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                listener.onError("Failed to retrieve session ID");
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e(TAG, error.toString());
                            listener.onError(error.getMessage());
                        }
                    });
            queue.add(request);
        } catch (JSONException e) {
            e.printStackTrace();
            listener.onError(e.getMessage());
        }
    }

    public void sendLocation(Location location, final Connection.Listener listener) {
//        try {
//            JSONObject args = location.toJson();
//            args.put("time", System.currentTimeMillis() - startTime);
//            console.putMessage(location.toString());
//            JsonObjectRequest request = new JsonObjectRequest
//                    (Request.Method.POST, locationUrl, args, new Response.Listener<JSONObject>() {
//                        @Override
//                        public void onResponse(JSONObject response) {
//                            Log.e(TAG, "onResponse location: " + response.toString());
//                            listener.onSuccess();
//                        }
//                    }, new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            Log.e(TAG, error.toString());
//                            listener.onError(error.getMessage());
//                        }
//                    });
//            queue.add(request);
//        } catch (JSONException e) {
//            e.printStackTrace();
//            listener.onError(e.getMessage());
//        }
    }

    public void sendRadio(Radio radio, final Connection.Listener listener) {
//        try {
//            JSONObject args = radio.toJson();
//            args.put("time", System.currentTimeMillis() - startTime);
//            JsonObjectRequest request = new JsonObjectRequest
//                    (Request.Method.POST, radioUrl, args, new Response.Listener<JSONObject>() {
//                        @Override
//                        public void onResponse(JSONObject response) {
//                            Log.e(TAG, "onResponse radio: " + response.toString());
//                            listener.onSuccess();
//                        }
//                    }, new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            Log.e(TAG, error.toString());
//                            //Log.e(TAG, new String(error.networkResponse.data));
//                            listener.onError(error.getMessage());
//                        }
//                    });
//            queue.add(request);
//        } catch (JSONException e) {
//            e.printStackTrace();
//            listener.onError(e.getMessage());
//        }
    }

    public void sendLink(Link link, final Connection.Listener listener) {
//        try {
//            JSONObject args = link.toJson();
//            args.put("time", System.currentTimeMillis() - startTime);
//            JsonObjectRequest request = new JsonObjectRequest
//                    (Request.Method.POST, linkUrl, args, new Response.Listener<JSONObject>() {
//                        @Override
//                        public void onResponse(JSONObject response) {
//                            Log.e(TAG, "onResponse link: " + response.toString());
//                            listener.onSuccess();
//                        }
//                    }, new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            Log.e(TAG, error.toString());
//                            listener.onError(error.getMessage());
//                        }
//                    });
//            queue.add(request);
//        } catch (JSONException e) {
//            e.printStackTrace();
//            listener.onError(e.getMessage());
//        }
    }

    private String createSessionName() {
        return dateFormat.format(new Date());
    }

    public interface Listener {
        void onSuccess();

        void onError(String message);
    }
}
