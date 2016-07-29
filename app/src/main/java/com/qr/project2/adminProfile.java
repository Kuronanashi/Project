package com.qr.project2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class adminProfile extends AppCompatActivity {

    private final static String T_ID = "ID";
    private final static String T_Name = "Name";
    private final static String T_Username = "Username";
    private final static String T_Email = "Email";

    ArrayList<HashMap<String, String>> params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_profile);

        final ListView List = (ListView) findViewById(R.id.lvUser);
        Button bAdd = (Button) findViewById(R.id.bAdd);
        final Button bLogout = (Button) findViewById(R.id.bLogout);

        final AlertDialog.Builder builder = new AlertDialog.Builder(adminProfile.this);

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://tryqr123.tk/getUsername.php";
        params = new ArrayList<HashMap<String, String>>();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray("result");

                    for (int i = 0; i< array.length(); i++) {
                        JSONObject get = array.getJSONObject(i);

                        String ID = get.getString(T_ID);
                        String Name = get.getString(T_Name);
                        String Username = get.getString(T_Username);
                        String Email = get.getString(T_Email);

                        HashMap<String, String> detail_params = new HashMap<String, String>();

                        detail_params.put(T_ID, ID);
                        detail_params.put(T_Name, Name);
                        detail_params.put(T_Username, Username);
                        detail_params.put(T_Email, Email);

                        params.add(detail_params);
                    }

                    ListAdapter adapter = new SimpleAdapter(adminProfile.this, params, R.layout.get_profile,
                            new String[]{T_ID,T_Name,T_Username,T_Email},
                            new int[]{R.id.ID, R.id.Name, R.id.Username, R.id.Email}
                    );

                    List.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(adminProfile.this, "Not working", Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(stringRequest);

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
                                editorPref.apply();

                                Intent intent = new Intent(adminProfile.this, mainPage.class);
                                Toast toast = Toast.makeText(adminProfile.this, "Logout Success", Toast.LENGTH_SHORT);
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
    }
}
