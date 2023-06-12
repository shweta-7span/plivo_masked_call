package com.example.demoplivo;

import android.util.Log;

import com.plivo.endpoint.Endpoint;
import com.plivo.endpoint.EventListener;
import com.plivo.endpoint.Incoming;
import com.plivo.endpoint.Outgoing;

import java.util.HashMap;

public class PlivoBackEnd implements EventListener {

    final private String TAG = getClass().getName();

    private Endpoint endpoint;

    private CallListener callListener;
    private LoginListener loginListener;

    static PlivoBackEnd newInstance() {
        return new PlivoBackEnd();
    }

    public static HashMap<String, Object> options = new HashMap<String, Object>() {
        {
            put("debug", true);
            put("enableTracking", true);
            put("maxAverageBitrate", 21000);
        }
    };

    public void init(boolean log) {
        endpoint = Endpoint.newInstance(true, this, options);
    }

    public void setLoginListener(LoginListener loginListener) {
        this.loginListener = loginListener;
    }

    public void setBackendListener(CallListener callListener) {
        this.callListener = callListener;
    }

    public Outgoing getOutgoing() {
        return endpoint.createOutgoingCall();
    }

    // To register with SIP Server
    public void login(String username, String password, String newToken) {
        endpoint.login(username, password, newToken);
    }

    // Plivo SDK callbacks
    @Override
    public void onLogin() {
        Log.d(TAG, Constants.LOGIN_SUCCESS);
        if (loginListener != null) loginListener.onLogin(true);
    }

    @Override
    public void onLogout() {

    }

    @Override
    public void onLoginFailed() {
        Log.d(TAG, Constants.LOGIN_FAILED);
        if (loginListener != null) loginListener.onLogin(false);
    }

    @Override
    public void onIncomingDigitNotification(String s) {

    }

    @Override
    public void onIncomingCall(Incoming incoming) {

    }

    @Override
    public void onIncomingCallHangup(Incoming incoming) {

    }

    @Override
    public void onIncomingCallRejected(Incoming incoming) {

    }

    @Override
    public void onIncomingCallInvalid(Incoming incoming) {

    }

    @Override
    public void onOutgoingCall(Outgoing outgoing) {
        Log.d(TAG, Constants.OUTGOING_CALL);
        if (callListener != null) callListener.onOutgoingCall(outgoing, CallState.PROGRESS);
    }

    @Override
    public void onOutgoingCallRinging(Outgoing outgoing) {
        Log.d(TAG, Constants.OUTGOING_CALL_RINGING);
        if (callListener != null) callListener.onOutgoingCall(outgoing, CallState.RINGING);
    }

    @Override
    public void onOutgoingCallAnswered(Outgoing outgoing) {
        Log.d(TAG, Constants.OUTGOING_CALL_ANSWERED);
        if (callListener != null) callListener.onOutgoingCall(outgoing, CallState.ANSWERED);
    }

    @Override
    public void onOutgoingCallRejected(Outgoing outgoing) {
        Log.d(TAG, Constants.OUTGOING_CALL_REJECTED);
        if (callListener != null) callListener.onOutgoingCall(outgoing, CallState.REJECTED);
    }

    @Override
    public void onOutgoingCallHangup(Outgoing outgoing) {
        Log.d(TAG, Constants.OUTGOING_CALL_HANGUP);
        if (callListener != null) callListener.onOutgoingCall(outgoing, CallState.HANGUP);
    }

    @Override
    public void onOutgoingCallInvalid(Outgoing outgoing) {
        Log.d(TAG, Constants.OUTGOING_CALL_INVALID);
        if (callListener != null) callListener.onOutgoingCall(outgoing, CallState.INVALID);
    }

    @Override
    public void mediaMetrics(HashMap hashMap) {

    }
}
