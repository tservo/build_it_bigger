package com.example.android.jokelibrary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class JokeActivity extends AppCompatActivity {

    public static final String ARG_JOKE="joke";

    private TextView mJokeText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joke);

        mJokeText = findViewById(R.id.joke_text);

        Intent i = getIntent();
        String joke = i.getStringExtra(ARG_JOKE);
        if (null == joke) {
            joke = getString(R.string.NO_JOKE);
        }

        mJokeText.setText(joke);
    }
}
