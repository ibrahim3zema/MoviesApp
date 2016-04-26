package com.example.ibra.moviesapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import extra.ContVars;
import pojo.Movies;

public class MainActivity extends AppCompatActivity implements MovieListener {
    public static boolean mTwoPane;
    ContVars vars   ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        vars=new ContVars();
        FrameLayout flpanel2 = (FrameLayout) findViewById(R.id.f_panel_2);
        if (null == flpanel2) {
            mTwoPane = false;
        } else {
            mTwoPane = true;
        }

        if (null == savedInstanceState) {
            MainActivityFrag mainFragment = new MainActivityFrag();
            mainFragment.SetMovieListener(this);


            getSupportFragmentManager().beginTransaction().replace(R.id.f_panel_1, mainFragment).commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void movieDetail(Movies name) {

        //Single pan Ui
        if (mTwoPane) //Case Tow pan Ui
        {

            DetailsActivityFrag dFragment = new DetailsActivityFrag();
            Bundle extra = new Bundle();
            extra.putSerializable(vars.Extra, name);
            dFragment.setArguments(extra);
            getSupportFragmentManager().beginTransaction().replace(R.id.f_panel_2, dFragment).commit();

        } else { //Case One  pan Ui
            Intent i = new Intent(this, DetailsActivity.class);
            i.putExtra(vars.Extra, name);
            startActivity(i);

        }
    }
}
