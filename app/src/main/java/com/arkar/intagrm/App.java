package com.arkar.intagrm;

import android.app.Application;

import com.parse.Parse;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("qL5OlnogwZw7o3NqWbk2PQGQvbe7sR2TqUkGtJYt")
                // if defined
                .clientKey("ztEwa35thVwKw3gnxXXZrfUiACs20D8A83mrVqD4")
                .server("https://parseapi.back4app.com/")
                .build()
        );
    }
}
