package com.qr.project2;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class viewLeaveAdmin extends AppCompatActivity {
    private final static String T_Username = "Username";
    private final static String T_Leave_Date = "Leave_Date";
    private final static String T_Description = "Description";
    private final static String T_Status = "Status";

    Spinner s_Username;

    String Username;
    TextView tv3;
    ListView lvStatus;

    private String JSON_STRING;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_leave_admin);

        Intent i = getIntent();
        Username = i.getStringExtra(Config.Username_Pref_Time);

        s_Username = (Spinner)findViewById(R.id.s_Username);
        tv3 = (TextView) findViewById(R.id.tv3);
        lvStatus = (ListView) findViewById(R.id.lvStatus);

        getUsername();

        s_Username.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Spinner_selected = sUsername_spinner.getSelectedItem().toString();
                Toast.makeText(viewLeaveAdmin.this, s_Username.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                getStatus();
                getStatusDetail();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }





    private void getUsername(){
        class GetUsername extends AsyncTask<Void,Void,String> {
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                JSON_STRING = s;
                showUsername();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(Config.getUsername_url);
                return s;
            }
        }
        GetUsername gj = new GetUsername();
        gj.execute();
    }

    private void showUsername(){
        JSONObject jsonObject = null;
        ArrayList<String> list = new ArrayList<>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray("result");

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                list.add(jo.getString(T_Username));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        s_Username.setAdapter(new ArrayAdapter<String>(viewLeaveAdmin.this, android.R.layout.simple_spinner_dropdown_item, list));
    }





    private void getStatus(){
        Username = s_Username.getSelectedItem().toString();
        class GetStatus extends AsyncTask<Void,Void,String>{
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                showStatus(s);
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(Config.getLeaveStatusAndCount_url,Username);
                return s;
            }
        }
        GetStatus gs = new GetStatus();
        gs.execute();
    }

    private void showStatus(String json){
        String Status_and_count = null;
        try {
            JSONObject jsonObject = new JSONObject(json);

            int Status = jsonObject.getInt("Status");
            int Count = jsonObject.getInt("Count");
            String s ;

            if (Status == 1) {
                s = " On Leave";
            } else {
                s = " Available";
            }
            Status_and_count = "Current Today Status : " +s + "\nLeft Count to Apply Leave : " +Count;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        tv3.setText(Status_and_count);
    }





    private void getStatusDetail(){
        Username = s_Username.getSelectedItem().toString();
        class GetStatusDetail extends AsyncTask<Void,Void,String> {
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                showStatusDetail(s);
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(Config.getLeaveUserStatus_url,Username);
                return s;
            }
        }
        GetStatusDetail gsd = new GetStatusDetail();
        gsd.execute();
    }

    private void showStatusDetail(String json){
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray("result");

            for(int i = 0; i<result.length(); i++){
                JSONObject get = result.getJSONObject(i);

                String Username = get.getString(T_Username);
                String Leave_Date =get.getString(T_Leave_Date);
                String Status =get.getString(T_Status);
                String Description =get.getString(T_Description);

                HashMap<String, String> detail_params = new HashMap<>();
                detail_params.put(T_Username, "Username: " +Username);
                detail_params.put(T_Leave_Date, "Leave Date : " +Leave_Date);
                detail_params.put(T_Status, "Status : " +Status);
                detail_params.put(T_Description, "Description : " +Description);
                list.add(detail_params);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ListAdapter adapter = new SimpleAdapter( viewLeaveAdmin.this, list, R.layout.get_payroll,
                new String[]{T_Username,T_Leave_Date,T_Status,T_Description},
                new int[]{R.id.tv1, R.id.tv2, R.id.tv3, R.id.tv4});
        lvStatus.setAdapter(adapter);
    }



}
