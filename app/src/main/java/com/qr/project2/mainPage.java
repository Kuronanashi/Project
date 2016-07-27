package com.qr.project2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class mainPage extends AppCompatActivity {

    boolean checkLogin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        Button bSignInID = (Button) findViewById(R.id.bSignInID);
        Button bSignInQR = (Button) findViewById(R.id.bSignInQR);

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
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences pref = getSharedPreferences(Config.Pref_Name, Context.MODE_PRIVATE);
        checkLogin = pref.getBoolean(Config.Login_Status_Pref, false);

        if (checkLogin) {
            Intent i = new Intent(mainPage.this, userHome.class);
            startActivity(i);
        }
    }
}
