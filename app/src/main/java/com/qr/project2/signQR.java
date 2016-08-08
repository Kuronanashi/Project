package com.qr.project2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

public class signQR extends AppCompatActivity implements View.OnClickListener {

    private Button bEncode;
    private TextView formatTxt, contentTxt;
    String x ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_qr);

        /*RelativeLayout root=(RelativeLayout)findViewById(R.id.root);
        root.setBackgroundColor(Color.parseColor(Config.Colour));*/

        bEncode = (Button) findViewById(R.id.bEncode);
        bEncode.setOnClickListener(signQR.this);
    }

    public void onClick(View v){
        if(v.getId()==R.id.bEncode){
            IntentIntegrator integrator = new IntentIntegrator(signQR.this);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
            integrator.setPrompt("Scan Your QR code login");
            integrator.setBarcodeImageEnabled(false);
            integrator.setBeepEnabled(false);
            integrator.setCameraId(0);
            integrator.initiateScan();
        }
    }
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            String scanContent = scanningResult.getContents();
            String scanFormat = scanningResult.getFormatName();

            String Username ="";
            if (!(scanContent.equals(""))) {
                Username = scanContent.toString();
            }

            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");
                        int adminLogin = jsonResponse.getInt("loginAdmin");

                        String Name = jsonResponse.getString("Name");
                        String Username = jsonResponse.getString("Username");
                        String Email = jsonResponse.getString("Email");
                        String Password = jsonResponse.getString("Password");

                        SharedPreferences pref = getSharedPreferences(Config.Pref_Name, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editorPref = pref.edit();

                        AlertDialog.Builder builder = new AlertDialog.Builder(signQR.this);
                        if (success) {
                            Toast toast = Toast.makeText(signQR.this, "User Login Done", Toast.LENGTH_SHORT);
                            toast.show();

                            editorPref.putString(Config.Username_Pref_Time, Username);
                            editorPref.apply();

                            Intent i = new Intent(signQR.this, userHome.class);
                            startActivity(i);
                            finish();

                        } else if (adminLogin == 1) {
                            Toast toast = Toast.makeText(signQR.this, "Admin Login Done", Toast.LENGTH_SHORT);
                            toast.show();

                            editorPref.putString(Config.Username_Pref_Time, Username);
                            editorPref.apply();

                            Intent i = new Intent(signQR.this, adminHome.class);
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
            signQRRequest loginRequest = new signQRRequest(Username, responseListener);
            RequestQueue queue = Volley.newRequestQueue(signQR.this);
            queue.add(loginRequest);

        }
        else{
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}













