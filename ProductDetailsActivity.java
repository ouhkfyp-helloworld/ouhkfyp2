package com.example.helloworld.ouhkfyp;

/**
 * Created by leelaiyin on 16/1/2017.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.helloworld.ouhkfyp.adapter.ListViewAdapterRelatedProduct;
import com.example.helloworld.ouhkfyp.json.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//import com.library.DatabaseHandler;

public class ProductDetailsActivity extends Activity {

    TextView tvRestaurantId;
    TextView tvName;
    TextView tvBrand;
    TextView tvPrice;
    ListView related_product_listview;

    TextView tvPhone;
    TextView tvAddress;
    TextView tvOpen;
    EditText etComment;
    ImageView rImage;
    ImageView imageView_trend;

    //    ListView lv;
    String rid;
    String product_id;
    String grade;
    LinearLayout info_Bg,gallery_Bg,map_Bg,comment_Bg;
    ImageView info_Btn,gallery_Btn,map_Btn,comment_Btn,makecomment_Btn,rate_Btn;
    String uid;
    ImageView btnBookmark;
    boolean btnBookmarkBo = false;
    public int success;
    ListViewAdapterRelatedProduct adapter;

    // List for the Product comment
    ArrayList<HashMap<String, String>> CommentList;
    ArrayList<HashMap<String, String>> RelatedProductList;



    final Context context = this;
    Bitmap bitmap;
    final Context c = this;
    // Progress Dialog
    private ProgressDialog pDialog;
    private ProgressDialog pDialog2;

    // JSON parser class
    JSONParser jsonParser = new JSONParser();

    // single restaurant url (connect to server)
    private static final String url_product_details = "http://leelaiyin1993.synology.me/~ouhk/fyp/get_product_detail.php";
    // url to edit the bookmark
    private static String url_check_bookmark = "http://leelaiyin1993.synology.me/~ouhk/checkBookmark.php";
    private static String url_add_bookmark = "http://leelaiyin1993.synology.me/~ouhk/addBookmark.php";
    private static String url_del_bookmark = "http://leelaiyin1993.synology.me/~ouhk/delBookmark.php";
    private static String url_new_comment = "http://leelaiyin1993.synology.me/~ouhk/create_restaurant_comment.php";
    private static String url_rate_restaurant = "http://leelaiyin1993.synology.me/~ouhk/rating_restaurant.php";
    // JSON Node names
    private static final String TAG_DISCOUNT = "Discount";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_RESTAURANT = "Restaurant";
    private static final String TAG_RID = "RID";
    private static final String TAG_NAME = "Name";
    private static final String TAG_PHONE = "Phone";
    private static final String TAG_ADDRESS = "Address";
    private static final String TAG_OPEN = "Open";
    private static final String TAG_IMAGE = "Image";
    public static final String TAG_GRADE = "Grade";

    // comment JSONArray
    JSONArray comment = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        //initActionBar();

        // For phone which version is 4.0 or above
        // Solved android.os.NetworkOnMainThreadException
        // ref: http://kuosun.blogspot.hk/2013/12/androidosnetworkonmainthreadexception.html
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
//          tvRestaurantId = (TextView) findViewById(R.id.tvDetailResID);
        tvName = (TextView) findViewById(R.id.tvName);
        tvBrand = (TextView) findViewById(R.id.tvBrand);
        tvPrice = (TextView) findViewById(R.id.tvPrice);
        rImage = (ImageView) findViewById(R.id.imageView_product);
        imageView_trend = (ImageView) findViewById(R.id.imageView_trend);
        related_product_listview = (ListView) findViewById(R.id.related_product_listview);

        RelatedProductList = new ArrayList<HashMap<String, String>>();
        if(AppManager.user != null)
            uid = AppManager.user.uid;

        Log.e("user id: ", " "+uid);
        /*
        tvName = (TextView) findViewById(R.id.restDetailName_tv);

        rImage = (ImageView) findViewById(R.id.restDetailPhoto_Img);

        info_Bg = (LinearLayout)findViewById(R.id.restDetailInfo_bg);
        gallery_Bg = (LinearLayout)findViewById(R.id.restDetailGallery_bg);
        map_Bg = (LinearLayout)findViewById(R.id.restDetailMap_bg);
        comment_Bg = (LinearLayout)findViewById(R.id.restDetailComment_bg);

        info_Btn = (ImageView)findViewById(R.id.restDetailInfo_Btn);
        gallery_Btn = (ImageView)findViewById(R.id.restDetailGallery_Btn);
        map_Btn = (ImageView)findViewById(R.id.restDetailMap_Btn);
        comment_Btn = (ImageView)findViewById(R.id.restDetailComment_Btn);
        btnBookmark = (ImageView)findViewById(R.id.restDetailBookmark);
        makecomment_Btn = (ImageView)findViewById(R.id.makecomment_Btn);
        rate_Btn = (ImageView)findViewById(R.id.rate_Btn);
        AppManager.detailRestaurantNumber = 0;
        */

        // Bookmark button
        /*
        btnBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AppManager.user == null){
                    Toast.makeText(getApplicationContext(), "Please Login first", Toast.LENGTH_SHORT).show();
                }else{
                    uid = AppManager.user.uid;
                    if (btnBookmarkBo == false){
                        new AddToMyBookmark().execute();
                    }else{
                        new DelFromMyBookmark().execute();
                    }
                }
            }
        });
        makecomment_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AppManager.user == null){
                    Toast.makeText(getApplicationContext(), "Please Login first", Toast.LENGTH_SHORT).show();
                }else{
                    uid = AppManager.user.uid;
                    final Dialog dialog = new Dialog(context);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable((Color.TRANSPARENT)));
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.activity_new_comment);

                    dialog.setCanceledOnTouchOutside(true);
                    TextView etRestaurantId = (TextView)dialog.findViewById(R.id.etRestaurantId);
                    TextView etRestaurantName = (TextView)dialog.findViewById(R.id.etRestaurantName);
                    etRestaurantId.setText(AppManager.watchDetailRestaurant.rid);
                    etRestaurantName.setText(AppManager.watchDetailRestaurant.name);

                    etComment = (EditText)dialog.findViewById(R.id.etComment);
                    Button btnAddComment = (Button)dialog.findViewById(R.id.btnAddComment);
                    ImageView btnClose = (ImageView)dialog.findViewById(R.id.btnClose);

                    btnAddComment.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            new CreateNewComment().execute();
                            dialog.dismiss();
                        }
                    });
                    btnClose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });

                    dialog.show();
                }

            }
        });
        rate_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AppManager.user == null){
                    Toast.makeText(getApplicationContext(), "Please Login first", Toast.LENGTH_SHORT).show();
                }else{
                    uid = AppManager.user.uid;

                    final Dialog dialog = new Dialog(context);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable((Color.TRANSPARENT)));
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.activity_rate_restaurant);

                    dialog.setCanceledOnTouchOutside(true);
                    TextView etRestaurantId = (TextView)dialog.findViewById(R.id.etRestaurantId);
                    TextView etRestaurantName = (TextView)dialog.findViewById(R.id.etRestaurantName);
                    etRestaurantId.setText(AppManager.watchDetailRestaurant.rid);
                    etRestaurantName.setText(AppManager.watchDetailRestaurant.name);

                    Spinner spinner = (Spinner) dialog.findViewById(R.id.spinner_mark);
                    ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_item,new String[]{"1","2","3","4","5","6","7","8","9","10"});
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);
                    spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
                        public void onItemSelected(AdapterView adapterView, View view, int position, long id){
                            grade = adapterView.getSelectedItem().toString();
                            Toast.makeText(RestaurantDetails.this, "您選擇"+adapterView.getSelectedItem().toString(), Toast.LENGTH_LONG).show();

                        }
                        public void onNothingSelected(AdapterView arg0) {
                            Toast.makeText(RestaurantDetails.this, "您沒有選擇任何項目", Toast.LENGTH_LONG).show();
                        }
                    });
                    Button btnRate = (Button)dialog.findViewById(R.id.btnRate);
                    ImageView btnClose = (ImageView)dialog.findViewById(R.id.btnClose);

                    btnRate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            new RateRestaurant().execute();
                            dialog.dismiss();
                        }
                    });
                    btnClose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });

                    dialog.show();
                }

            }
        });
        */



        /*

         // Hashmap to load data from the Sqlite database (get the user id)

        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        HashMap<String,String> user = new HashMap<String, String>();
        user = db.getUserDetails();
        System.out.println("EditProduct page" + user.get("name") + " : " + user.get("uid"));
        uid = user.get("uid");
        */

        // Hashmap for ListView
        CommentList = new ArrayList<HashMap<String, String>>();

        // listview
