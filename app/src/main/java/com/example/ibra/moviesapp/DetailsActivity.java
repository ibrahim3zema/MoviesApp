package com.example.ibra.moviesapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        if (null==savedInstanceState)
        {
        getSupportFragmentManager().beginTransaction().
                add(R.id.Detalis_frame_layout, new DetailsActivityFrag()).commit();
        }
    }
}
