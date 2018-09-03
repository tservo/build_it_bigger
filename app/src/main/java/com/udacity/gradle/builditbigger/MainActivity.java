package com.udacity.gradle.builditbigger;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.android.jokelibrary.JokeActivity;
import com.example.android.jokesmith.JokeSmith;
import com.udacity.gradle.builditbigger.network.EndpointsAsyncTask;

import timber.log.Timber;

import static com.example.android.jokelibrary.JokeActivity.ARG_JOKE;


public class MainActivity extends AppCompatActivity {

    private JokeSmith mJokeSmith;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set up Timber logging
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        setContentView(R.layout.activity_main);
        mJokeSmith = new JokeSmith();
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
        // let's route to our new android library.
        EndpointsAsyncTask jokeEndpointTask = new EndpointsAsyncTask(this);
        jokeEndpointTask.execute();



        //Toast.makeText(this, mJokeSmith.getJoke(), Toast.LENGTH_SHORT).show();
    }


}
