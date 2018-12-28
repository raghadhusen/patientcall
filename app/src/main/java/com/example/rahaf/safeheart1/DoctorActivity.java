package com.example.rahaf.safeheart1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.fasterxml.jackson.databind.JsonNode;
import com.scaledrone.lib.Member;
import com.scaledrone.lib.Room;
import com.scaledrone.lib.RoomListener;
import com.scaledrone.lib.Scaledrone;

public class DoctorActivity extends AppCompatActivity  {
    Button call,book,homeb;
    String patientID;
    String first_name, last_name, age, doctor_id,doctor_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);

        homeb= (Button) findViewById(R.id.homeBtn);
        call = (Button) findViewById(R.id.callBtn);
        book = (Button) findViewById(R.id.bookBtn);

        Bundle extras =getIntent().getExtras();
        if(extras != null)
        {

            patientID = extras.getString("ID");
            first_name = extras.getString("firstName");
            last_name = extras.getString("lastName");
            age = extras.getString("age");
            doctor_id = extras.getString("doctor_id");
            doctor_phone = extras.getString("doctorPhone");
        }
        homeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DoctorActivity.this, User_Activity.class);
                intent.putExtra("ID", patientID);
                intent.putExtra("firstName", first_name);
                intent.putExtra("lastName", last_name);
                intent.putExtra("age", age);
                intent.putExtra("doctor_id", doctor_id);
                startActivity(intent);
            }
        });

        call.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:"+doctor_phone));
                        if (ActivityCompat.checkSelfPermission(DoctorActivity.this,
                                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        startActivity(callIntent);
                    }
                }
        );

    }


}
