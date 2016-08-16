package com.qr.project2;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class applyLeaveDetail extends AppCompatActivity {

    ListView lvStatus;

    private final static String T_Username = "Username";
    private final static String T_Leave_Date = "Leave_Date";
    private final static String T_Description = "Description";
    private final static String T_Status = "Status";

    String Username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_leave_detail);

        lvStatus = (ListView) findViewById(R.id.lvStatus);

        Intent i = getIntent();
        Username = i.getStringExtra(Config.Username_Pref_Time);

        getStatusDetail();
    }

    private void getStatusDetail(){
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

                /*SimpleDateFormat Format = new SimpleDateFormat("dd-mm-yyyy", Locale.US);
                Date fromDate = Format.parse(Leave_Date);
                String Date = fromDate + "";*/

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

        ListAdapter adapter = new SimpleAdapter( applyLeaveDetail.this, list, R.layout.get_payroll,
                new String[]{T_Username,T_Leave_Date,T_Status,T_Description},
                new int[]{R.id.tv1, R.id.tv2, R.id.tv3, R.id.tv4});
        lvStatus.setAdapter(adapter);


    }
}
