package com.sourcey.materiallogindemo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class ContactUs extends AppCompatActivity {

    EditText name, email, message;
    String name2, email2, message2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        name = (EditText) findViewById(R.id.editText);
        email = (EditText) findViewById(R.id.editText2);
        message = (EditText) findViewById(R.id.editText3);
        name2 = name.getText().toString();
        email2 = email.getText().toString();
        message2 = message.getText().toString();
    }

    public void senMail(View v){

     /* Intent intent = new Intent(Intent.ACTION_SENDTO); // it's not ACTION_SEND
       intent.setType("plain/text");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
        intent.putExtra(Intent.EXTRA_TEXT, message2);

        intent.setData(Uri.parse("mailto:alaamousa123456@gmail.com")); // or just "mailto:" for blank
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.
        startActivity(intent);*/
        if(!name2.isEmpty()  && !email2.isEmpty() && !message2.isEmpty() ){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setMessage("Thank you for contacting ParQU. A support officer will contact you.");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog a = builder.create();
            a.show();
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
