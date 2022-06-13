package com.example.developpementmobproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
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

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;

public class AddMatchActivity extends AppCompatActivity {
    TextView textView;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_match);
        textView = (TextView) findViewById(R.id.lieu);
        button = (Button) findViewById(R.id.buttonLocation);


        LocationManager locationManager = null;
        String fournisseur;
        String lastLocation;



        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteres = new Criteria();

        // la précision  : (ACCURACY_FINE pour une haute précision ou ACCURACY_COARSE pour une moins bonne précision)
        criteres.setAccuracy(Criteria.ACCURACY_FINE);

        // l'altitude
        criteres.setAltitudeRequired(true);

        // la direction
        criteres.setBearingRequired(true);

        // la vitesse
        criteres.setSpeedRequired(true);

        // la consommation d'énergie demandée
        criteres.setCostAllowed(true);
        criteres.setPowerRequirement(Criteria.POWER_HIGH);

        fournisseur = locationManager.getBestProvider(criteres, true);
        Log.d("GPS", "fournisseur : " + fournisseur);

        // dernière position connue
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location localisation = locationManager.getLastKnownLocation(fournisseur);
        Log.d("GPS", "localisation : " + localisation.toString());
        lastLocation = localisation.toString();
        String coordonnees = String.format("Latitude : %f - Longitude : %f\n", localisation.getLatitude(), localisation.getLongitude());
        Log.d("GPS", "coordonnees : " + coordonnees);
        String autres = String.format("Vitesse : %f - Altitude : %f - Cap : %f\n", localisation.getSpeed(), localisation.getAltitude(), localisation.getBearing());
        Log.d("GPS", autres);
        //String timestamp = String.format("Timestamp : %d\n", localisation.getTime());
        //Log.d("GPS", "timestamp : " + timestamp);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date(localisation.getTime());
        Log.d("GPS", sdf.format(date));




        button.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                textView.setText(lastLocation);
            }
        });




        Button buttonAddMatch = findViewById(R.id.buttonSaveMatch);
        EditText inputTitle = findViewById(R.id.titre);
        EditText inputPlayer1 = findViewById(R.id.joueur_1);
        EditText inputPlayer2 = findViewById(R.id.joueur_2);
        EditText inputDate = findViewById(R.id.date);
        EditText inputScore = findViewById(R.id.score);
        //EditText inputLocalisation = findViewById(R.id.inputLocalisation);



        buttonAddMatch.setOnClickListener(
            new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String title = inputTitle.getText().toString();
                        String player1 = inputPlayer1.getText().toString();
                        String player2 = inputPlayer2.getText().toString();
                        String date = inputDate.getText().toString();
                        String score = inputScore.getText().toString();
                        //String localisation = inputLocalisation.getText().toString();


                        new Thread(new Runnable() {
                            @Override
                            public void run() {

                                try {
                                    Connection connection = DriverManager.getConnection("jdbc:mysql://10.0.2.2:3306/tennismatch", "root", "");

                                    String sql = "INSERT INTO formulairetable (Title, Player1, Player2, score, Localisation, DateMatch) VALUES ('" + title + "', '" + player1 +"', '" + player2 +"', '" + score +"', 'abc', '" + date +"')";
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