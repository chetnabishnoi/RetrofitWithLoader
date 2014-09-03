package com.retrofit;

import android.os.Bundle;

import com.retrofit.fragments.HomeFragment;

/**
 * Created by Cbishnoi on 08-04-2014.
 */
public class HomeActivity extends AbstractActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addNewFragmentWithBackStack(new HomeFragment());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
