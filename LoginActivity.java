package com.example.helloworld.ouhkfyp;

/**
 * Created by leelaiyin on 19/1/2017.
 */


import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.helloworld.ouhkfyp.json.JSONParser;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class LoginActivity extends FragmentActivity {

    EditText email,password;
    Button btnRegisterPage, btnLogin;
    TextView.OnEditorActionListener te;
    ProgressBar pr;

    JSONParser jsonParser = new JSONParser();
    private static final String url_login = "http://leelaiyin1993.synology.me/~ouhk/fyp/login.php";
    int success;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        pr = (ProgressBar)findViewById(R.id.LoginRegister_ProgressBar);
        btnRegisterPage = (Button)findViewById(R.id.btnRegisterPage);
        email = (EditText)findViewById(R.id.etEmail);
        password = (EditText)findViewById(R.id.etPassword);
        btnLogin = (Button)findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Login().execute();
            }
        });

//        btnRegisterPage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent myIntent = new Intent(LoginActivity.this, Register.class);
//                myIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                LoginOrRegister.this.startActivity(myIntent);
//            }
//        });



    }
    class Login extends AsyncTask<String, String, String> {

        String email_str = "";
        String password_str = "";

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            email_str = email.getText().toString();
            password_str = password.getText().toString();
            if(AppManager.loading)return;
            if(email_str.equals("")||password_str.equals(""))return;
            AppManager.loading = true;
            pr.setVisibility(View.VISIBLE);

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
                        params.add(new BasicNameValuePair("email", email_str));
                        params.add(new BasicNameValuePair("password", password_str));

                        // getting restairamt details by making HTTP request
                        // Note that restaurant details url will use GET request
                        JSONObject json = jsonParser.makeHttpRequest(
                                url_login, "GET", params);

                        // check your log for json response
                        Log.d("login", json.toString());

                        // json success tag
                        success = json.getInt("success");

                        if(success==0) {
                            String error_msg = "";
                            try{
                                error_msg = json.getString("error_msg");
                            }catch(Exception e){};
                            AppManager.loading = false;
                            pr.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(),"Login Error",Toast.LENGTH_SHORT).show();
                            password.setText("");
                            email.setText("");
                        }else if(success==1){
                            //login success

                            //pr.setVisibility(View.GONE);
                            Log.e("login",json.toString());
                            String email="";
                            String user_id="";
                            String reg_date="";
                            try{

                                JSONObject user_obj = json.getJSONObject("user");
                                email = user_obj.optString("email","");
                                user_id = user_obj.optString("user_id","");
                                //System.out.println("UID is"+ user_obj.getString("uid"));
                                reg_date = user_obj.optString("reg_date","");
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            Toast.makeText(getApplicationContext(),"Login successfully",Toast.LENGTH_SHORT).show();
                            AppManager.user = new User(email,user_id, reg_date);
                            Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);
                            LoginActivity.this.startActivity(myIntent);
                            LoginActivity.this.finish();
                            AppManager.loading = false;
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


        }
    }




}
