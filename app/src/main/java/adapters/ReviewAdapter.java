package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.ibra.moviesapp.R;

import java.util.List;

import pojo.Review;

/**
 * Created by ibra on 4/12/2016.
 */
public class ReviewAdapter extends ArrayAdapter<Review> {
    Context context;
    List<Review> reviewList;
    TextView author, comment;
    Review review;

    public ReviewAdapter(Context context, int resource, List<Review> obj) {
        super(context, resource, obj);
        this.context = context;
        this.reviewList = obj;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_review, parent, false);
        review = reviewList.get(position);
        author = (TextView) view.findViewById(R.id.author);
        comment = (TextView) view.findViewById(R.id.comment);
        for (int i = 0; i < reviewList.size(); i++) {
            author.setText(review.getName());
            comment.setText(review.getComment());
        }
        return view;
    }
}
