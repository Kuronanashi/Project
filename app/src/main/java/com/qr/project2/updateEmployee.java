package com.qr.project2;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class updateEmployee extends AppCompatActivity implements ListView.OnItemClickListener{
    ListView lvUsername;
    Button bActive, bInactive,bShow;
    String JSON_STRING;
    String Map_username;

    private final static String T_ID = "ID";
    private final static String T_Name = "Name";
    private final static String T_Username = "Username";
    private final static String T_Email = "Email";
    private final static String T_Status = "Status";

    private final static String T_Username_Map = "Username1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_employee);

        lvUsername = (ListView) findViewById(R.id.lvUsername);
        bActive = (Button) findViewById(R.id.bActive);
        bInactive = (Button) findViewById(R.id.bInactive);
        bShow = (Button) findViewById(R.id.bShow);

        getUsername();

        lvUsername.setOnItemClickListener(this);

        bActive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(updateEmployee.this, "Status change to active", Toast.LENGTH_SHORT).show();
                setActive();
            }
        });

        bInactive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(updateEmployee.this, "Status change to inactive", Toast.LENGTH_SHORT).show();
                setInactive();
            }
        });

        bShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(updateEmployee.this, "Username Refreshed", Toast.LENGTH_SHORT).show();
                getUsername();
            }
        });



    }




    private void getUsername(){
        class GetUsername extends AsyncTask<Void,Void,String>{
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                JSON_STRING = s;
                showUsername();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(Config.getUsernameAdmin_url);
                return s;
            }
        }
        GetUsername gu = new GetUsername();
        gu.execute();
    }


    private void showUsername(){
        JSONObject jsonObject = null;
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray("result");

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);

                String ID = "ID : " +jo.getString(T_ID);
                String Name = "Name : " +jo.getString(T_Name);
                String Username = "Username : " +jo.getString(T_Username);
                String Email = "Email : " +jo.getString(T_Email);
                String Status = "Status : " +jo.getString(T_Status);

                String Username_Map = jo.getString(T_Username);

                HashMap<String,String> employees = new HashMap<>();
                employees.put(T_ID,ID);
                employees.put(T_Name,Name);
                employees.put(T_Username,Username);
                employees.put(T_Email,Email);
                employees.put(T_Status,Status);

                employees.put(T_Username_Map, Username_Map);

                list.add(employees);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ListAdapter adapter = new SimpleAdapter(updateEmployee.this, list, R.layout.get_profile,
                new String[]{T_ID,T_Name,T_Username,T_Email,T_Status},
                new int[]{R.id.tv1, R.id.tv2, R.id.tv3, R.id.tv4, R.id.tv5});

        lvUsername.setAdapter(adapter);
    }

    private void setActive(){
        class SetActive extends AsyncTask<Void,Void,String>{
            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put(Config.Key_Username_Map,Map_username);

                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest(Config.SetActive_url,hashMap);
                return s;
            }
        }
        SetActive sa = new SetActive();
        sa.execute();
    }

    private void setInactive(){
        class SetInactive extends AsyncTask<Void,Void,String>{
            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put(Config.Key_Username_Map,Map_username);

                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest(Config.SetInactive_url,hashMap);
                return s;
            }
        }
        SetInactive si = new SetInactive();
        si.execute();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        HashMap<String,String> map =(HashMap)parent.getItemAtPosition(position);
        //Intent intent = new Intent(this, editLeave.class);

        Map_username = map.get(T_Username_Map).toString();
        Toast.makeText(updateEmployee.this, Map_username + " Selected", Toast.LENGTH_SHORT).show();

        //startActivity(intent);
    }


}
