package com.example.demoplivo;

public class Utils {

    // endpoint username & password
    static final String USERNAME = "shweta211634202018610";
    static final String PASSWORD = "Shweta@1234";
    static final String HH_MM_SS = "%02d:%02d:%02d";
    static final String MM_SS = "%02d:%02d";

    private static boolean isLoggedIn = false;

    static boolean getLoggedinStatus() {
        return isLoggedIn;
    }

    static void setLoggedinStatus(boolean status) {
        isLoggedIn = status;
    }

}
