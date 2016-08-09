package com.qr.project2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.HashMap;

public class userHome extends AppCompatActivity implements View.OnClickListener{
    Button bLogout,bLeave,bPayroll_view;
    String Username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        bLeave = (Button) findViewById(R.id.bLeave);
        bPayroll_view = (Button) findViewById(R.id.bPayroll_view);
        bLogout = (Button) findViewById(R.id.bLogout);

        Intent i = getIntent();
        Username = i.getStringExtra(Config.Username_Pref_Time);

        bLeave.setOnClickListener(this);
        bPayroll_view.setOnClickListener(this);
        bLogout.setOnClickListener(this);
    }

    private void userLogout(){
        class UserLogout extends AsyncTask<Void,Void,String>{
            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put(Config.Key_Username,Username);

                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest(Config.Logout_url,hashMap);
                return s;
            }
        }
        UserLogout ue = new UserLogout();
        ue.execute();
    }

    private void confirmLogout(){
        AlertDialog.Builder builder = new AlertDialog.Builder(userHome.this);
        builder.setMessage("Proceed?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        userLogout();

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

                        Toast toast = Toast.makeText(userHome.this, "Logout Success", Toast.LENGTH_SHORT);
                        toast.show();
                        Intent intent = new Intent(userHome.this, mainPage.class);
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

    @Override
    public void onClick(View v) {
        if(v == bLogout){
            confirmLogout();
        }
        if(v == bLeave){
            /*Intent i = new Intent(userHome.this, userProfile.class);
            startActivity(i);*/
        }
        if(v == bPayroll_view){
            Intent i = new Intent(userHome.this, viewPayroll.class);
            i.putExtra(Config.Username_Pref_Time, Username);
            startActivity(i);
        }
    }
}
