package com.qr.project2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class qrtest extends AppCompatActivity  {

    private final static String T_ID = "ID";
    private final static String T_Name = "Name";
    private final static String T_Username = "Username";
    private final static String T_Email = "Email";

    ArrayList <HashMap<String, String>> params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrtest);

        final ListView List = (ListView) findViewById(R.id.listview);

        params = new ArrayList<HashMap<String, String>>();

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://tryqr123.tk/getUsername.php";

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

                    ListAdapter adapter = new SimpleAdapter(qrtest.this, params, R.layout.get_profile,
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
                Toast.makeText(qrtest.this, "Not working", Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(stringRequest);
    }
}
