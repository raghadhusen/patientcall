package com.example.rahaf.safeheart1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FamilyActivity extends AppCompatActivity {



    RecyclerView recyclerView;
    FamilyAdapter familyAdapter;
    Button add_family;

    List<Family> familyList;
    String patientID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family);
        add_family=(Button)findViewById(R.id.add);

        familyList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            patientID = extras.getString("ID");
        }
        loadFamilyList();
        //adding some items to our list




        //creating recyclerView adapter
        FamilyAdapter adapter = new FamilyAdapter(this, familyList);

        //setting adapter to recyclerView
        recyclerView.setAdapter(adapter);

    }

    public void add_new_family(View view)
    {
        Intent intent = new Intent(FamilyActivity.this, AddNew.class);
        intent.putExtra("ID",patientID);
        startActivity(intent);





    }

    private void loadFamilyList() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.FAMILY_LIST,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("tag1", "onResponse: "+ response);

                        try {
                            JSONArray family_list = new JSONArray(response);
                             for(int i =0; i<family_list.length();i++)
                             {
                                 JSONObject familyObject = family_list.getJSONObject(i);
                                 String id = familyObject.getString("id");
                                 String name = familyObject.getString("name");
                                 String num = familyObject.getString("num");

                                 Family family = new Family(id,name, num, patientID);
                                 familyList.add(family);
                             }
                            //creating recyclerView adapter
                            FamilyAdapter adapter = new FamilyAdapter(FamilyActivity.this, familyList);

                            //setting adapter to recyclerView
                            recyclerView.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(FamilyActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();;

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {


                Map<String, String> postParam = new HashMap<String, String>();
                postParam.put(Config.KEY_USERNAME, patientID);
                return postParam;
            }


            @Override
            public byte[] getBody() throws AuthFailureError {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put(Config.KEY_USERNAME, patientID);

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
        Volley.newRequestQueue(this).add(stringRequest);

    }
}
