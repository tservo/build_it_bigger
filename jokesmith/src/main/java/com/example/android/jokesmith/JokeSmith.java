package com.example.android.jokesmith;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Random;
import java.util.ResourceBundle;

public class JokeSmith {

    private ArrayList<String> mJokeList;
    private Random mGenerator;

    public JokeSmith() {
        mJokeList = new ArrayList<>();
        mGenerator = new Random();
        ResourceBundle bundle = ResourceBundle.getBundle("Jokes", Locale.getDefault());
        Enumeration<String> keys = bundle.getKeys();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            mJokeList.add(bundle.getString(key));
        }

    }
    public String getJoke() {
        if (mJokeList.size() == 0) return "No Jokes to tell.";

        return mJokeList.get(mGenerator.nextInt(mJokeList.size()));
    }
}
