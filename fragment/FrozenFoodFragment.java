package com.example.helloworld.ouhkfyp.fragment;

/**
 * Created by leelaiyin on 16/1/2017.
 */

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.helloworld.ouhkfyp.R;
import com.example.helloworld.ouhkfyp.adapter.ListViewAdapterProduct;
import com.example.helloworld.ouhkfyp.json.JSONParser;
import com.example.helloworld.ouhkfyp.log.DLog;
import com.example.helloworld.ouhkfyp.tab.BaseFragment;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class FrozenFoodFragment extends BaseFragment {

    // Progress Dialog
    private ProgressDialog pDialog;
    GridView gridView;
    View view;

    ListView listView;
    ListViewAdapterProduct adapter;
    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();
    ArrayList<HashMap<String, String>> productList;
    // url to get all products list (connect to server)
    private static String url_get_frozen_food = "http://leelaiyin1993.synology.me/~ouhk/fyp/get_all_products.php";

    // JSON Node names
    static final String TAG_SUCCESS = "success";
    static final String TAG_PRODUCT = "Product"; // 大細楷, 改PATH時要改埋
    static final String TAG_PID = "product_id";
    static final String TAG_CID = "category_id";
    public static final String TAG_NAME = "name";
    static final String TAG_DESCRIPTION = "description";
    public static final String TAG_IMAGE = "image";
    static final String TAG_ORIGIN = "origin";

    // products JSONArray
    JSONArray product = null;


    String cuisine;
    String c1;
    String district;
    String clist="";

    String page = "none";

    private static final String DATA_NAME = "name";

    private String title = "";

    public static FrozenFoodFragment newInstance(String title, int indicatorColor,
                                                 int dividerColor, int iconResId) {

        DLog.d("FrozenFoodFragment - newInstance");
        FrozenFoodFragment f = new FrozenFoodFragment();
        f.setTitle(title);
        f.setIndicatorColor(indicatorColor);
        f.setDividerColor(dividerColor);
        f.setIconResId(iconResId);


        //pass data
        Bundle args = new Bundle();
        args.putString(DATA_NAME, title);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        DLog.d("FrozenFoodFragment - onActivityCreated");
        //listView = (ListView) view.findViewById(R.id.product_listview);
        // ArrayList<ProfileNews> data =new ArrayList<ProfileNews>();
        // data.add(new ProfileNews("","Test List item"));
        // data.add(new ProfileNews("","Wow wow wow"));
        // data.add(new ProfileNews("","Great Good Nice"));
        // data.add(new ProfileNews("","Yeah~~~~~~~~~~~~~~~~"));
        //NewsAdapter na = new NewsAdapter(getActivity(),R.layout.listview_profile_news,data);
        //listView.setAdapter(na);
        //uid = AppManager.watchDetailProfile.uid;

        productList = new ArrayList<HashMap<String, String>>();

        new LoadAllProduct().execute();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DLog.d("FrozenFoodFragment - onCreate");

        //get data
        title = getArguments().getString(DATA_NAME);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        DLog.d("FrozenFragment - onCreateView");

        //layout
        view = inflater.inflate(R.layout.frg_itemlist, container, false);

        //view
//        TextView txtName = (TextView) view.findViewById(R.id.txtName);
        //      txtName.setText(title);

        return view;
    }

    class LoadAllProduct extends AsyncTask<String, String, String> {

        int success;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Chilled & Frozen Food. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            // pDialog.show();
        }

        protected String doInBackground(String... args) {

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("cid", "2"));
            //Log.e("USER_ID: ", uid);
            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(url_get_frozen_food, "GET", params);

            // Check log cat for JSON reponse
            Log.d("frozen food product: ", json.toString());

            try {
                // Checking for SUCCESS TAG
                success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // restaurants found
                    // Getting Array of bookmark
                    product = json.getJSONArray(TAG_PRODUCT);

                    // looping through All Products
                    for (int i = 0; i < product.length(); i++) {
                        JSONObject c = product.getJSONObject(i);

                        // Storing each json item in variable
                        String product_id = c.getString("product_id");
                        String name = c.getString("name");
                        String description = c.getString("description");
                        String image = "http://leelaiyin1993.synology.me/~ouhk/images/product/" + c.getString("image");
                        String price = c.getString("price");


                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        map.put("product_id", product_id);
                        //map.put("uid", uid);
                        map.put("name", name);
                        map.put("description", description);
                        map.put("image", image);
                        map.put("price", price);

                        // adding HashList to ArrayList
                        productList.add(map);
                    }
                } else {
                    pDialog.dismiss();
                    Log.e("No Product","");

                    /*
                    // no retaruant(s) found
                    // Launch Add New product Activity
                    Intent i = new Intent(getApplicationContext(),
                            NewProductActivity.class);
                    // Closing all previous activities
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    */
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/

        protected void onPostExecute(String file_url) {

            if(success == 1){
                // dismiss the dialog after getting all restaurants
                pDialog.dismiss();
                // Locate the listview in listview_main.xml
                gridView = (GridView)view.findViewById(R.id.product_gridView);
                // Pass the results into ListViewAdapterResataurant.java
                adapter = new ListViewAdapterProduct(getActivity(), productList);
                // Set the adapter to the ListView
                gridView.setAdapter(adapter);
            }else{
                pDialog.dismiss();
                Toast.makeText(getActivity(), "No friend list in your list", Toast.LENGTH_SHORT).show();
            }

        }

    }



    @Override
    public void onDestroy() {
        DLog.d("FrozenFoodFragment - onDestroy");
        super.onDestroy();
    }



    @Override
    public void onDestroyView() {
        DLog.d("FrozenFoodFragment - onDestroyView");
        super.onDestroyView();
    }



}

