package com.example.demoplivo;

import com.plivo.endpoint.Incoming;
import com.plivo.endpoint.Outgoing;

import java.util.HashMap;

public interface BackendListener {
    void onLogin(boolean success);
    void onLogout();
    void onIncomingCall(Incoming data, CallState callState);
    void onOutgoingCall(Outgoing data, CallState callState);
    void onIncomingDigit(String digit);
    void mediaMetrics(HashMap messageTemplate);
}
