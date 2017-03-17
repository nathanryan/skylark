package com.nryan.skylark.firebase;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nryan.skylark.MainActivity;
import com.nryan.skylark.R;

public class Profile extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;

    private TextView textViewUserEmail;
    private Button buttonLogout;
    private Button buttonList;
    private Button birbList;

    private DatabaseReference databaseReference;

    private EditText editName, editAddress;
    private Button buttonSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this, Login.class));
        }

        databaseReference = FirebaseDatabase.getInstance().getReference();

        editName = (EditText) findViewById(R.id.editTextName);
        editAddress = (EditText) findViewById(R.id.editTextAddress);
        buttonSave = (Button) findViewById(R.id.buttonAddInfo);


        FirebaseUser user = firebaseAuth.getCurrentUser();

        textViewUserEmail = (TextView) findViewById(R.id.textViewUserEmail);
        textViewUserEmail.setText("Welcome " + user.getEmail());


        buttonLogout = (Button) findViewById(R.id.buttonLogout);
        buttonList = (Button) findViewById(R.id.btnList);
        birbList = (Button) findViewById(R.id.birbBtn);

        buttonLogout.setOnClickListener(this);
        buttonSave.setOnClickListener(this);
        buttonList.setOnClickListener(this);
        birbList.setOnClickListener(this);
    }

    private void saveUserInformation(){
        String name = editName.getText().toString().trim();
        String address = editAddress.getText().toString().trim();

        UserInformation userInformation = new UserInformation(name, address);

        FirebaseUser user = firebaseAuth.getCurrentUser();

        databaseReference.child(user.getUid()).setValue(userInformation);

        Toast.makeText(this, "Information Saved..", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View view) {
        if(view == buttonLogout){
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this, Login.class));
        }

        if(view == buttonSave){
            saveUserInformation();
        }

        if(view == buttonList){
            startActivity(new Intent(this, ProfileList.class));
        }

        if(view == birbList){
            startActivity(new Intent(this, MainActivity.class));
        }
    }
}
