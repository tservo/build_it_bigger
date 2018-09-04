package com.udacity.gradle.builditbigger;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import timber.log.Timber;
import com.udacity.gradle.builditbigger.network.EndpointsAsyncTask;


public class MainActivity extends AppCompatActivity {


    @Nullable
    private CountingIdlingResource mIdlingResource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set up Timber logging
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        setContentView(R.layout.activity_main);

        getIdlingResource();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void tellJoke(View view) {
        // let's make the call to our endpoints server.
        EndpointsAsyncTask jokeEndpointTask = new EndpointsAsyncTask(this, getIdlingResource());
        jokeEndpointTask.execute();



        //Toast.makeText(this, mJokeSmith.getJoke(), Toast.LENGTH_SHORT).show();
    }

    /**
     *
     * @return the Idling Resource
     */
    @VisibleForTesting
    @NonNull
    public CountingIdlingResource getIdlingResource() {
        Timber.i("getSimpleIdlingResource()");
        if (null == mIdlingResource) {
            mIdlingResource = new CountingIdlingResource("MainActivityAsyncTask");
        }

        return mIdlingResource;
    }

}


