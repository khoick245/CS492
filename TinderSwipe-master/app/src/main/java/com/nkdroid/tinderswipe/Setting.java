package com.nkdroid.tinderswipe;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;

import static android.R.id.toggle;
import static com.nkdroid.tinderswipe.R.id.seekBar;
import static com.nkdroid.tinderswipe.R.id.textView;
import static java.security.AccessController.getContext;

public class Setting extends AppCompatActivity {

    Button logoutButton;
    SeekBar radiusSeekBar;
    Button changeRadiusButton;
    TextView milesTextView;
    private FirebaseAuth mFirebaseAuth;
    private static Bundle bundle = new Bundle();
    int defaultMiles;

    static ToggleButton soundButton;

    SharedPreferences app_preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        // get radius from preference manager to show on label and on seek bar
        app_preferences = PreferenceManager.getDefaultSharedPreferences(this);
        defaultMiles = app_preferences.getInt("Radius",2);

        radiusSeekBar = (SeekBar)findViewById(seekBar);
        changeRadiusButton = (Button) findViewById(R.id.btnChangeRadius);
        milesTextView = (TextView) findViewById(R.id.txtMiles);

        radiusSeekBar.setProgress(defaultMiles);
        milesTextView.setText("Miles: " + radiusSeekBar.getProgress());

        changeRadiusButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                // write radius value to preference manager
                app_preferences = PreferenceManager.getDefaultSharedPreferences(Setting.this);
                SharedPreferences.Editor editor = app_preferences.edit();
                editor.putInt("Radius", radiusSeekBar.getProgress());
                editor.commit();

                // reload the app
                Intent mStartActivity = new Intent(Setting.this, LocationLoading.class);
                int mPendingIntentId = 123456;
                PendingIntent mPendingIntent = PendingIntent.getActivity(Setting.this,mPendingIntentId, mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
                AlarmManager mgr = (AlarmManager)Setting.this.getSystemService(Context.ALARM_SERVICE);
                mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        } );

        radiusSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                defaultMiles = radiusSeekBar.getProgress();
                milesTextView.setText("Miles: " + radiusSeekBar.getProgress());

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                milesTextView.setText("Miles: " + radiusSeekBar.getProgress());
            }
        });


        // Initialize FirebaseAuth
        mFirebaseAuth = FirebaseAuth.getInstance();
        logoutButton = (Button)findViewById(R.id.logoutButton);
        soundButton = (ToggleButton)findViewById(R.id.SoundToggleButton);

        logoutButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                mFirebaseAuth.signOut();
                //finish();
                Intent logInIntent = new Intent(Setting.this, LogInActivity.class);
                logInIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                logInIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(logInIntent);
            }
        } );


        soundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AudioManager amanager = (AudioManager)getSystemService(AUDIO_SERVICE);

                // Is the toggle on?
                boolean on = ((ToggleButton)view).isChecked();

                if(on) {
                    amanager.setStreamMute(AudioManager.STREAM_MUSIC, false);
                }
                else {
                    amanager.setStreamMute(AudioManager.STREAM_MUSIC, true);
                }
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        bundle.putBoolean("ToggleButtonState", soundButton.isChecked());
    }

    @Override
    public void onResume() {
        super.onResume();
        soundButton.setChecked(bundle.getBoolean("ToggleButtonState",true));
    }
}