//        lv = (ListView) findViewById(R.id.commentlist);

        // getting restaurant details from intent
        Intent i = getIntent();
        // getting restaurant  id (rid) from intent
        product_id = i.getStringExtra("product_id");
        Log.e("Product ID: ", product_id);

        // Getting complete restaurant details in background thread
        new GetProductDetails().execute();

        imageView_trend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PriceTrendActivity.class);
                intent.putExtra("product_id", product_id);
                context.startActivity(intent);

            }
        });

    }

    /**
     * Background Async Task to Get complete restaurant details
     */
    class GetProductDetails extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ProductDetailsActivity.this);
            pDialog.setMessage("Loading restaurant details. Please wait...");
            pDialog.setTitle("Checking Network");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Getting restaurant details in background thread
         */
        protected String doInBackground(String... args) {

            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    // Check for success tag

                    try {
                        // Building Parameters
                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("product_id", product_id));

                        // getting restairamt details by making HTTP request
                        // Note that restaurant details url will use GET request
                        JSONObject json = jsonParser.makeHttpRequest(
                                url_product_details, "GET", params);

                        // check your log for json response
                        Log.d("product_Details", json.toString());

                        // json success tag
                        success = json.getInt(TAG_SUCCESS);
                        if (success == 1) {
                            // successfully received restaurant details
                            JSONArray resObj = json
                                    .getJSONArray("Product"); // JSON Array
                            // get first restaurant object from JSON Array
                            JSONObject product = resObj.getJSONObject(0);

                            String product_id = product.optString("product_id","");
                            String category_id = product.optString("category_id", "");
                            String name = product.optString("name","");
                            String description = product.optString("description","");
                            String brand = product.optString("brand","");
                            String origin = product.optString("origin","Close");
                            String image = "http://leelaiyin1993.synology.me/~ouhk/images/product/" + product.optString("image", "");
                            String numOfView = product.optString("numOfView", "0");

                            Product now_product = new Product(product_id, category_id, name, description, brand, origin, image, numOfView);
                            //AppManager.watchDetailRestaurant = now_product;

                            JSONArray rproductObj = json.getJSONArray("Related_Product");
                            for (int i = 0; i < rproductObj.length(); i++) {
                                JSONObject c = rproductObj.getJSONObject(i);

                                // Storing each json item in variable
                                String related_product_product_id = c.optString("product_id","");
                                String related_product_category_id = c.optString("category_id", "");
                                String related_product_name = c.optString("name","");
                                String related_product_description = c.optString("description","");
                                String related_product_brand = c.optString("brand","");
                                String related_product_origin = c.optString("origin","Close");
                                String related_product_image = "http://leelaiyin1993.synology.me/~ouhk/images/product/" + c.optString("image", "");
                                String related_product_numOfView = c.optString("numOfView", "0");

                                // creating new HashMap
                                HashMap<String, String> map = new HashMap<String, String>();

                                // adding each child node to HashMap key => value
                                map.put("related_product_product_id", related_product_product_id);
                                map.put("related_product_category_id", related_product_category_id);
                                map.put("related_product_name", related_product_name);
                                map.put("related_product_description", related_product_description);
                                map.put("related_product_brand", related_product_brand);
                                map.put("related_product_origin", related_product_origin);
                                map.put("related_product_image", related_product_image);
                                map.put("related_product_numOfView", related_product_numOfView);

                                // adding HashList to ArrayList
                                RelatedProductList.add(map);
                            }
                            /*
                            if (AppManager.user != null){

                                uid = AppManager.user.uid;
                                // Check whether the user add the product to bookmark list
                                List<NameValuePair> param = new ArrayList<NameValuePair>();
                                param.add(new BasicNameValuePair("uid", uid));
                                param.add(new BasicNameValuePair("rid", restaurant.getString(TAG_RID)));
                                Log.d("restaurant_id", restaurant.getString(TAG_RID));
                                Log.d("User_id", uid);
                                JSONObject jsonBookmark = jsonParser.makeHttpRequest(url_check_bookmark, "GET", param);

                                int successBookmark = jsonBookmark.getInt(TAG_SUCCESS);
                                Log.e("Success_or_not", "" + successBookmark);
                                if (successBookmark == 1){
                                    btnBookmark.setImageResource(R.mipmap.restdetail_bookmarked);
                                    btnBookmarkBo = true;
                                }else{
                                    btnBookmark.setImageResource(R.mipmap.restdetail_bookmark);
                                    btnBookmarkBo = false;
                                }
                            }
                            // Check whether the user add the product to bookmark list
                            List<NameValuePair> param = new ArrayList<NameValuePair>();
                            param.add(new BasicNameValuePair("uid", uid));
                            param.add(new BasicNameValuePair("rid", restaurant.getString(TAG_RID)));
                            Log.d("restaurant_id", restaurant.getString(TAG_RID));
                            JSONObject jsonBookmark = jsonParser.makeHttpRequest(url_check_bookmark, "GET", param);

                            int successBookmark = jsonBookmark.getInt(TAG_SUCCESS);
                            Log.e("Success_or_not", "" + successBookmark);
                            if (successBookmark == 1){
                                btnBookmark.setImageResource(R.mipmap.restdetail_bookmarked);
                                btnBookmarkBo = true;
                            }else{
                                btnBookmark.setImageResource(R.mipmap.restdetail_bookmark);
                                btnBookmarkBo = false;
                            }
                            */

                            // display restaurant data in EditText
//                            tvRestaurantId.setText(rid);
                            tvBrand.setText(brand);
                            tvName.setText(name);

//                            tvAddress.setText(address);
//                            tvPhone.setText(phone);
//                            tvOpen.setText(open);
                            new LoadImage().execute(image);
                            //changeFragment(RestaurantInfoFragment.newInstance());

                        } else {
                            // restaurant with rid not found
                            Log.e("Res_not_found", "");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            return null;
        }


        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once got all details
            pDialog.dismiss();

            if(success == 1){
                // dismiss the dialog after getting all restaurants
                pDialog.dismiss();
                // Locate the listview in listview_main.xml
                related_product_listview = (ListView) findViewById(R.id.related_product_listview);
                // Pass the results into ListViewAdapterRelatedProduct.java
                adapter = new ListViewAdapterRelatedProduct(context, RelatedProductList);
                // Set the adapter to the ListView
                related_product_listview.setAdapter(adapter);
            }else{
                pDialog.dismiss();
                Toast.makeText(context, "No related product in your list", Toast.LENGTH_SHORT).show();
            }

        }
    }


    /*
    // Add restaurant to bookmark list
    class AddToMyBookmark extends AsyncTask<String, String, String>{
        int success_Add_Bookmark = 0;
        protected void onPreExecute(){
            super.onPreExecute();
        }

        protected  String doInBackground(String... params){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try{
                        List<NameValuePair> param = new ArrayList<NameValuePair>();
                        param.add(new BasicNameValuePair("uid", uid));
                        param.add(new BasicNameValuePair("rid", rid));

                        JSONObject jsonAddBookmark = jsonParser.makeHttpRequest(url_add_bookmark, "GET", param);
                        success_Add_Bookmark = jsonAddBookmark.getInt(TAG_SUCCESS);
                        Log.e ("success_bookmark", ""+success_Add_Bookmark);
                        if(success_Add_Bookmark == 1){
                            btnBookmark.setImageResource(R.mipmap.restdetail_bookmarked);
                            btnBookmarkBo = true;
                        }else{
                            btnBookmark.setImageResource(R.mipmap.restdetail_bookmark);
                            btnBookmarkBo = false;
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

            });
            return null;
        }

        protected void onPostExecute(String file_url){


            Toast.makeText(getApplicationContext(), "added to list",Toast.LENGTH_SHORT).show();
        }
    }

    // Remove restaurant from bookmark list
    class DelFromMyBookmark extends AsyncTask<String, String, String>{
        int success_Del_Bookmark = 0;
        protected void onPreExecute(){
            super.onPreExecute();
        }

        protected  String doInBackground(String... params){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try{
                        List<NameValuePair> param = new ArrayList<NameValuePair>();
                        param.add(new BasicNameValuePair("uid", uid));
                        param.add(new BasicNameValuePair("rid", rid));

                        JSONObject jsonDelBookmark = jsonParser.makeHttpRequest(url_del_bookmark, "GET", param);
                        success_Del_Bookmark = jsonDelBookmark.getInt(TAG_SUCCESS);
                        Log.e ("success_del_bookmark", ""+success_Del_Bookmark);
                        if(success_Del_Bookmark == 1){
                            btnBookmark.setImageResource(R.mipmap.restdetail_bookmark);
                            btnBookmarkBo = false;
                        }else{
                            btnBookmark.setImageResource(R.mipmap.restdetail_bookmarked);
                            btnBookmarkBo = true;
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

            });
            return null;
        }

        protected void onPostExecute(String file_url){


            Toast.makeText(getApplicationContext(), "Deleted from list",Toast.LENGTH_SHORT).show();
        }
    }
    class CreateNewComment extends AsyncTask<String, String, String> {

        int success;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(RestaurantDetails.this);
            pDialog.setMessage("Adding Comment ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }


        protected String doInBackground(String... args) {

            String newComment = etComment.getText().toString();


            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("User_UID", uid));
            params.add(new BasicNameValuePair("Restaurant_RID", rid));
            params.add(new BasicNameValuePair("Comment", newComment));


            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_new_comment,
                    "POST", params);

            // check log cat fro response
            Log.d("Create Response", json.toString());

            // check for success tag
            try {
                success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // successfully created product

                    Intent i = new Intent(getApplicationContext(), RestaurantDetails.class);
                    i.putExtra("TAG_RID", rid);
                    System.out.println("passing value is:"+ rid);
                    finish();
                    startActivity(getIntent());

                    // closing this screen
//                    Toast.makeText(RestaurantDetails.this,json.getString("message"),Toast.LENGTH_LONG).show();


                } else {
                    // failed to create comment
//                    Toast.makeText(RestaurantDetails.this,"Failed to create comment",Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }


        protected void onPostExecute(String file_url) {
            if(success == 1){
                Toast.makeText(RestaurantDetails.this,"Comment created",Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(RestaurantDetails.this,"Failed to created comment",Toast.LENGTH_LONG).show();
            }
            // dismiss the dialog once done
            pDialog.dismiss();
            // Custom toast

        }

    }

    class RateRestaurant extends AsyncTask<String, String, String> {

        int success;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(RestaurantDetails.this);
            pDialog.setMessage("Rating ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        protected String doInBackground(String... args) {

//            grade = "10";

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("RID", rid));
            params.add(new BasicNameValuePair("Grade", grade));
            System.out.println("passing rid is:"+ rid + "the grade is: " + grade );


            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_rate_restaurant,
                    "POST", params);

            // check log cat fro response
            Log.d("Create Response", json.toString());

            // check for success tag
            try {
                success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // successfully created product

                    Intent i = new Intent(getApplicationContext(), RestaurantDetails.class);
                    i.putExtra("TAG_RID", rid);
                    finish();
                    startActivity(getIntent());

                    // closing this screen
//                    Toast.makeText(RestaurantDetails.this,json.getString("message"),Toast.LENGTH_LONG).show();


                } else {
                    // failed to create comment
//                    Toast.makeText(RestaurantDetails.this,"Failed to create comment",Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String file_url) {
            if(success == 1){
                Toast.makeText(RestaurantDetails.this,"Restaurant rated",Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(RestaurantDetails.this,"Failed to rated",Toast.LENGTH_LONG).show();
            }
            // dismiss the dialog once done
            pDialog.dismiss();
            // Custom toast

        }

    }*/

    private class LoadImage extends AsyncTask<String, String, Bitmap> {
        @Override

        protected Bitmap doInBackground(String... args) {
            try {
                bitmap = BitmapFactory.decodeStream((InputStream) new URL(args[0]).getContent());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap image) {
            if (image != null) {
                rImage.setImageBitmap(image);
                pDialog.dismiss();
            } else {
                pDialog.dismiss();
                Toast.makeText(ProductDetailsActivity.this, "Image Does Not exist or Network Error", Toast.LENGTH_SHORT).show();
            }
        }
    }
    /*
    public void changeFragment(android.support.v4.app.Fragment f){
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.RestDetailPageFragment, f);
        //transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
        //transaction.commit();

    }

    public void changeItemImg(){
        if(AppManager.detailRestaurantNumber==0){
            info_Bg.setBackgroundResource( R.mipmap.restdetail_iconroundedrectangle);
            gallery_Bg.setBackgroundColor(Color.TRANSPARENT);
            map_Bg.setBackgroundColor(Color.TRANSPARENT);
            comment_Bg.setBackgroundColor(Color.TRANSPARENT);
        }
        if(AppManager.detailRestaurantNumber==1){
            info_Bg.setBackgroundColor(Color.TRANSPARENT);
            gallery_Bg.setBackgroundResource(R.mipmap.restdetail_iconroundedrectangle);
            map_Bg.setBackgroundColor(Color.TRANSPARENT);
            comment_Bg.setBackgroundColor(Color.TRANSPARENT);
        }
        if(AppManager.detailRestaurantNumber==2){
            info_Bg.setBackgroundColor(Color.TRANSPARENT);
            gallery_Bg.setBackgroundColor(Color.TRANSPARENT);
            map_Bg.setBackgroundResource(R.mipmap.restdetail_iconroundedrectangle);
            comment_Bg.setBackgroundColor(Color.TRANSPARENT);
        }
        if(AppManager.detailRestaurantNumber==3){
            info_Bg.setBackgroundColor(Color.TRANSPARENT);;
            gallery_Bg.setBackgroundColor(Color.TRANSPARENT);
            map_Bg.setBackgroundColor(Color.TRANSPARENT);
            comment_Bg.setBackgroundResource(R.mipmap.restdetail_iconroundedrectangle);
        }

    }*/

}