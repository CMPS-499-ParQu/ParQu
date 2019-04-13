package com.sourcey.materiallogindemo;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

 EditText nameText, plateText , emailText, mobileText , passwordText , reEnterPasswordText , uidText;
 Button signUpButton;
 TextView loginLink , progressBarText;
 ProgressBar progressBar;
 private FirebaseAuth mAuth;
    DatabaseReference databaseUsers;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        databaseUsers = FirebaseDatabase.getInstance().getReference("users");

        nameText = (EditText) findViewById(R.id.input_name);
        plateText = (EditText) findViewById(R.id.input_plateNumber);
        emailText =(EditText) findViewById(R.id.input_email);
        mobileText= (EditText) findViewById(R.id.input_mobile);
        passwordText =(EditText) findViewById(R.id.input_password);
        uidText =(EditText) findViewById(R.id.input_uid);
        reEnterPasswordText = (EditText) findViewById(R.id.input_reEnterPassword);
        signUpButton = (Button)findViewById(R.id.btn_signup) ;
        loginLink = (TextView)findViewById(R.id.link_login) ;
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        progressBarText = (TextView) findViewById(R.id.progressBarinsideText);

        findViewById(R.id.btn_signup).setOnClickListener(this);
        findViewById(R.id.link_login).setOnClickListener(this);
//

        mAuth = FirebaseAuth.getInstance();
    }

    private void registerUser() {
        final String name = nameText.getText().toString().trim();
        final String plateNo = plateText.getText().toString().trim();
        final String email = emailText.getText().toString().trim();
        final String mobile = mobileText.getText().toString().trim();
        final String password = passwordText.getText().toString().trim();
        final String uid = uidText.getText().toString().trim();

    if (validate()){

        progressBar.setVisibility(View.VISIBLE);
        progressBarText.setVisibility(View.VISIBLE);


        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {


                progressBar.setVisibility(View.GONE);
                progressBarText.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    String id = databaseUsers.push().getKey();
                    User user = new User(name, plateNo , email, mobile , password, uid);

                    databaseUsers.child(id).setValue(user);

//                    Toast.makeText(getApplicationContext(),"User Registered Sucsessfull" , Toast.LENGTH_LONG).show();
                    finish();
                    Intent intent = new Intent(SignupActivity.this, Choices.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("email", email);
                    startActivity(intent);
                }else {

                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                      //  Toast.makeText(getApplicationContext(), "You are already registered", Toast.LENGTH_SHORT).show();
                        AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);

                        builder.setMessage("You are already registered");
                        builder.setTitle("Error");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        AlertDialog a = builder.create();
                        a.show();
                    } else {
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

    }

    }


    public boolean validate() {
        boolean valid = true;

        String name = nameText.getText().toString().trim();
        String plateNumber = plateText.getText().toString().trim();
        String email = emailText.getText().toString().trim();
        String mobile = mobileText.getText().toString().trim();
        String password = passwordText.getText().toString().trim();
        String reEnterPassword = reEnterPasswordText.getText().toString().trim();

        if (name.isEmpty() || name.length() < 3) {
            nameText.setError("at least 3 characters");
            valid = false;
        } else {
            nameText.setError(null);
        }

        if (plateNumber.isEmpty()) {
            plateText.setError("Enter Valid Number");
            valid = false;
        } else {
            plateText.setError(null);
        }


        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError("enter a valid email address");
            valid = false;
        } else {
            emailText.setError(null);
        }

        if (mobile.isEmpty() || mobile.length()!=8) {
            mobileText.setError("Enter Valid Mobile Number");
            valid = false;
        } else {
            mobileText.setError(null);
        }

        if (password.isEmpty() || password.length() < 6 || password.length() > 12) {
            passwordText.setError("between 6 and 12 alphanumeric characters");
            valid = false;
        } else {
            passwordText.setError(null);
        }

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 6 || reEnterPassword.length() > 12 || !(reEnterPassword.equals(password))) {
            reEnterPasswordText.setError("Password doesn't match");
            valid = false;
        } else {
            reEnterPasswordText.setError(null);
        }

        return valid;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_signup:
                registerUser();
                break;

            case R.id.link_login:
                finish();
                startActivity(new Intent(this, LoginActivity.class));
                break;
        }
    }
}