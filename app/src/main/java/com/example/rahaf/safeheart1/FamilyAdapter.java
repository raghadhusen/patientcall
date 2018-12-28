package com.example.rahaf.safeheart1;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * RecyclerView.Adapter
 * RecyclerView.ViewHolder
 *
 */
public class FamilyAdapter extends RecyclerView.Adapter<FamilyAdapter.FamilyViewHolder> {

    private Context context;
    private List<Family> familyList;


    public FamilyAdapter(Context context, List<Family> familyList) {
        this.context = context;
        this.familyList = familyList;
    }

    @NonNull
    @Override
    public FamilyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_layout, null);
        return new FamilyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(FamilyViewHolder familyViewHolder, final int position) {
        Family family = familyList.get(position);
        familyViewHolder.textViewTitle.setText(family.getName());
        familyViewHolder.familyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked on : " + familyList.get(position));
                String name = familyList.get(position).getName();
                String num = familyList.get(position).getNum();
                Intent intent = new Intent(context, Chatting.class);
                intent.putExtra("name", name);
                intent.putExtra("num", num);
                context.startActivity(intent);


            }
        });
        familyViewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(context );
                builder.setTitle("Delete");
                builder.setMessage("Are you sure you want to delete this one ")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                RemoveFamilyMember(position);
                            }
                        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        builder.setCancelable(true);
                    }
                });
                AlertDialog alert=builder.create();
                alert.show();


            }
        });


    }

    public void RemoveFamilyMember(int position) {
        final String FamilyID = familyList.get(position).getID();
        final String patientID = familyList.get(position).getPatient_id();
        Log.d("FamilyID" , FamilyID);
        Log.d("PatientID" , patientID);
        //Creating a string request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.REMOVE_FAMILY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //If we are getting success from server
                        Log.d("TAG!1" , response);
                        // String userName = responseJSON.getString("user");
                        if (response.equalsIgnoreCase(Config.LOGIN_SUCCESS)) {

                            Toast.makeText(context, "Successfully you delete this family member!!", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(context, FamilyActivity.class);
                            intent.putExtra("ID", patientID);
                            context.startActivity(intent);
                        } else {
                            //If the server response is not success
                            //Displaying an error message on toast
                            Toast.makeText(context, "Failed!! Please try AGAIN", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding parameters to request
                params.put(Config.KEY_USERNAME, FamilyID);


                //returning parameter
                return params;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put(Config.KEY_USERNAME, FamilyID);

                } catch (JSONException e) {

                }

                return jsonObject.toString().getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }



    @Override
    public int getItemCount() {
        return familyList.size();
    }

        class FamilyViewHolder extends RecyclerView.ViewHolder {
            //ImageView imageView;
            TextView textViewTitle;
            RelativeLayout familyLayout;
            Button deleteButton;

            public FamilyViewHolder(@NonNull View itemView) {
                super(itemView);
                //imageView = itemView.findViewById(R.id.imageView);
                textViewTitle = itemView.findViewById(R.id.textViewName);
                familyLayout = itemView.findViewById(R.id.family_layout);
                deleteButton = itemView.findViewById(R.id.delete);

            }


        }
    }

