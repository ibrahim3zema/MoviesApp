package adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ibra.moviesapp.DetailsActivityFrag;
import com.example.ibra.moviesapp.R;

import java.util.List;

import extra.uri;
import parser.MoviesParser;
import parser.TrailerParser;
import pojo.Trailer;

/**
 * Created by ibra on 4/12/2016.
 */
public class TrailerAdapter extends ArrayAdapter<Trailer> {

    Context context;
    List<Trailer> trailerList;
    TextView text_trailer;
    uri uri = new uri();
    public TrailerAdapter(Context context, int resource, List<Trailer> obj) {
        super(context, resource, obj);
        this.context = context;
        this.trailerList = obj;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_trailer, parent, false);
        Trailer trailer = trailerList.get(position);
        text_trailer=(TextView)view.findViewById(R.id.text_trailer);
        for (int i = 0; i < trailerList.size(); i++) {
            text_trailer.setText(trailer.getName());
        }
        view.setOnClickListener(new AdapterView.OnClickListener() {

            @Override
            public void onClick(View v) {
                String vidUrl = uri.VID_URL + trailerList.get(position).getKey();
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(vidUrl)));
            }
        });
        return view;
    }
}
