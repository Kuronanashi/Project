package com.qr.project2;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

public class editLeave extends AppCompatActivity implements ListView.OnItemClickListener{

    private final static String T_Username = "Username";
    private final static String T_Leave_Date = "Leave_Date";
    private final static String T_Description = "Description";
    private final static String T_Status = "Status";

    private final static String T_Username_Map = "Username1";

    private Button bShow,bApprove,bDecline;
    private ListView lvLeave;
    private String JSON_STRING;

    String Map_username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_leave);

        bShow = (Button) findViewById(R.id.bShow);
        bApprove = (Button) findViewById(R.id.bApprove);
        bDecline = (Button) findViewById(R.id.bDecline);
        lvLeave = (ListView)findViewById(R.id.lvLeave);

        lvLeave.setOnItemClickListener(this);

        getJSON();

        bShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(editLeave.this, "Status refreshed", Toast.LENGTH_SHORT).show();
                getJSON();
            }
        });

        bApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(editLeave.this, "Status approved", Toast.LENGTH_SHORT).show();
                adminApprove();
            }
        });

        bDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(editLeave.this, "Status decline", Toast.LENGTH_SHORT).show();
                adminDecline();
            }
        });
    }

    private void showRequest(){
        JSONObject jsonObject = null;
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray("result");

            for(int i = 0; i<result.length(); i++){
                JSONObject get = result.getJSONObject(i);

                String Username_Map = get.getString(T_Username);

                String Username = "Username: " + Username_Map;
                String Leave_Date = "Leave Date: " +get.getString(T_Leave_Date);
                String Description = "Description: " +get.getString(T_Description);
                String Status = "Status: " + get.getString(T_Status);

                HashMap<String, String> detail_params = new HashMap<>();
                detail_params.put(T_Username, Username);
                detail_params.put(T_Leave_Date, Leave_Date);
                detail_params.put(T_Description, Description);
                detail_params.put(T_Status, Status);

                detail_params.put(T_Username_Map, Username_Map);

                list.add(detail_params);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ListAdapter adapter = new SimpleAdapter( editLeave.this, list, R.layout.get_payroll,
                new String[]{T_Username,T_Leave_Date,T_Description,T_Status},
                new int[]{R.id.tv1, R.id.tv2, R.id.tv3, R.id.tv4});
        lvLeave.setAdapter(adapter);
    }

    private void adminApprove(){
        class AdminApprove extends AsyncTask<Void,Void,String>{
            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put(Config.Key_Username_Map,Map_username);

                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest(Config.SetApprove_url,hashMap);
                return s;
            }
        }
        AdminApprove aa = new AdminApprove();
        aa.execute();
    }

    private void adminDecline(){
        class AdminApprove extends AsyncTask<Void,Void,String>{
            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put(Config.Key_Username_Map,Map_username);

                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest(Config.SetDecline_url,hashMap);
                return s;
            }
        }
        AdminApprove aa = new AdminApprove();
        aa.execute();
    }

    private void getJSON(){
        class GetJSON extends AsyncTask<Void,Void,String> {
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                JSON_STRING = s;
                showRequest();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(Config.editLeave_url);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        HashMap<String,String> map =(HashMap)parent.getItemAtPosition(position);
        //Intent intent = new Intent(this, editLeave.class);

        Map_username = map.get(T_Username_Map).toString();
        Toast.makeText(editLeave.this, Map_username + "Selected", Toast.LENGTH_SHORT).show();

        //startActivity(intent);
    }
}
