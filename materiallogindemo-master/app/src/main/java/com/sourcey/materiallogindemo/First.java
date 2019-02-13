package com.sourcey.materiallogindemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class First extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        // conitne a s vip or normal

        final Button normalBtn = findViewById(R.id.normal_btn);
        Button vipBtn = findViewById(R.id.vip_btn);


        normalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent normalIntent = new Intent(First.this,Choices2.class);
                startActivity(normalIntent);
            }
        });


        vipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent vipIntent = new Intent(First.this,LoginActivity.class);
                startActivity(vipIntent);
            }
        });


    }
}
