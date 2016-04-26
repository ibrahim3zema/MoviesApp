package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.ibra.moviesapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import extra.uri;
import pojo.Movies;

/**
 * Created by ibra on 3/25/16.
 */
public class MovieAdapter extends BaseAdapter {
    Context mconext;
    private final LayoutInflater Inflater;
    public ArrayList<Movies> arraylist;

    public MovieAdapter(Context context, ArrayList<Movies> arraylist) {
        this.Inflater = LayoutInflater.from(context);
        this.arraylist = arraylist;
        this.mconext = context;
    }

    @Override
    public int getCount() {
        return arraylist.size();
    }

    @Override
    public Object getItem(int position) {
        return arraylist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder {
        ImageView imageView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = Inflater.inflate(R.layout.item_movie, null);
            holder.imageView = (ImageView) convertView.findViewById(R.id.imageView1);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Movies movie = arraylist.get(position);
        Picasso.with(mconext).load(uri.PATH + movie.getPoster_path()).into(holder.imageView);
        return convertView;

    }
}
