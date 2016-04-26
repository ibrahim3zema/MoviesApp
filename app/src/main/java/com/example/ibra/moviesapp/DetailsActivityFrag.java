package com.example.ibra.moviesapp;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import adapters.ReviewAdapter;
import adapters.TrailerAdapter;
import extra.ContVars;
import extra.uri;
import parser.DurationParser;
import parser.ReviewParser;
import parser.TrailerParser;
import pojo.Movies;
import pojo.Review;
import pojo.Trailer;
import utils.DBTransaction;

public class DetailsActivityFrag extends Fragment {
    TextView year, time, rate, name, overview;
    public int id;
    String ID;
    ImageView imageView;
    Context context;
    uri uri = new uri();
    Movies obj;
    public static ListView trailer_listView;
    ListView review_ListView = null;
    public static List<Trailer> trailers;
    List<Review> reviews;
    ToggleButton toggleButton;
    DBTransaction transaction;
    Button showReviews;

    public DetailsActivityFrag() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);

        year = (TextView) view.findViewById(R.id.year);
        name = (TextView) view.findViewById(R.id.name);
        time = (TextView) view.findViewById(R.id.time);
        rate = (TextView) view.findViewById(R.id.rate);
        overview = (TextView) view.findViewById(R.id.overview);
        imageView = (ImageView) view.findViewById(R.id.imageView);
        trailer_listView = (ListView) view.findViewById(R.id.trailer_listView);
        review_ListView = new ListView(getActivity());
        toggleButton = (ToggleButton) view.findViewById(R.id.toggleButton);
        showReviews = (Button) view.findViewById(R.id.showRevs);
        showReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showReviews();
            }
        });
        try {
            obj = (Movies) getArguments().getSerializable(ContVars.Extra);
            //Toast.makeText(getActivity(), "yyyyyyyyyy", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Intent intent = getActivity().getIntent();
            obj = (Movies) intent.getSerializableExtra(ContVars.Extra);
            //Toast.makeText(getActivity(), "nnnnnnnnnn", Toast.LENGTH_SHORT).show();

        }
        year.setText(obj.getRelease_date());
        name.setText(obj.getOriginal_title());
        rate.setText(obj.getVote_average() + " " + "/10");
        overview.setText(obj.getOverview());
        id = obj.getId();
        ID = String.valueOf(id);
        Picasso.with(context).load(uri.PATH + obj.getPoster_path()).into(imageView);
        transaction = new DBTransaction(getActivity());
        CompoundButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    transaction.set_Fav(obj);
                    Toast.makeText(getActivity(), "Add to Favorite successfully", Toast.LENGTH_LONG).show();
                    toggleButton.setBackgroundColor(Color.GREEN);
                } else {
                    transaction.delete_Fav(obj.getId() + "");
                    Toast.makeText(getActivity(), "Removed from Favorite successfully", Toast.LENGTH_LONG).show();
                    toggleButton.setBackgroundColor(Color.LTGRAY);

                }
            }
        };
        toggleButton.setOnCheckedChangeListener(null);
        changeToggleStatus();
        toggleButton.setOnCheckedChangeListener(listener);

        return view;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        requestTrailersData(uri.URI + ID + uri.VIDEOS + uri.API + uri.API_KEY_VALUE);
        requestReviewsData(uri.URI + ID + uri.REVIEWS + uri.API + uri.API_KEY_VALUE);
        requestDurationData(uri.URI + ID + uri.API + uri.API_KEY_VALUE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.detail_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.action_share);
        ShareActionProvider mShareActionProvider =
                (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(createShareForecastIntent());
        } else {
            Log.d("", "Share Action Provider is null?");
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_share) {
            createShareForecastIntent();
        }

        return super.onOptionsItemSelected(item);
    }

    private Intent createShareForecastIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Check " + obj.getOriginal_title() + " Film  from Movies APP");
        return shareIntent;
    }

    private void requestTrailersData(String uri) {
        StringRequest request = new StringRequest(uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        trailers = TrailerParser.parseFeed(response);
                        updateTrailersDisplay();
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

    protected void updateTrailersDisplay() {
        TrailerAdapter adapter = new TrailerAdapter(getActivity(), R.layout.item_trailer, trailers);
        int s = trailers.size();
        if (MainActivity.mTwoPane) {
            if (s >= 2) {
                ViewGroup.LayoutParams params = trailer_listView.getLayoutParams();
                params.height = s * 140;
                trailer_listView.setLayoutParams(params);
                trailer_listView.requestLayout();
            }
        } else {
            if (s >= 2) {
                ViewGroup.LayoutParams params = trailer_listView.getLayoutParams();
                params.height = s * 200;
                trailer_listView.setLayoutParams(params);
                trailer_listView.requestLayout();
            }
        }
        trailer_listView.setAdapter(adapter);
    }

    private void requestReviewsData(String uri) {
        StringRequest request = new StringRequest(uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        reviews = ReviewParser.parseFeed(response);
                        ReviewAdapter adapter = new ReviewAdapter(getActivity(), R.layout.item_review, reviews);
                        review_ListView.setAdapter(adapter);
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

    /*protected void updateReviewsDisplay() {
        ReviewAdapter adapter = new ReviewAdapter(getActivity(), R.layout.item_review, reviews);
        review_ListView.setAdapter(adapter);
    }*/
    public void showReviews() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(true);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ((ViewGroup) review_ListView.getParent()).removeView(review_ListView);
            }
        });
        builder.setView(review_ListView);
        AlertDialog dialog = builder.create();
        dialog.show();




    }

    private void requestDurationData(String uri) {
        StringRequest request = new StringRequest(uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String x = DurationParser.parseFeed(response);
                        time.setText(x);
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

    private void changeToggleStatus() {
        String ids = transaction.get_IDs();
        String[] strValues = ids.split(",");
        ArrayList<String> aListNumbers = new ArrayList<String>(Arrays.asList(strValues));
        if (aListNumbers.contains(ID)) {
            toggleButton.setChecked(true);
            toggleButton.setBackgroundColor(Color.GREEN);
        } else {
            toggleButton.setBackgroundColor(Color.LTGRAY);
        }
    }
}
