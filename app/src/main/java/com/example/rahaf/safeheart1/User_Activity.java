package com.example.rahaf.safeheart1;

import android.Manifest;
import android.app.Notification;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class User_Activity extends AppCompatActivity {
    ImageButton doctor_button, family_button, location, notification_button;
    TextView txtArduino, patient_name;
    String patientID;
    String first_name, last_name, age, doctor_id;
    ArrayList<Integer> rates = new ArrayList<>();
    int count = 0;
    public String reading="";
    String doctorPhone="";
    List<String> familiesPhone = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        //BluetoothConnection();
        doctor_button = (ImageButton) findViewById(R.id.doctor);
        family_button = (ImageButton) findViewById(R.id.family);
        location = (ImageButton) findViewById(R.id.location);
        notification_button = (ImageButton) findViewById(R.id.notification);
        patient_name = (TextView) findViewById(R.id.patient_name);
        txtArduino = (TextView) findViewById(R.id.textView2);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            patientID = extras.getString("ID");
            first_name = extras.getString("firstName");
            last_name = extras.getString("lastName");
            age = extras.getString("age");
            doctor_id = extras.getString("doctor_id");
        }
        patient_name.setText(first_name);
        fillRates();
        //getDoctorPhone();
        //getFamiliesPhone();
        final Handler haa=new Handler();
        haa.postDelayed(new Runnable() {
            @Override
            public void run() {
                updateTextView(count);
                haa.postDelayed(this, 1000);
            }
        }, 1000);
/**
 * if(!checkReading())
 * {
 *      pushNotofication
 *
 *
 * }
 *
 */
        final Handler ha=new Handler();
        ha.postDelayed(new Runnable() {

            @Override
            public void run() {
               updateReading();

                ha.postDelayed(this, 30000);
            }
        }, 30000);

        final Handler handler=new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                checkReading();

                ha.postDelayed(this, 1000);
            }
        }, 1000);

        }
    public void checkReading() {
        int read = Integer.parseInt(reading);

        if (20 <= read && read < 40) {
            SendSMS();

        } else if (40 <= read && read < 60) {
            SendSMS();

        } else if (read > 120) {
            SendSMS();
        }
    }
    public void SendSMS()
    {

        String Massege = " ATTENTION!! "+ patient_name.toString() + " please follow her up and try to reach her as soon s";


    }

    private void fillRates() {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(getAssets().open("reading1.txt")));

            String line = "";
            while((line = br.readLine())!= null)
            {
                String tokens[] = line.split(" ");
                for(int i = 0 ; i<tokens.length;i++)
                {
                    rates.add(Integer.parseInt(tokens[i]));
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void updateTextView(int i) {
        reading = String.valueOf(rates.get(i));
        txtArduino.setText(String.valueOf(rates.get(i))+ " PBM");
        count++;
    }


    public void DoctorActivity(View view) {
        doctor_button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), DoctorActivity.class);

                        intent.putExtra("ID", patientID);
                        intent.putExtra("firstName", first_name);
                        intent.putExtra("lastName", last_name);
                        intent.putExtra("age", age);
                        intent.putExtra("doctor_id", doctor_id);
                        intent.putExtra("doctorPhone",doctorPhone);
                        startActivity(intent);

                    }
                }
        );
    }


    public void FamilyActivity(View view) {
        family_button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), FamilyActivity.class);
                        intent.putExtra("ID", patientID);
                        startActivity(intent);
                    }
                }
        );
    }

    public void LocationActivity(View view) {
        location.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }
        );
    }

    public void NotificationActivity() {
        notification_button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }
        );
    }
    public void updateReading()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.UPDATE_READING,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //If we are getting success from server
                        Log.d("tag", "onResponse: "+ response);
                        JSONObject responseJSON = null;
                        try {
                            responseJSON = new JSONObject(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        String status = null;
                        try {
                            if (responseJSON != null) {
                                status = responseJSON.getString("status");
                            }
                            if(status.equalsIgnoreCase(Config.LOGIN_SUCCESS)) {
                                Toast.makeText(User_Activity.this, "SUCCESS", Toast.LENGTH_LONG).show();



                            }else{
                                //If the server response is not success
                                //Displaying an error message on toast
                                Toast.makeText(User_Activity.this, "Failed when update reading on database", Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                //Adding parameters to request
                params.put(Config.KEY_USERNAME, patientID);
                params.put("reading",reading);

                //returning parameter
                return params;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put(Config.KEY_USERNAME, patientID);
                    jsonObject.put("reading", reading);
                }catch(JSONException e)
                {

                }

                return jsonObject.toString().getBytes() ;
            }
            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void getDoctorPhone()
    {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.GET_DOCTOR_PHONE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //If we are getting success from server
                        Log.d("tag", "onResponse: "+ response);
                        JSONObject responseJSON = null;
                        try {
                            responseJSON = new JSONObject(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        String status = null;
                        try {
                            if (responseJSON != null) {
                                status = responseJSON.getString("status");
                            }
                            if(status.equalsIgnoreCase(Config.LOGIN_SUCCESS)) {
                                doctorPhone = responseJSON.getString("phone");




                            }else{
                                //If the server response is not success
                                //Displaying an error message on toast
                                Toast.makeText(User_Activity.this, "Failed to get Doctor Phone", Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want
                    }
                }){
            @Override
            public byte[] getBody() throws AuthFailureError {
                JSONObject jsonObject = new JSONObject();
                try {
                          jsonObject.put("id", doctor_id);
                }catch(JSONException e)
                {

                }

                return jsonObject.toString().getBytes() ;
            }
            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void getFamiliesPhone()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.GET_FAMILIES_PHONE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //If we are getting success from server
                        Log.d("tag", "onResponse: "+ response);
                       try{
                           JSONArray family_phone = new JSONArray(response);
                           for(int i =0; i<family_phone.length();i++)
                           {
                               JSONObject familyPhone = family_phone.getJSONObject(i);

                               String num = familyPhone.getString("num");

                              familiesPhone.add(num);
                           }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want
                    }
                }){
            @Override
            public byte[] getBody() throws AuthFailureError {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("username", patientID);
                }catch(JSONException e)
                {

                }

                return jsonObject.toString().getBytes() ;
            }
            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }


}