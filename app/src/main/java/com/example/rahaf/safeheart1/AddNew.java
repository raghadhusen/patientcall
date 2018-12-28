package com.example.rahaf.safeheart1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddNew extends AppCompatActivity {
    EditText id,name,phone,password;
    Button addNew;
    String patientID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new);
        id = (EditText) findViewById(R.id.id);
        name = (EditText) findViewById(R.id.name);
        phone=(EditText) findViewById(R.id.phone);
        password = (EditText) findViewById(R.id.pass);

        addNew = (Button) findViewById(R.id.add);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            patientID = extras.getString("ID");
        }


    }
    public void add_new(View view)
    {
        final String username = patientID;
        final String famID = id.getText().toString().trim();
        final String famName = name.getText().toString().trim();
        final String famPhone = phone.getText().toString().trim();
        final String famPassword = password.getText().toString().trim();


        //Creating a string request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.ADD_FAMILY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //If we are getting success from server

                        // String userName = responseJSON.getString("user");
                        if(response.equalsIgnoreCase(Config.LOGIN_SUCCESS)) {

                            Toast.makeText(AddNew.this, "Successfully you add new family member!!", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(AddNew.this, FamilyActivity.class);
                            intent.putExtra("ID",patientID);
                            startActivity(intent);
                        }else{
                            //If the server response is not success
                            //Displaying an error message on toast
                            Toast.makeText(AddNew.this, "Failed!! Please try new ID", Toast.LENGTH_LONG).show();
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
                params.put(Config.KEY_USERNAME, username);
                params.put("id", famID);
                params.put("name", famName);
                params.put("phone", famPhone);
                params.put("password", famPassword);


                //returning parameter
                return params;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put(Config.KEY_USERNAME, username);
                    jsonObject.put("id", famID);
                    jsonObject.put("name", famName);
                    jsonObject.put("phone", famPhone);
                    jsonObject.put("password", famPassword);

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

