package com.nkdroid.tinderswipe;

import android.content.Intent;
import android.media.AudioManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.firebase.auth.FirebaseAuth;

import static android.R.id.toggle;

public class Setting extends AppCompatActivity {

    Button logoutButton;
    Button goBackButton;
    private FirebaseAuth mFirebaseAuth;
    private static Bundle bundle = new Bundle();

    static ToggleButton soundButton;

    public void toggleClick(View v){
        AudioManager amanager = (AudioManager)getSystemService(AUDIO_SERVICE);
        if(soundButton.isChecked())
            amanager.setStreamMute(AudioManager.STREAM_MUSIC, true);
        else
            amanager.setStreamMute(AudioManager.STREAM_MUSIC, false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        // Initialize FirebaseAuth
        mFirebaseAuth = FirebaseAuth.getInstance();
        logoutButton = (Button)findViewById(R.id.logoutButton);
        goBackButton = (Button)findViewById(R.id.btnGoBack);
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

        goBackButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                finish();
                Intent mainIntent = new Intent(Setting.this, MainActivity.class);
                startActivity(mainIntent);
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
