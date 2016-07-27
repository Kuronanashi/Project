package com.qr.project2;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class userHome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        final TextView tvLoginUsername = (TextView)findViewById(R.id.tvLoginUsername);
        final TextView tvLoginEmail = (TextView)findViewById(R.id.tvLoginEmail);
        final Button bLogout = (Button) findViewById(R.id.bLogout);

        Intent i = getIntent();

        String Username = i.getStringExtra("Username");
        String Email = i.getStringExtra("Email");

        String Show = Username + " Welcome or login...";
        String Show2 = Email + " User Email";

        tvLoginUsername.setText(Show);
        tvLoginEmail.setText(Show2);

        bLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(userHome.this, mainPage.class);
                if(Build.VERSION.SDK_INT >= 11) {
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                } else {
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                }
                Toast toast = Toast.makeText(userHome.this, "Logout Success", Toast.LENGTH_SHORT);
                toast.show();
                startActivity(intent);
            }
        });
    }
}
