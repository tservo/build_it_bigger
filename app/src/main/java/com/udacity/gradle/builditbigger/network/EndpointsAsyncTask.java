package com.udacity.gradle.builditbigger.network;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.test.espresso.idling.CountingIdlingResource;

import com.example.android.jokelibrary.JokeActivity;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.udacity.gradle.builditbigger.backend.jokeApi.JokeApi;

import java.io.IOException;
import java.lang.ref.WeakReference;

import timber.log.Timber;

import static com.example.android.jokelibrary.JokeActivity.ARG_JOKE;

public class EndpointsAsyncTask extends AsyncTask<Void, Void, String> {
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
