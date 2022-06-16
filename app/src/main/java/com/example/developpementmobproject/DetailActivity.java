package com.example.developpementmobproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {

    String matchTitle = "";
    String matchPlayer1 = "";
    String matchPlayer2 = "";
    String matchScore = "";
    String matchDate = "";
    String localisation = "";
    String matchId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        TextView textViewTitre = findViewById(R.id.titre);
        TextView textViewjoueur1 = findViewById(R.id.joueur1);
        TextView textViewjoueur2 = findViewById(R.id.joueur2);
        TextView textViewScoreMatch = findViewById(R.id.scoreMatch);
        TextView textViewDate = findViewById(R.id.dateMatch);


        try {
            Intent myIntent = getIntent(); // gets the previously created intent
            matchId = myIntent.getStringExtra("matchTitleExtra");

            System.out.println(matchId);
        }
        catch (Exception e){
            System.out.println("Error when retrieving extra");
        }


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    Connection connection = DriverManager.getConnection("jdbc:mysql://10.0.2.2:3306/tennismatch", "root", "");

                    String sql = "SELECT * FROM formulairetable WHERE title = '"+matchId+"'";
                    System.out.println(matchId);
                    PreparedStatement statement = connection.prepareStatement(sql);
                    ResultSet result = statement.executeQuery();


                    //Stores properties of a ResultSet object, including column count
                    ResultSetMetaData rsmd = result.getMetaData();
                    int columnCount = rsmd.getColumnCount();

                    ArrayList<String> arrayResult = new ArrayList<>(columnCount);
                    while (result.next()) {
                        int i = 1;
                        while (i <= columnCount) {
                            arrayResult.add(result.getString(i++));
                        }
                    }

                    System.out.println(arrayResult);

                    matchTitle = arrayResult.get(1);
                    matchPlayer1 = arrayResult.get(2);
                    matchPlayer2 = arrayResult.get(3);
                    matchScore = arrayResult.get(6);
                    matchDate = arrayResult.get(5);
                    localisation = arrayResult.get(4);



                    new Handler(Looper.getMainLooper()).post(new Runnable(){
                        @Override
                        public void run() {
                            textViewTitre.setText(matchTitle);
                            textViewjoueur1.setText(matchPlayer1);
                            textViewjoueur2.setText(matchPlayer2);
                            textViewScoreMatch.setText(matchScore);
                            textViewDate.setText(matchDate);




                            FragmentManager fragmentManager = getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            Bundle bundle = new Bundle();
                            bundle.putString("localisation", localisation );
                            MapsFragment gFrg = new MapsFragment();
                            gFrg.setArguments(bundle);
                            fragmentTransaction.replace(R.id.frameMapsFragment, gFrg);
                            fragmentTransaction.commit();

                        }
                    });


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();



    }
}