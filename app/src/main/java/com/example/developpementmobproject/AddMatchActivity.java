package com.example.developpementmobproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;


public class AddMatchActivity extends AppCompatActivity {
    TextView textView;
    Button button;
    String localisationMatch = "";


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_match);
        textView = (TextView) findViewById(R.id.localisation);
        button = (Button) findViewById(R.id.buttonLocation);


        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Localisation locationListener = new Localisation();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            System.out.println("ERROR PERMISSION");
            ActivityCompat.requestPermissions(AddMatchActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);
        }
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 5000, 10, locationListener);

        button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        textView.setText(locationListener.getLatitude() + "    "  + locationListener.getLongitude());
                        localisationMatch = ""+ locationListener.getLatitude() + "," + locationListener.getLongitude() + "";
                    }
                });



        Button buttonAddMatch = findViewById(R.id.buttonSaveMatch);
        EditText inputTitle = findViewById(R.id.titre);
        EditText inputPlayer1 = findViewById(R.id.joueur_1);
        EditText inputPlayer2 = findViewById(R.id.joueur_2);
        EditText inputDate = findViewById(R.id.date);
        EditText inputScore = findViewById(R.id.score);
        EditText inputLocalisation = findViewById(R.id.localisation);



        buttonAddMatch.setOnClickListener(
            new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String title = inputTitle.getText().toString();
                        String player1 = inputPlayer1.getText().toString();
                        String player2 = inputPlayer2.getText().toString();
                        String date = inputDate.getText().toString();
                        String score = inputScore.getText().toString();
                        String localisation = inputLocalisation.getText().toString();


                        new Thread(new Runnable() {
                            @Override
                            public void run() {

                                try {
                                    Connection connection = DriverManager.getConnection("jdbc:mysql://10.0.2.2:3306/tennismatch", "root", "");

                                    String sql = "INSERT INTO formulairetable (Title, Player1, Player2, score, Localisation, DateMatch) VALUES ('" + title + "', '" + player1 +"', '" + player2 +"', '" + score +"', '"+ localisation +"', '" + date +"')";
                                    PreparedStatement statement = connection.prepareStatement(sql);
                                    statement.execute();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                        }).start();
                    }
            }
        );
    }
}