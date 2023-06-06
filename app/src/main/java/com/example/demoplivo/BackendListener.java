package com.example.demoplivo;

import com.plivo.endpoint.Incoming;
import com.plivo.endpoint.Outgoing;

import java.util.HashMap;

enum STATE { IDLE, PROGRESS, RINGING, ANSWERED, HANGUP, REJECTED, INVALID }

public interface BackendListener {
    void onLogin(boolean success);
    void onLogout();
    void onIncomingCall(Incoming data, STATE callState);
    void onOutgoingCall(Outgoing data, STATE callState);
    void onIncomingDigit(String digit);
    void mediaMetrics(HashMap messageTemplate);
}
