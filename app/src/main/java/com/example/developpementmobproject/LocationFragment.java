package com.example.developpementmobproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LocationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LocationFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public LocationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LocationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LocationFragment newInstance(String param1, String param2) {
        LocationFragment fragment = new LocationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_location, container, false);
        ListView listView = (ListView) v.findViewById(R.id.listTitle);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    Connection connection = DriverManager.getConnection("jdbc:mysql://10.0.2.2:3306/tennismatch", "root", "");

                    String sql = "SELECT title FROM formulairetable";
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

                    new Handler(Looper.getMainLooper()).post(new Runnable(){
                        @Override
                        public void run() {
                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, arrayResult);
                            listView.setAdapter(arrayAdapter);

                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    String match = adapterView.getItemAtPosition(i).toString();
                                    Intent detailsMatchActivityIntent = new Intent(getActivity(), DetailActivity.class);
                                    detailsMatchActivityIntent.putExtra("matchTitleExtra", match);
                                    startActivity(detailsMatchActivityIntent);
                                }
                            });
                        }
                    });


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }}).start();

        return v;
    }


}