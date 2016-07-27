package com.qr.project2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class signID extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    boolean checkFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_id);

        final EditText etUsername = (EditText) findViewById(R.id.etUsername);
        final EditText etPassword = (EditText) findViewById(R.id.etPassword);
        final Button bLogin = (Button) findViewById(R.id.bLogin);
        final CheckBox cBox = (CheckBox) findViewById(R.id.cBox);
        cBox.setOnCheckedChangeListener(this);
        checkFlag = cBox.isChecked();

        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String Username = etUsername.getText().toString();
                final String Password = etPassword.getText().toString();

                AlertDialog.Builder builder = new AlertDialog.Builder(signID.this);
                boolean valid = false;

                if (Username.matches("") & Password.matches("")) {
                    builder.setMessage("Please, Enter The Value")
                            .setNegativeButton("Retry", null)
                            .create()
                            .show();
                } else if (Username.matches("")) {
                    etUsername.setError("Please, Enter The Username");

                } else if (Password.matches("")) {
                    etPassword.setError("Please, Enter The Password");
                } else {
                    valid = true;
                }

                if (!Username.matches("") & !Password.matches("") & valid) {
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");
                                int adminLogin = jsonResponse.getInt("loginAdmin");

                                String Name = jsonResponse.getString("Name");
                                String Email = jsonResponse.getString("Email");

                                SharedPreferences pref = getSharedPreferences(Config.Pref_Name, Context.MODE_PRIVATE);
                                final SharedPreferences.Editor editorPref = pref.edit();

                                AlertDialog.Builder builder = new AlertDialog.Builder(signID.this);
                                if (success) {
                                    if (checkFlag) {
                                        editorPref.putString(Config.Name_Pref, Name);
                                        editorPref.putString(Config.Username_Pref, Username);
                                        editorPref.putString(Config.Email_Pref, Email);
                                        editorPref.putString(Config.Password_Pref, Password);
                                        editorPref.putBoolean(Config.Login_Status_Pref, true);
                                        editorPref.apply();
                                    }
                                    Toast toast = Toast.makeText(signID.this, "User Login Done", Toast.LENGTH_SHORT);
                                    toast.show();

                                    Intent i = new Intent(signID.this, userHome.class);
                                    startActivity(i);
                                    finish();

                                } else if (adminLogin == 1) {
                                    if (checkFlag) {
                                        editorPref.putString(Config.Name_Pref, Name);
                                        editorPref.putString(Config.Username_Pref, Username);
                                        editorPref.putString(Config.Email_Pref, Email);
                                        editorPref.putString(Config.Password_Pref, Password);
                                        editorPref.putBoolean(Config.Login_Status_Pref, true);
                                        editorPref.apply();
                                    }
                                    Toast toast = Toast.makeText(signID.this, "Admin Login Done", Toast.LENGTH_SHORT);
                                    toast.show();

                                    Intent i = new Intent(signID.this, adminHome.class);
                                    startActivity(i);
                                    finish();

                                } else {
                                    builder.setMessage("Login Failed")
                                            .setNegativeButton("Retry", null)
                                            .create()
                                            .show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };

                    signIDRequest loginRequest = new signIDRequest(Username, Password, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(signID.this);
                    queue.add(loginRequest);
                }
            }
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        checkFlag = b;
    }
}
