package com.example.developpementmobproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AddMatchActivity extends AppCompatActivity {
    TextView textView;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_match);
        textView=(TextView) findViewById(R.id.lieu);
        button=(Button) findViewById(R.id.buttonLocation);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText("Location Trouvée");
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