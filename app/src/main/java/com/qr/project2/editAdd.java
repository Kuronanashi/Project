package com.qr.project2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.json.JSONException;
import org.json.JSONObject;

public class editAdd extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_add);

        /*RelativeLayout root=(RelativeLayout)findViewById(R.id.root);
        root.setBackgroundColor(Color.parseColor(Config.Colour));*/

        final Button bAddProfile = (Button) findViewById(R.id.bAddProfile);
        final EditText etName = (EditText)findViewById(R.id.etName);
        final EditText etUsername = (EditText)findViewById(R.id.etUsername);
        final EditText etEmail = (EditText)findViewById(R.id.etEmail);
        final EditText etPassword = (EditText)findViewById(R.id.etPassword);
        final EditText etRPassword = (EditText)findViewById(R.id.etRPassword);

        bAddProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String Name         = etName.getText().toString();
                final String Username     = etUsername.getText().toString();
                final String Email        = etEmail.getText().toString();
                final String Password     = etPassword.getText().toString();
                final String RPassword    = etRPassword.getText().toString();
                final int UsernameLength  = Username.length();
                final int NameLength  = Name.length();

                final AlertDialog.Builder builder = new AlertDialog.Builder(editAdd.this);
                boolean valid = false;
                final String EmailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                final Intent i = new Intent(editAdd.this, AddSuccess.class);

                if (Name.matches("") & Username.matches("") & Email.matches("") & Password.matches("") & RPassword.matches("")) {
                    builder.setMessage("Please, Enter The Value")
                            .setNegativeButton("Retry", null)
                            .create()
                            .show();
                } else if (Name.matches("")) {
                    etName.setError("Please, Enter The Name");
                }else if (NameLength < 5 || NameLength > 16) {
                    etName.setError("Please, Enter The Name between 5 to 16 character");

                } else if (Username.matches("")) {
                    etUsername.setError("Please, Enter The Username");
                } else if (UsernameLength < 5 || UsernameLength > 16) {
                    etUsername.setError("Please, Enter The Username between 5 to 16 character");

                }else if (Email.matches("")) {
                    etEmail.setError("Please, Enter The Email");
                } else if (!Email.matches(EmailPattern)) {
                    etEmail.setError("Please, Enter Valid Email");

                } else if (Password.matches("")) {
                    etPassword.setError("Please, Enter The Password");
                } else if (RPassword.matches("")) {
                    etRPassword.setError("Please, Enter The Re-Password");
                } else if (!Password.matches(RPassword)){
                    etRPassword.setError("Please, Re-enter Password Same With The Password");
                }else
                    valid = true;

                if (!Username.matches("")) {
                    try {
                        BitMatrix bitMatrix = multiFormatWriter.encode(Username, BarcodeFormat.QR_CODE, 200, 200);
                        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                        Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                        i.putExtra("pic", bitmap);

                    } catch (WriterException e) {
                        e.printStackTrace();
                    }
                }

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            int Message = jsonResponse.getInt("message");
                            int MessageError = jsonResponse.getInt("messageError");

                            if (MessageError == 1) {
                                builder.setMessage("Please, Enter Other Username")
                                        .setNegativeButton("Retry", null)
                                        .create()
                                        .show();
                            } else if (MessageError == 2) {
                                builder.setMessage("Please, Enter Other Email")
                                        .setNegativeButton("Retry", null)
                                        .create()
                                        .show();
                            } else if (MessageError == 3) {
                                builder.setMessage("Please, Enter Other Username and Email ")
                                        .setNegativeButton("Retry", null)
                                        .create()
                                        .show();
                            } else if (Message == 1) {
                                Toast toast = Toast.makeText(editAdd.this, "Adding Employee Done", Toast.LENGTH_SHORT);
                                toast.show();
                                startActivity(i);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                if (valid) {
                    editAddRequest AddRequest = new editAddRequest(Name, Username, Email, Password, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(editAdd.this);
                    queue.add(AddRequest);

                }

            }
        });
    }
}