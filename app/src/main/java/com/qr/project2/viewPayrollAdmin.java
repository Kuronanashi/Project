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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class viewPayrollAdmin extends AppCompatActivity implements View.OnClickListener{

    private final static String T_Username = "Username";
    private final static String T_Time_Detail = "Time_Detail";
    private final static String T_Time_Logout = "Time_Logout";
    private final static String T_Time_Date = "T_Date";

    private final static String T_Total = "T_Total";

    Button bLogout,bMonth_check;
    String Username, Spinner_selected;
    String MonthA[] = {"1","2","3","4","5","6","7","8","9","10","11","12"};
    Spinner sMonth_spinner,sUsername_spinner;

    TextView tvTry,tvResult_data;
    ListView listView;

    private String JSON_STRING;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_payroll_admin);

        bLogout = (Button) findViewById(R.id.bLogout);
        bMonth_check = (Button) findViewById(R.id.bMonth_check);
        sMonth_spinner = (Spinner)findViewById(R.id.sMonth_spinner);
        sUsername_spinner = (Spinner)findViewById(R.id.sUsername_spinner);


        tvTry = (TextView) findViewById(R.id.tvTry);
        listView = (ListView) findViewById(R.id.listView);

        tvResult_data = (TextView) findViewById(R.id.tvResult_data);

        //Intent i = getIntent();
        //Username = i.getStringExtra(Config.Username_Pref_Time);
        //Username = "rizky";


        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, MonthA);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sMonth_spinner.setAdapter(spinnerArrayAdapter);

        bLogout.setOnClickListener(this);
        bMonth_check.setOnClickListener(this);

        getUsername();

        sMonth_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Spinner_selected = sMonth_spinner.getSelectedItem().toString();
                Toast.makeText(viewPayrollAdmin.this, sMonth_spinner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sUsername_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Spinner_selected = sUsername_spinner.getSelectedItem().toString();
                Toast.makeText(viewPayrollAdmin.this, sUsername_spinner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    private void getTime(){
        class GetTime extends AsyncTask<Void,Void,String> {
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //JSON_STRING = s;
                takeTime(s);
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                //String s = rh.sendGetRequest(Config.getTime_url);
                String s = rh.sendGetRequestParam(Config.getTime_url, Username);
                return s;
            }
        }
        GetTime gj = new GetTime();
        gj.execute();
    }

    private void takeTime(String json){
        //JSONObject jsonObject = null;
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray("result");

            Username = sUsername_spinner.getSelectedItem().toString();

            String x = sMonth_spinner.getSelectedItem().toString();
            int m = Integer.parseInt(x);
            int mo = 0;
            if (m == 1){
                mo = 31;
            } else if (m == 2){
                mo = 29;
            } else if (m == 3){
                mo = 31;
            } else if (m == 4){
                mo = 30;
            } else if (m == 5){
                mo = 31;
            } else if (m == 6){
                mo = 30;
            } else if (m == 7){
                mo = 31;
            } else if (m == 8){
                mo = 31;
            } else if (m == 9){
                mo = 30;
            } else if (m == 10){
                mo = 31;
            } else if (m == 11){
                mo = 30;
            } else if (m == 12){
                mo = 31;
            }

            String format = "yyyy-MM-dd HH:mm:ss";
            SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);

            Calendar cal=Calendar.getInstance();
            SimpleDateFormat current_month_f = new SimpleDateFormat("M", Locale.US);
            String current_month = current_month_f.format(cal.getTime());
            SimpleDateFormat current_date_f = new SimpleDateFormat("d", Locale.US);
            String current_date = current_date_f.format(cal.getTime());

            int Count_full =0 , Count_not_full = 0, Total_hour = 0 , Total_money = 0, Count_w=0;

            for(int i = 0; i<result.length(); i++){
                JSONObject get = result.getJSONObject(i);

                String Username = get.getString(T_Username);
                String Time_Detail =get.getString(T_Time_Detail);
                String Time_Logout =get.getString(T_Time_Logout);

                Date fromDate = sdf.parse(Time_Detail);
                Date toDate = sdf.parse(Time_Logout);
                long diff = 0;

                String month_data = "" + (fromDate.getMonth()+1);

                if(fromDate.before(toDate))
                {
                    diff = toDate.getTime() - fromDate.getTime();
                }
                if(fromDate.after(toDate))
                {
                    diff = fromDate.getTime() - toDate.getTime();
                }

                //long diff = toDate.getTime() - fromDate.getTime();
                String dateFormat="Duration: ";

                int diffDays = (int) (diff / (24 * 60 * 60 * 1000));
                if(diffDays>0){
                    dateFormat+=diffDays+" day ";
                }
                diff -= diffDays * (24 * 60 * 60 * 1000);

                int diffhours = (int) (diff / (60 * 60 * 1000));
                if(diffhours>0){
                    dateFormat+=diffhours+" hour ";
                }
                diff -= diffhours * (60 * 60 * 1000);

                int diffmin = (int) (diff / (60 * 1000));
                if(diffmin>0){
                    dateFormat+=diffmin+" min ";
                }
                diff -= diffmin * (60 * 1000);

                int diffsec = (int) (diff / (1000));
                if(diffsec>0){
                    dateFormat+=diffsec+" sec";
                }



                if (x.matches(month_data) ) {

                    if (diffhours >= 8 || diffDays >= 1)
                    {
                        Count_full++;
                    } else{
                        Count_not_full++;
                    }
                    Count_w++;
                    Total_hour = Total_hour + diffhours;

                    HashMap<String, String> detail_params = new HashMap<>();
                    detail_params.put(T_Username, "Username: " +Username);
                    detail_params.put(T_Time_Detail, "Login Time : " +Time_Detail);
                    detail_params.put(T_Time_Logout, "Login Out : " +Time_Logout);
                    detail_params.put(T_Time_Date, "Time : " +dateFormat);

                    list.add(detail_params);
                }
            }
            int Money = 2500 - (30 - Count_w) * 50;
            String Text = "Your salary this month is : " + Money + "\nTotal working days : " + Count_w  + " from " + mo + " days with " + Count_full +  " day full time and " + Count_not_full + " day not full time \nTotal working hour : "+ Total_hour + " /month";

            if (x.matches(current_month)){
                Text = "Your salary this month is : " + Money + "\nTotal working days : " + Count_w  + " from " + current_date + " days with " + Count_full +  " day full time and " + Count_not_full + " day not full time \nTotal working hour : "+ Total_hour + " /month";
            }

            tvResult_data.setText(Text);

        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }

        ListAdapter adapter = new SimpleAdapter( viewPayrollAdmin.this, list, R.layout.get_payroll,
                new String[]{T_Username,T_Time_Detail,T_Time_Logout,T_Time_Date},
                new int[]{R.id.tv1, R.id.tv2, R.id.tv3, R.id.tv4});
        listView.setAdapter(adapter);

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
                String s = rh.sendGetRequest(Config.getUsername_url);
                return s;
            }
        }
        GetUsername gj = new GetUsername();
        gj.execute();
    }

    private void showUsername(){
        JSONObject jsonObject = null;
        //ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
        ArrayList<String> list = new ArrayList<>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray("result");

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                //String Username = jo.getString(T_Username);

                //HashMap<String,String> employees = new HashMap<>();
                //employees.put(T_Username,Username);
                list.add(jo.getString(T_Username));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        sUsername_spinner.setAdapter(new ArrayAdapter<String>(viewPayrollAdmin.this, android.R.layout.simple_spinner_dropdown_item, list));

        /*ListAdapter adapter = new SimpleAdapter(viewPayrollAdmin.this, list, R.layout.get_profile,
                new String[]{T_Username},
                new int[]{R.id.tv1});

        listView.setAdapter(adapter);*/
    }

    private void userLogout(){
        class UserLogout extends AsyncTask<Void,Void,String> {
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
        AlertDialog.Builder builder = new AlertDialog.Builder(viewPayrollAdmin.this);
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

                        Toast toast = Toast.makeText(viewPayrollAdmin.this, "Logout Success", Toast.LENGTH_SHORT);
                        toast.show();
                        Intent intent = new Intent(viewPayrollAdmin.this, mainPage.class);
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
        if(v == bMonth_check){
            getTime();
        }

    }
}
