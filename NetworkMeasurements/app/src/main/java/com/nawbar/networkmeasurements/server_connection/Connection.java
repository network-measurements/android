package com.nawbar.networkmeasurements.server_connection;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.nawbar.networkmeasurements.logger.LoggerInput;
import com.nawbar.networkmeasurements.measurements.models.Link;
import com.nawbar.networkmeasurements.measurements.models.Location;
import com.nawbar.networkmeasurements.measurements.models.Radio;
import com.nawbar.networkmeasurements.view.ConsoleInput;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Bartosz Nawrot on 2017-10-14.
 */

public class Connection {

    private static final String TAG = Connection.class.getSimpleName();

    private static final String URL = "https://measurements-web-beta-alcatras.c9users.io/";
    private static final String URL_PARAM = "?format=json";
    private static final String URL_BASE = URL + "measurement_sessions/";
    private static final String SESSIONS_POST = URL_BASE + URL_PARAM;
    private static final String LOCATION = "/location_measurements" + URL_PARAM;
    private static final String RADIO = "/radio_measurements" + URL_PARAM;
    private static final String LINK = "/link_measurements" + URL_PARAM;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);

    private RequestQueue queue;

    private ConsoleInput console;
    private LoggerInput logger;

    private String sessionId;
    private long startTime;

    private String locationUrl;
    private String radioUrl;
    private String linkUrl;

    public Connection(Context context, ConsoleInput consoleInput, LoggerInput loggerInput) {
        this.queue = Volley.newRequestQueue(context);
        this.console = consoleInput;
        this.logger = loggerInput;
    }

    public void startSession(final Listener listener) {
        final String name = createSessionName();
        console.putMessage("CON: Starting sessionId \"" + name + "\"");
        try {
            JSONObject args = new JSONObject();
            args.put("name", name);
            Log.e(TAG, args.toString());
            JsonObjectRequest request = new JsonObjectRequest
                    (Request.Method.POST, SESSIONS_POST, args, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.e(TAG, "onResponse sessionId: " + response.toString());
                            try {
                                sessionId = response.getString("id");
                                startTime = System.currentTimeMillis();
                                locationUrl = URL_BASE + sessionId + LOCATION;
                                radioUrl = URL_BASE + sessionId + RADIO;
                                linkUrl = URL_BASE + sessionId + LINK;
                                if (logger != null) logger.initialize(name);
                                listener.onSuccess();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                listener.onError("Failed to retrieve sessionId ID");
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            traceError(error, listener);
                        }
                    });
            queue.add(request);
        } catch (JSONException e) {
            e.printStackTrace();
            listener.onError(e.getMessage());
        }
    }

    public void sendLocation(Location location, final Listener listener) {
        try {
            JSONObject args = location.toJson(System.currentTimeMillis() - startTime);
            log(args.toString());
            JsonObjectRequest request = new JsonObjectRequest
                    (Request.Method.POST, locationUrl, args, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.e(TAG, "onResponse location: " + response.toString());
                            listener.onSuccess();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            traceError(error, listener);
                        }
                    });
            queue.add(request);
        } catch (JSONException e) {
            e.printStackTrace();
            listener.onError(e.getMessage());
        }
    }

    public void sendRadio(Radio radio, final Listener listener) {
        try {
            JSONObject args = radio.toJson(System.currentTimeMillis() - startTime);
            log(args.toString());
            JsonObjectRequest request = new JsonObjectRequest
                    (Request.Method.POST, radioUrl, args, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.e(TAG, "onResponse radio: " + response.toString());
                            listener.onSuccess();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            traceError(error, listener);
                        }
                    });
            queue.add(request);
        } catch (JSONException e) {
            e.printStackTrace();
            listener.onError(e.getMessage());
        }
    }

    public void sendLink(Link link, final Listener listener) {
        try {
            JSONObject args = link.toJson(System.currentTimeMillis() - startTime);
            log(args.toString());
            JsonObjectRequest request = new JsonObjectRequest
                    (Request.Method.POST, linkUrl, args, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.e(TAG, "onResponse link: " + response.toString());
                            listener.onSuccess();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            traceError(error, listener);
                        }
                    });
            queue.add(request);
        } catch (JSONException e) {
            e.printStackTrace();
            listener.onError(e.getMessage());
        }
    }

    private String createSessionName() {
        return dateFormat.format(new Date());
    }

    private void log(String message) {
        Log.e(TAG, message);
        if (logger != null) logger.log(message);
    }

    private void traceError(VolleyError error, Listener listener) {
        String message = getErrorMessage(error);
        Log.e(TAG, message);
        if (error.networkResponse != null && error.networkResponse.data != null) {
            Log.e(TAG, new String(error.networkResponse.data));
        }
        listener.onError(message);
    }

    private String getErrorMessage(VolleyError error){
        String message = "Unknown error";
        if(error instanceof TimeoutError){
            message = "Timeout error";
        } else if ((error instanceof ServerError || error instanceof AuthFailureError)){
            if (error.networkResponse != null) {
                return "Server error " + error.networkResponse.statusCode;
            } else {
                return "Unknown server error";
            }
        }else if(error instanceof NetworkError){
            message = "Connection error";
        }
        return message;
    }

    public String getSessionId() {
        return sessionId;
    }

    public interface Listener {
        void onSuccess();
        void onError(String message);
    }
}