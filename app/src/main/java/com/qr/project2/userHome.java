package com.qr.project2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AlertDialog;
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

        final AlertDialog.Builder builder = new AlertDialog.Builder(userHome.this);

        final TextView tvLoginUsername = (TextView)findViewById(R.id.tvLoginUsername);
        final TextView tvLoginEmail = (TextView)findViewById(R.id.tvLoginEmail);

        final TextView tvName = (TextView)findViewById(R.id.tvName);
        final TextView tvEmail = (TextView)findViewById(R.id.tvEmail);


        final Button bLogout = (Button) findViewById(R.id.bLogout);
        final Button bShow = (Button) findViewById(R.id.bShow);

        bLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences pref = getSharedPreferences(Config.Pref_Name, Context.MODE_PRIVATE);
                SharedPreferences.Editor editorPref = pref.edit();

                editorPref.putBoolean(Config.Login_Status_Pref, false);
                editorPref.putString(Config.Name_Pref, "");
                editorPref.putString(Config.Username_Pref, "");
                editorPref.putString(Config.Email_Pref, "");
                editorPref.putString(Config.Password_Pref, "");
                editorPref.commit();

                /*if(Build.VERSION.SDK_INT >= 11) {
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                } else {
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                }*/

                builder.setMessage("Proceed?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                Intent intent = new Intent(userHome.this, mainPage.class);
                                Toast toast = Toast.makeText(userHome.this, "Logout Success", Toast.LENGTH_SHORT);
                                toast.show();
                                startActivity(intent);
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

        bShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences pref = getSharedPreferences(Config.Pref_Name, Context.MODE_PRIVATE);

                String Username = pref.getString(Config.Username_Pref, "Empty");
                String Password = pref.getString(Config.Password_Pref, "Empty");
                String Name = pref.getString(Config.Name_Pref, "Empty");
                String Email = pref.getString(Config.Email_Pref, "Empty");

                String Show =  "Name: " + Name;
                String Show2 =  "Username: " + Username;
                String Show3 =  "Email: " + Email;
                String Show4 = "Password: " + Password;

                tvLoginUsername.setText(Show2);
                tvLoginEmail.setText(Show4);
                tvName.setText(Show);
                tvEmail.setText(Show3);
            }
        });
    }
}
