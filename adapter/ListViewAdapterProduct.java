package com.example.helloworld.ouhkfyp.adapter;

/**
 * Created by leelaiyin on 16/1/2017.
 */

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.helloworld.ouhkfyp.LoadImage.ImageLoader;
import com.example.helloworld.ouhkfyp.ProductDetailsActivity;
import com.example.helloworld.ouhkfyp.R;
import com.example.helloworld.ouhkfyp.fragment.FreshFoodFragment;

import java.util.ArrayList;
import java.util.HashMap;
//import com.LoadImage.MainActivity;

public class ListViewAdapterProduct extends BaseAdapter {

    // Declare Variables
    Context context;
    LayoutInflater inflater;
    ArrayList<HashMap<String, String>> data;
    ImageLoader imageLoader;
    HashMap<String, String> resultp = new HashMap<String, String>();

    public ListViewAdapterProduct(Context context,
                                     ArrayList<HashMap<String, String>> arraylist) {
        this.context = context;
        data = arraylist;
        imageLoader = new ImageLoader(context);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    public View getView(final int position, View convertView, ViewGroup parent) {
        // Declare Variables
        TextView name;
        TextView price;
        TextView cuisine;
        TextView ranking;
        ImageView rImage;
        ImageView rankImage;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.list_view_product, parent, false);
        // Get the position
        resultp = data.get(position);

        // Locate the TextViews in listview_item.xml
        name = (TextView) itemView.findViewById(R.id.tvName);
        price = (TextView) itemView.findViewById(R.id.tvPrice);
        // cuisine = (TextView) itemView.findViewById(R.id.tvCuisine);
        // ranking = (TextView) itemView.findViewById(R.id.tvRanking);

        // Locate the ImageView in listview_item.xml
        rImage = (ImageView) itemView.findViewById(R.id.productImage);
        // rankImage = (ImageView) itemView.findViewById(R.id.ranking);

        // Capture position and set results to the TextViews
        name.setText(resultp.get("name"));
        price.setText("HKD:" + resultp.get("price"));

        // Capture position and set results to the ImageView
        // Passes flag images URL into ImageLoader.class
        imageLoader.DisplayImage(resultp.get(FreshFoodFragment.TAG_IMAGE), rImage);

        // Capture ListView item click (Later to modify for restaurant details)

        itemView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // Get the position
                resultp = data.get(position);
                System.out.println("The data postion is : " + position);
                System.out.println("The product id is: " + resultp.get("product_id"));
                Intent intent = new Intent(context, ProductDetailsActivity.class);
                intent.putExtra("product_id", resultp.get("product_id"));

                // Start SingleItemView Class

                context.startActivity(intent);

            }
        });

        return itemView;
    }
}