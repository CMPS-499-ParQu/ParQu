package com.sourcey.materiallogindemo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInstaller;
import android.net.Uri;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.Properties;

public class ContactUs extends AppCompatActivity {

    EditText name, email, message;
    String name2, email2, message2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
     //   getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        name = (EditText) findViewById(R.id.editText);
        email = (EditText) findViewById(R.id.editText2);
        message = (EditText) findViewById(R.id.editText3);
        name2 = name.getText().toString();
        email2 = email.getText().toString();
        message2 = message.getText().toString();
    }

    public void senMail(View v){


       if(!name.getText().toString().isEmpty()  && !email.getText().toString().isEmpty() && !message.getText().toString().isEmpty() ){
           Intent i = new Intent(Intent.ACTION_SEND);
           i.setType("message/rfc822");
           i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"parQU2019@gmail.com"});
           i.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
           i.putExtra(Intent.EXTRA_TEXT   , message.getText().toString()+"");
           try {
               startActivity(Intent.createChooser(i, "Send mail..."));
           } catch (android.content.ActivityNotFoundException ex) {
               Toast.makeText(ContactUs.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
           }
        }
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setMessage("Fill all information in the form");
            builder.setTitle("Error");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog a = builder.create();
            a.show();
        }


    }
}
