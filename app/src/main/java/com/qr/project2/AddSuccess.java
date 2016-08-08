package com.qr.project2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class AddSuccess extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_success);


        final ImageView imageView = (ImageView)findViewById(R.id.imageView);
        final Button bHome = (Button) findViewById(R.id.bHome);

        final Button bLogout = (Button) findViewById(R.id.bLogout);

        Bitmap bitmap = getIntent().getParcelableExtra("pic");
        imageView.setImageBitmap(bitmap);

        final AlertDialog.Builder builder = new AlertDialog.Builder(AddSuccess.this);

        bHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AddSuccess.this, adminHome.class);
                startActivity(i);
                finish();
            }
        });

        bLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                builder.setMessage("Proceed?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                SharedPreferences pref = getSharedPreferences(Config.Pref_Name, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editorPref = pref.edit();

                                editorPref.putBoolean(Config.Login_Status_Pref, false);
                                editorPref.putBoolean(Config.Login_Status_Pref_Admin, false);
                                editorPref.putString(Config.Name_Pref, "");
                                editorPref.putString(Config.Username_Pref, "");
                                editorPref.putString(Config.Email_Pref, "");
                                editorPref.putString(Config.Password_Pref, "");
                                editorPref.putString(Config.Username_Pref_Time, "");
                                editorPref.apply();

                                Toast toast = Toast.makeText(AddSuccess.this, "Logout Success", Toast.LENGTH_SHORT);
                                toast.show();
                                Intent intent = new Intent(AddSuccess.this, mainPage.class);
                                if(Build.VERSION.SDK_INT >= 11) {
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                } else {
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                }
                                startActivity(intent);
                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .create()
                        .show();
            }
        });
    }
}
