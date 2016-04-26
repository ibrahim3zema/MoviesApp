package com.example.ibra.moviesapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.List;

import adapters.MovieAdapter;
import extra.uri;
import parser.MoviesParser;
import pojo.Movies;
import utils.DBTransaction;

public class MainActivityFrag extends Fragment {
    List<Movies> moviesList;
    GridView gridView;
    uri uri = new uri();
    DBTransaction transaction;
    MovieListener mListener;
    Movies movies;
    MovieAdapter adapter;

    public MainActivityFrag() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (savedInstanceState == null) {
            displayView(uri.POPULAR);
        } else {
            uri.type = savedInstanceState.getString("state");
            displayView(uri.type);
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_activity, container, false);
        gridView = (GridView) view.findViewById(R.id.gridView);
        transaction = new DBTransaction(getActivity());
      //  Toast.makeText(getActivity(), "dddddd", Toast.LENGTH_SHORT).show();
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               // movies= (Movies) adapter.getItem(position);
                movies = moviesList.get(position);
              //  Toast.makeText(getActivity(), "added", Toast.LENGTH_SHORT).show();
                mListener.movieDetail(movies);
            }
        });
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("state", uri.type);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_popular) {
            displayView(uri.POPULAR);
            uri.type = uri.POPULAR;
        }
        if (id == R.id.action_rated) {
            displayView(uri.TOP_RATED);
            Toast.makeText(getActivity(), "top rated", Toast.LENGTH_SHORT).show();
            uri.type = uri.TOP_RATED;
        }
        if (id == R.id.fav) {
            requestFavorite();
        }

        return super.onOptionsItemSelected(item);
    }

    private void displayView(String displayType) {
        if (isOnline()) {
            requestData(uri.URI + displayType + uri.API + uri.API_KEY_VALUE);
        } else {
            Toast.makeText(getActivity(), "Network isn't available", Toast.LENGTH_LONG).show();
        }
    }

    private void requestData(String uri) {
        StringRequest request = new StringRequest(uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        moviesList = MoviesParser.parseFeed(response);
                        updateDisplay();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);
    }

    private void requestFavorite() {
        List<Movies> mList = transaction.get_Fav();
        if (mList != null && mList.size() > 0) {
            Toast.makeText(getActivity(), " Favorite Movies !", Toast.LENGTH_LONG).show();
            moviesList.clear();
            moviesList.addAll(mList);
            MovieAdapter adapter = new MovieAdapter(getActivity(), (ArrayList<Movies>) moviesList);
            gridView.setAdapter(adapter);

        } else {
            Toast.makeText(getActivity(), "No Favorite Movies !", Toast.LENGTH_LONG).show();
            displayView(uri.type);
        }
    }

    protected void updateDisplay() {
        MovieAdapter adapter = new MovieAdapter(getActivity(), (ArrayList<Movies>) moviesList);
        gridView.setAdapter(adapter);
    }

    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public void SetMovieListener(MovieListener movisListener) {
        mListener = movisListener;
    }

}
