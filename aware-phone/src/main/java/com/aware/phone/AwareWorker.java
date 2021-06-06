package com.aware.phone;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.aware.Aware;
import com.aware.Aware_Preferences;
import com.aware.plugin.device_usage.Provider;
import com.aware.plugin.studentlife.audio_final.Settings;
import com.aware.providers.Battery_Provider;
import com.aware.providers.Communication_Provider;
import com.aware.providers.Locations_Provider;
import com.aware.providers.Screen_Provider;

import com.aware.utils.DatabaseHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class AwareWorker extends Worker {
    public AwareWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        Context appContext = getApplicationContext();
        Data inputData=getInputData();
        String token = inputData.getString("token");
        String URL = inputData.getString("URL");
        Log.i("Worker",token);
        Log.i("URL",URL);


        Cursor location_data = appContext.getContentResolver().query(Locations_Provider.Locations_Data.CONTENT_URI, null, null, null,"timestamp desc limit 1");
        RequestQueue requestQueue_location = Volley.newRequestQueue(appContext);
        JSONObject jsonBody_location = new JSONObject();
        if( location_data.getCount() > 0 ) {
            try {


                String new_data = DatabaseHelper.cursorToString(location_data);
                jsonBody_location.put("Title", "location");

                jsonBody_location.put("data", new_data);
                Log.i("VOLLEY", new_data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            send_data(jsonBody_location, requestQueue_location, URL,token);


        }

        Cursor screen_data = appContext.getContentResolver().query(Screen_Provider.Screen_Data.CONTENT_URI, null, null, null, "timestamp desc limit 1");
        RequestQueue requestQueue_screen = Volley.newRequestQueue(appContext);
        JSONObject jsonBody_screen = new JSONObject();
        if( screen_data.getCount() > 0 ) {
            try {


                String new_data = DatabaseHelper.cursorToString(screen_data);
                jsonBody_screen.put("Title", "screen");
                jsonBody_screen.put("data", new_data);
                Log.i("VOLLEY", new_data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            send_data(jsonBody_screen, requestQueue_screen, URL,token);


        }

        Cursor device_data = appContext.getContentResolver().query(Provider.DeviceUsage_Data.CONTENT_URI, null, null, null, "timestamp desc limit 1");
        RequestQueue requestQueue_device = Volley.newRequestQueue(appContext);
        JSONObject jsonBody_device = new JSONObject();
        if( device_data.getCount() > 0 ) {
            try {


                String new_data = DatabaseHelper.cursorToString(device_data);
                jsonBody_device.put("Title", "device usage");
                jsonBody_device.put("data", new_data);
                Log.i("VOLLEY", new_data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            send_data(jsonBody_device, requestQueue_device, URL,token);


        }
        Cursor conversation_data = appContext.getContentResolver().query(com.aware.plugin.studentlife.audio_final.Provider.StudentLifeAudio_Data.CONTENT_URI, null, null, null, "timestamp desc limit 1");
        RequestQueue requestQueue_conversation = Volley.newRequestQueue(appContext);
        JSONObject jsonBody_conversation = new JSONObject();
        if( conversation_data.getCount() > 0 ) {
            try {


                String new_data = DatabaseHelper.cursorToString(conversation_data);
                jsonBody_conversation.put("Title", "conversation");
                jsonBody_conversation.put("data", new_data);
                Log.i("VOLLEY", new_data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            send_data(jsonBody_conversation, requestQueue_conversation, URL,token);


        }

        Cursor comcall_data = appContext.getContentResolver().query(Communication_Provider.Calls_Data.CONTENT_URI, null, null, null, "timestamp desc limit 1");
        RequestQueue requestQueue_comcall = Volley.newRequestQueue(appContext);
        JSONObject jsonBody_comcall = new JSONObject();
        if( comcall_data.getCount() > 0 ) {
            try {


                String new_data = DatabaseHelper.cursorToString(comcall_data);
                jsonBody_conversation.put("Title", "call");
                jsonBody_conversation.put("data", new_data);
                Log.i("VOLLEY", new_data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            send_data(jsonBody_comcall, requestQueue_comcall, URL,token);


        }

        Cursor commess_data = appContext.getContentResolver().query(Communication_Provider.Calls_Data.CONTENT_URI, null, null, null, "timestamp desc limit 1");
        RequestQueue requestQueue_mess = Volley.newRequestQueue(appContext);
        JSONObject jsonBody_mess = new JSONObject();
        if( commess_data.getCount() > 0 ) {
            try {


                String new_data = DatabaseHelper.cursorToString(commess_data);
                jsonBody_conversation.put("Title", "message");
                jsonBody_conversation.put("data", new_data);
                Log.i("VOLLEY", new_data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            send_data(jsonBody_mess, requestQueue_mess, URL,token);


        }

        Cursor battery_data = appContext.getContentResolver().query(Battery_Provider.Battery_Data.CONTENT_URI, null, null, null, "timestamp desc limit 1");
        RequestQueue requestQueue_battery = Volley.newRequestQueue(appContext);
        JSONObject jsonBody_battery = new JSONObject();
        if( battery_data.getCount() > 0 ) {
            try {


                String new_data = DatabaseHelper.cursorToString(battery_data);
                jsonBody_battery.put("Title", "battery");
                jsonBody_battery.put("data", new_data);
                Log.i("VOLLEY", new_data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            send_data(jsonBody_battery, requestQueue_battery, URL,token);


        }




        return Result.success();
    }


    public void send_data(JSONObject jsonBody, RequestQueue requestQueue, String URL, final String token) {
        Log.i("all",jsonBody.toString());
        final String requestBody = jsonBody.toString();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("VOLLEY", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VOLLEY", error.toString());
            }
        }) {
            @Override
            public Map<String,String> getHeaders()throws AuthFailureError {
                HashMap<String,String> headers = new HashMap<>();
                headers.put("Authorization",token);
                return headers;
            }
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }
        };
        requestQueue.add(stringRequest);
    }

}
