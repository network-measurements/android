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

    public void startSession() {
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
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e(TAG, error.toString());
                        }
                    });

            queue.add(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void sendRadio(Radio radio) {
        try {
            JSONObject args = radio.toJson();
            JsonObjectRequest request = new JsonObjectRequest
                    (Request.Method.GET, URL + "/session", args, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.e(TAG, "onResponse session: " + response.toString());

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e(TAG, error.toString());
                        }
                    });
            queue.add(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void sendLocation(Location location) {

    }

    public void sendLink(Link link) {

    }

    private String createSessionName() {
        return Calendar.getInstance().getTime().toString();
    }
}
