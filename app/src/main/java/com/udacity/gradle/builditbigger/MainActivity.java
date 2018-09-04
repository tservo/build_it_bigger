package com.udacity.gradle.builditbigger;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.android.jokelibrary.JokeActivity;
import com.example.android.jokesmith.JokeSmith;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.udacity.gradle.builditbigger.IdlingResource.SimpleIdlingResource;
import com.udacity.gradle.builditbigger.backend.jokeApi.JokeApi;

import java.io.IOException;
import java.lang.ref.WeakReference;

import timber.log.Timber;

import static com.example.android.jokelibrary.JokeActivity.ARG_JOKE;


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
        // let's route to our new android library.



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

class EndpointsAsyncTask extends AsyncTask<Void, Void, String> {
    private static JokeApi jokeApiService = null;
    private WeakReference<Context> wContext;
    private CountingIdlingResource mIdlingResource;

    public EndpointsAsyncTask(Context context, CountingIdlingResource idlingResource) {
        // avoid memory leak by being extra nice about holding the context
        wContext = new WeakReference<>(context);
        // and for testing let's hold on to this
        mIdlingResource = idlingResource;
        mIdlingResource.increment();
    }
    @Override
    protected String doInBackground(Void... params) {

        Timber.d("Hello from AsyncTask");

        if(jokeApiService == null) {  // Only do this once
            JokeApi.Builder builder = new JokeApi.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null)
                    // options for running against local devappserver
                    // - 10.0.2.2 is localhost's IP address in Android emulator
                    // - turn off compression when running against local devappserver
                    .setRootUrl("http://10.0.2.2:8080/_ah/api/")
                    .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                        @Override
                        public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                            abstractGoogleClientRequest.setDisableGZipContent(true);
                        }
                    });
            // end options for devappserver

            jokeApiService = builder.build();
        }

        try {
            return jokeApiService.tellJoke().execute().getJoke();
        } catch (IOException e) {
            return e.getMessage();
        } finally {
            mIdlingResource.decrement();
        }
    }

    @Override
    protected void onPostExecute(String result) {
        // let's see if we have a context to work with
        Context context = wContext.get();
        if (null != context) {
            Intent i = new Intent(context, JokeActivity.class);
            i.putExtra(ARG_JOKE, result);
            context.startActivity(i);
        }
    }
}
