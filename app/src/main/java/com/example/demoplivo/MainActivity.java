package com.example.demoplivo;

import static com.example.demoplivo.Constants.RECORD_AUDIO_PERMISSION_REQUEST_CODE;
import static com.example.demoplivo.Utils.HH_MM_SS;
import static com.example.demoplivo.Utils.MM_SS;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.plivo.endpoint.Incoming;
import com.plivo.endpoint.Outgoing;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements BackendListener {

    final private String TAG = getClass().getName();
    private ActionBar actionBar;
    private Timer callTimer;
    private int tick;

    private Object callData;

    private boolean isSpeakerOn=false, isHoldOn=false, isMuteOn=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Plivo Calling");
        actionBar = getSupportActionBar();

        FirebaseApp.initializeApp(getApplicationContext());

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            init();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.RECORD_AUDIO}, RECORD_AUDIO_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RECORD_AUDIO_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                init();
            }
        }
    }

    private void init() {
        registerBackendListener();
        loginWithToken();
    }

    private void registerBackendListener() {
        ((App) getApplication()).backend().setListener(this);
    }

    private void loginWithToken() {
        if (Utils.getLoggedinStatus()) {
            showUserName();
        } else {
            FirebaseMessaging.getInstance().getToken().addOnSuccessListener(s -> {
                ((App) getApplication()).backend().login(Utils.USERNAME, Utils.PASSWORD, s);
            });
        }
    }

    private void showUserName() {
        ((AppCompatTextView) findViewById(R.id.logged_in_as)).setText(Utils.USERNAME);
    }

    public void onClickBtnMakeCall(View view) {
        EditText phoneNumberText = findViewById(R.id.call_text);
        String phoneNumber = phoneNumberText.getText().toString();
        if (phoneNumber.matches("")) {
            Toast.makeText(this, Constants.OUTGOING_CALL_DIAL_HINT, Toast.LENGTH_SHORT).show();
            return;
        }

        showOutCallUI(CallState.IDLE,null, phoneNumber);
    }

    private void makeCall(String phoneNum) {
        Outgoing outgoing = ((App) getApplication()).backend().getOutgoing();
        if (outgoing != null) {
            outgoing.call(phoneNum);
        }
    }

    public void onClickBtnSpeaker(View view) {
        AudioManager audioManager =(AudioManager)getSystemService(Context.AUDIO_SERVICE);
        ImageButton btn = findViewById(R.id.speaker);
        if(isSpeakerOn) {
            isSpeakerOn=false;
            btn.setImageResource(R.drawable.speaker);
            audioManager.setMode(AudioManager.MODE_IN_CALL);
            audioManager.setMode(AudioManager.MODE_NORMAL);
        }
        else {
            isSpeakerOn=true;
            btn.setImageResource(R.drawable.speaker_selected);
            audioManager.setMode(AudioManager.MODE_NORMAL);
            audioManager.setMode(AudioManager.MODE_IN_CALL);
        }
        audioManager.setSpeakerphoneOn(isSpeakerOn);
    }

    public void onClickBtnHold(View view) {
        ImageButton btn = findViewById(R.id.hold);
        if(isHoldOn) {
            isHoldOn=false;
            btn.setImageResource(R.drawable.hold);
            unHoldCall();
        }
        else {
            isHoldOn=true;
            btn.setImageResource(R.drawable.hold_selected);
            holdCall();
        }
    }

    public void holdCall() {
        if (callData != null) {
            if (callData instanceof Outgoing) {
                ((Outgoing) callData).hold();
            } else {
                ((Incoming) callData).hold();
            }
        }
    }

    public void unHoldCall() {
        if (callData != null) {
            if (callData instanceof Outgoing) {
                ((Outgoing) callData).unhold();
            } else {
                ((Incoming) callData).unhold();
            }
        }
    }

    public void onClickBtnMute(View view) {
        ImageButton btn = findViewById(R.id.mute);
        if(isMuteOn) {
            isMuteOn=false;
            btn.setImageResource(R.drawable.mute);
            unMuteCall();
        }
        else {
            isMuteOn=true;
            btn.setImageResource(R.drawable.mute_selected);
            muteCall();
        }
    }

    public void muteCall() {
        if (callData != null) {
            if (callData instanceof Outgoing) {
                ((Outgoing) callData).mute();
            } else {
                ((Incoming) callData).mute();
            }
        }
    }

    public void unMuteCall() {
        if (callData != null) {
            if (callData instanceof Outgoing) {
                ((Outgoing) callData).unmute();
            } else {
                ((Incoming) callData).unmute();
            }
        }
    }

    public void onClickBtnEndCall(View view) {
        AudioManager audioManager =(AudioManager)getSystemService(Context.AUDIO_SERVICE);
        cancelTimer();
        endCall();
        isSpeakerOn = false;
        isHoldOn = false;
        isMuteOn = false;
        audioManager.setSpeakerphoneOn(isSpeakerOn);
        setContentView(R.layout.activity_main);
//        updateUI(STATE.IDLE, null);
    }

    public void endCall() {
        if (callData != null) {
            if (callData instanceof Outgoing) {
                Log.d(TAG, "End Call");
                ((Outgoing) callData).hangup();
            } else {
                ((Incoming) callData).hangup();
            }
        }else{
            Log.e(TAG, "callData is NULL");
        }
    }

    /**
     * Display & Handle Outgoing Calls
     *
     * @param callState
     * @param phoneNumber
     */
    private void showOutCallUI(CallState callState, Outgoing outgoing, String phoneNumber) {
        callData = outgoing;

        String title = callState.name();
        TextView callerState;

        switch (callState) {
            case IDLE:
                setContentView(R.layout.call);
                actionBar.hide();
                callerState = findViewById(R.id.caller_state);
                callerState.setText(title);
                if (phoneNumber != null) {
                    TextView callerName = findViewById(R.id.caller_name);
                    callerName.setText(phoneNumber);
                    makeCall(phoneNumber);
                }
                break;
            case RINGING:
                callerState = findViewById(R.id.caller_state);
                callerState.setText(Constants.RINGING_LABEL);
                break;
            case ANSWERED:
                startTimer();
                break;
            case HANGUP:
            case REJECTED:
                cancelTimer();
                setContentView(R.layout.activity_main);
                actionBar.show();
                showUserName();
                break;
        }
    }

    private void startTimer() {
        cancelTimer();

        callTimer = new Timer(false);
        callTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    int hours = (int) TimeUnit.SECONDS.toHours(tick);
                    int minutes = (int) TimeUnit.SECONDS.toMinutes(tick -= TimeUnit.HOURS.toSeconds(hours));
                    int seconds = (int) (tick - TimeUnit.MINUTES.toSeconds(minutes));
                    String text = hours > 0 ? String.format(HH_MM_SS, hours, minutes, seconds) : String.format(MM_SS, minutes, seconds);
                    TextView timerTextView = findViewById(R.id.caller_state);
                    if (timerTextView != null) {
                        timerTextView.setVisibility(View.VISIBLE);
                        timerTextView.setText(text);
                        tick++;
                    }
                });
            }
        }, 100, TimeUnit.SECONDS.toMillis(1));
    }

    private void cancelTimer() {
        if (callTimer != null) callTimer.cancel();
        tick = 0;
    }


    /*private void updateUI(STATE state, Object data) {
        callData = data;
        if (data != null) {
            if (data instanceof Outgoing) {
                // handle outgoing
                showOutCallUI(state, (Outgoing) data, null);
            }
        }
    }*/

    @Override
    public void onLogin(boolean success) {
        runOnUiThread(() -> {
            if (success) {
                showUserName();
            } else {
                Toast.makeText(this, R.string.login_success, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onLogout() {

    }

    @Override
    public void onIncomingCall(Incoming data, CallState callState) {

    }

    @Override
    public void onOutgoingCall(Outgoing data, CallState callState) {
        Log.d(TAG, "onOutgoingCall Done: " + callState);
        runOnUiThread(() -> showOutCallUI(callState, data,null));
    }

    @Override
    public void onIncomingDigit(String digit) {

    }

    @Override
    public void mediaMetrics(HashMap messageTemplate) {

    }
}