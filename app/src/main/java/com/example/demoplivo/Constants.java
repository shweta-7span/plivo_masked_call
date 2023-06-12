package com.example.demoplivo;

import com.plivo.endpoint.BuildConfig;

public class Constants {
    public static final String LOGIN_SUCCESS = "onLogin success";
    public static final String LOGOUT_SUCCESS = "onLogout success";
    public static final String LOGIN_FAILED = "onLogin Failed";
    public static final String OUTGOING_CALL_DIAL_HINT = "Enter sip uri or phone number";
    public static final String OUTGOING_CALL = "onOutgoingCall";
    public static final String OUTGOING_CALL_RINGING = "onOutgoingCall Ringing";
    public static final String OUTGOING_CALL_ANSWERED = "onOutgoingCall Answered";
    public static final String OUTGOING_CALL_REJECTED = "onOutgoingCall Rejected";
    public static final String OUTGOING_CALL_HANGUP = "onOutgoingCall Hangup";
    public static final String OUTGOING_CALL_INVALID = "onOutgoingCall Invalid";
    public static final String RINGING_LABEL = "Ringing...";

    //REQUEST_CODE
    public static final int RECORD_AUDIO_PERMISSION_REQUEST_CODE = 101;

    public static final String APP_PACKAGE_NAME = BuildConfig.APPLICATION_ID;
    public static final String NOTIFICATION_CHANNEL_ID = APP_PACKAGE_NAME;



}
