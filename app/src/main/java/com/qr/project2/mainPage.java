package com.qr.project2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class mainPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        Button bSignInID = (Button) findViewById(R.id.bSignInID);
        Button bSignInQR = (Button) findViewById(R.id.bSignInQR);

        Button bQrtest = (Button) findViewById(R.id.bQrtest);
        Button bShow = (Button) findViewById(R.id.bShow);

        final TextView tvChange = (TextView)findViewById(R.id.tvChange);
        final TextView tvChange2 = (TextView) findViewById(R.id.tvChange2);

        bSignInID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mainPage.this, signID.class);
                startActivity(i);
            }
        });

        bSignInQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mainPage.this, signQR.class);
                startActivity(i);
            }
        });

        bQrtest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent (mainPage.this, qrtest.class);
                startActivity(i);
            }
        });

        bShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences prefShare = getSharedPreferences("login.config", Context.MODE_PRIVATE);
                String x= prefShare.getString("Data 1", "");
                String y= prefShare.getString("Data 2", "");

                tvChange.setText(x);
                tvChange2.setText(y);
            }
        });
    }
}
