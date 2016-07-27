package com.qr.project2;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class adminProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_profile);

        Button bAdd = (Button) findViewById(R.id.bAdd);
        final Button bLogout = (Button) findViewById(R.id.bLogout);

        bAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(adminProfile.this, editAdd.class);
                startActivity(i);
            }
        });

        bLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(adminProfile.this, mainPage.class);
                if(Build.VERSION.SDK_INT >= 11) {
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                } else {
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                }
                Toast toast = Toast.makeText(adminProfile.this, "Logout Success", Toast.LENGTH_SHORT);
                toast.show();
                startActivity(intent);
            }
        });
    }
}
