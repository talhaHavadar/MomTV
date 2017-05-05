package com.scorptech.momtv;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.scorptech.momtv.socket.Client;
import com.scorptech.momtv.socket.SocketListener;
import com.scorptech.momtv.socket.TCPClient;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements RecognitionListener, SocketListener {

    private static final String TAG = "MainActivity";
    private final int REQUEST_CODE_RECORD_AUDIO = 999;
    private final static String SHARED_SERVER_IP_KEY = "SERVER_IP";
    SpeechRecognizer speechRecognizer;
    ImageView btnSpeak;
    TextView tvResult;
    TextView tvStatus;
    EditText etIPAddress;
    SharedPreferences prefs;
    RelativeLayout settings_container;
    int settingsClickCount;
    TCPClient client;
    boolean speechAvailable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefs = this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        implementSettingsContainer();

        etIPAddress = (EditText) findViewById(R.id.etIPAddress);
        etIPAddress.setVisibility(View.GONE);
        // Setting default text for EditText View
        etIPAddress.setText(getServerIPAddress() == null ? "" : getServerIPAddress());
        connectToServer();
        etIPAddress.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                boolean handled = true;
                Log.e(TAG, "onEditorAction: " + i + ":" + keyEvent + ":" + EditorInfo.IME_ACTION_DONE);
                if (i == EditorInfo.IME_ACTION_DONE) {
                    handled = prefs.edit().putString(SHARED_SERVER_IP_KEY, etIPAddress.getText().toString())
                            .commit();
                    if (handled) {
                        etIPAddress.setVisibility(View.GONE);
                        connectToServer();
                    }
                }

                return !handled;
            }
        });

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizer.setRecognitionListener(this);
        tvResult = (TextView) findViewById(R.id.tvResult);
        tvStatus = (TextView) findViewById(R.id.tvStatus);
        btnSpeak = (ImageView) findViewById(R.id.ivBtnSpeech);



        btnSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (speechAvailable) {
                    Intent intent = RecognizerIntent.getVoiceDetailsIntent(getApplicationContext());
                    intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                    speechRecognizer.startListening(intent);
                }

            }
        });
        tvStatus.setText("");
        tvResult.setText("");
        if (!SpeechRecognizer.isRecognitionAvailable(this)) {
            Toast.makeText(this, "Recognition is not available!!", Toast.LENGTH_SHORT).show();
        }

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO)) {

            } else {
                ActivityCompat.requestPermissions(this, new String[] {
                        Manifest.permission.RECORD_AUDIO
                }, REQUEST_CODE_RECORD_AUDIO);
            }
        } else {
            speechAvailable = true;
        }

    }

    private void connectToServer() {
        if (client != null) {
            client.close();
        }
        client = new TCPClient(getServerIPAddress(), 4444);
        client.setSocketListener(this);
        client.connect();
    }

    private String getServerIPAddress() {
        if (prefs != null) {
            return prefs.getString(SHARED_SERVER_IP_KEY, "192.168.1.4");
        }
        return null;
    }

    private void implementSettingsContainer() {
        settings_container = (RelativeLayout) findViewById(R.id.settings_container);
        settingsClickCount = 0;
        final Handler handler = new Handler();
        final Runnable clickTimeout = new Runnable() {
            @Override
            public void run() {
                if (settingsClickCount < 3) {
                    settingsClickCount = 0;
                }
            }
        };
        settings_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etIPAddress.getVisibility() == View.GONE) {
                    if (settingsClickCount == 0) {
                        handler.postDelayed(clickTimeout, 5000);
                    }
                    if(settingsClickCount >= 3) {
                        etIPAddress.setVisibility(View.VISIBLE);
                        handler.removeCallbacks(clickTimeout);
                        settingsClickCount = 0;
                    }
                    settingsClickCount++;
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_RECORD_AUDIO: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    speechAvailable = true;
                } else speechAvailable = false;
            }
        }
    }

    @Override
    public void onReadyForSpeech(Bundle bundle) {
        tvStatus.setText(R.string.on_listening);
        btnSpeak.setImageResource(R.mipmap.btnspeechactive);
    }

    @Override
    public void onBeginningOfSpeech() {
        Log.d(TAG, "onBeginningOfSpeech: ");
    }

    @Override
    public void onRmsChanged(float v) {
        Log.d(TAG, "onRmsChanged: " + v);
    }

    @Override
    public void onBufferReceived(byte[] bytes) {
        Log.e(TAG, "onBufferReceived: " + new String(bytes));
    }

    @Override
    public void onEndOfSpeech() {
        tvStatus.setText("");
        btnSpeak.setImageResource(R.mipmap.btnspeech);
    }

    @Override
    public void onError(int i) {
        Log.e(TAG, "onError: " + i);
        if (i == 7) {
            tvResult.setText(R.string.on_error_understand);
        } else {
            tvResult.setText(R.string.on_error);
        }
    }

    @Override
    public void onResults(Bundle bundle) {
        ArrayList<String> resultList = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        if (resultList != null && resultList.size() > 0) {
            Log.e(TAG, "onResults: " + resultList.get(0));
            tvResult.setText(resultList.get(0));
            client.send(resultList.get(0));
        }
    }

    @Override
    public void onPartialResults(Bundle bundle) {
        ArrayList<String> resultList = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        if (resultList != null && resultList.size() > 0) {
            tvResult.setText(resultList.get(resultList.size() - 1));
        }
    }

    @Override
    public void onEvent(int i, Bundle bundle) {
        Log.d(TAG, "onEvent: ");
    }

    @Override
    public void onData(Client client, String data) {
        Log.e(TAG, "onData: " + data);
    }
}
