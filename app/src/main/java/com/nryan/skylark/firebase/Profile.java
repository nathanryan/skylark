package com.nryan.skylark.firebase;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nryan.skylark.MainActivity;
import com.nryan.skylark.R;

/**
 * Created by Nathan Ryan x13448212 on 10/02/2017.
 */

public class Profile extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;

    private Button buttonLogout;
    private Button birbList;

    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, Login.class));
        }

        databaseReference = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        buttonLogout = (Button) findViewById(R.id.buttonLogout);
        birbList = (Button) findViewById(R.id.birbBtn);

        buttonLogout.setOnClickListener(this);
        birbList.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == buttonLogout) {
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this, Login.class));
        }

        if (view == birbList) {
            startActivity(new Intent(this, MainActivity.class));
        }
    }
}
