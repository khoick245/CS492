package com.nkdroid.tinderswipe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class Setting extends AppCompatActivity {

    Button logoutButton;
    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        // Initialize FirebaseAuth
        mFirebaseAuth = FirebaseAuth.getInstance();
        logoutButton = (Button)findViewById(R.id.logoutButton);

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
    }
}
