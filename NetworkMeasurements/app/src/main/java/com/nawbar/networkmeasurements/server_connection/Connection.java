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

import java.util.Calendar;

/**
 * Created by Bartosz Nawrot on 2017-10-14.
 */

public class Connection {

    private static final String TAG = Connection.class.getSimpleName();
    private static final String URL = "https://web-meas-alcatras.c9users.io/";

    private ConsoleInput console;

    private String session;

    private RequestQueue queue;

    public Connection(Context context, ConsoleInput consoleInput) {
        this.queue = Volley.newRequestQueue(context);
    }

    public void startSession(final Connection.Listener listener) {
        JSONObject args = new JSONObject();
        try {
            args.put("name", createSessionName());
            JsonObjectRequest request = new JsonObjectRequest
                    (Request.Method.POST, URL + "sessions?format=json", args, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.e(TAG, "onResponse session: " + response.toString());
                            try {
                                session = response.getString("id");
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

    public void sendRadio(Radio radio, final Connection.Listener listener) {
        try {
            JSONObject args = radio.toJson();
            JsonObjectRequest request = new JsonObjectRequest
                    (Request.Method.GET, URL + "/radio", args, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.e(TAG, "onResponse radio: " + response.toString());
                            listener.onSuccess();
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
        try {
            JSONObject args = location.toJson();
            JsonObjectRequest request = new JsonObjectRequest
                    (Request.Method.GET, URL + "/location", args, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.e(TAG, "onResponse location: " + response.toString());
                            listener.onSuccess();
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

    public void sendLink(Link link, final Connection.Listener listener) {

    }

    private String createSessionName() {
        return Calendar.getInstance().getTime().toString();
    }

    public interface Listener {
        void onSuccess();
        void onError(String message);
    }
}
