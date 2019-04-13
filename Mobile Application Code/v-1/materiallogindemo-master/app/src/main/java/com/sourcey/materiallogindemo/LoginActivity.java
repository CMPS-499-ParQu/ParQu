package com.sourcey.materiallogindemo;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.content.Intent;
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

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText emailText, passwordText ;
    Button signInButton;
    TextView signUpLink , progressBarText;
    ProgressBar progressBar;
    private FirebaseAuth mAuth;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        emailText =(EditText) findViewById(R.id.input_email);

        passwordText =(EditText) findViewById(R.id.input_password);

        signInButton = (Button)findViewById(R.id.btn_login) ;
        signUpLink = (TextView)findViewById(R.id.link_signup) ;
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        progressBarText = (TextView) findViewById(R.id.progressBarinsideText);


        findViewById(R.id.link_signup).setOnClickListener(this);
        findViewById(R.id.btn_login).setOnClickListener(this);

    }


    private void userLogIn() {
        final String email = emailText.getText().toString().trim();
        String password = passwordText.getText().toString().trim();

        if (validate()){

            progressBar.setVisibility(View.VISIBLE);
            progressBarText.setVisibility(View.VISIBLE);


//            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                @Override
//                public void onComplete(@NonNull Task<AuthResult> task) {
//                    progressBar.setVisibility(View.GONE);
//                    progressBarText.setVisibility(View.GONE);
//                    if (task.isSuccessful()) {
//                        finish();
//                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(intent);
//                    }else {
//                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//
//                    }
//
//                }
//            });
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progressBar.setVisibility(View.GONE);
                    progressBarText.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        finish();
                        Intent intent = new Intent(LoginActivity.this, Choices.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("email", email);
                        startActivity(intent);
                    } else {

                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }

    }


   public boolean validate() {
        boolean valid = true;

       String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

       if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError("enter a valid email address");
            valid = false;
       } else {
           emailText.setError(null);
       }

      if (password.isEmpty() || password.length() < 6 || password.length() > 12) {
          passwordText.setError("between 4 and 10 alphanumeric characters");
           valid = false;
       } else {
           passwordText.setError(null);
       }

       return valid;
   }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                userLogIn();
                break;

            case R.id.link_signup:
                finish();
                startActivity(new Intent(this, SignupActivity.class));
                break;
        }
    }
}
