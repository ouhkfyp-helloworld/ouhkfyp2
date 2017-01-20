package com.example.helloworld.ouhkfyp;

/**
 * Created by leelaiyin on 19/1/2017.
 */

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.example.helloworld.ouhkfyp.fragment.TabFragment;


public class ProductMainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_main);


        initTabFragment(savedInstanceState);
    }


    private void initTabFragment(Bundle savedInstanceState){
        if (savedInstanceState == null) {
            TabFragment tabFragment = new TabFragment();

            this.getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.content_fragment, tabFragment)
                    .commit();
        }
    }

}