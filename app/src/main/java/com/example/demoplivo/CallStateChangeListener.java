package com.example.demoplivo;

import android.os.Parcelable;

import com.plivo.endpoint.Outgoing;

public interface CallStateChangeListener {
    void onCallStateChanged(CallState callState, Outgoing data);
}
