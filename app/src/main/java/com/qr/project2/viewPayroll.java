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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class viewPayroll extends AppCompatActivity implements View.OnClickListener{

    private final static String T_Username = "Username";
    private final static String T_Time_Detail = "Time_Detail";
    private final static String T_Time_Logout = "Time_Logout";
    private final static String T_Time_Date = "T_Date";

    private final static String T_Total = "T_Total";
    private final static String T_Leave_Date = "Leave_Date";

    Button bLogout,bMonth_check;
    String Username, Spinner_selected;
    String MonthA[] = {"1","2","3","4","5","6","7","8","9","10","11","12"};
    Spinner sMonth_spinner;

    TextView tvTry,tvResult_data;
    ListView listView;

    String Total_Leave;

    private String JSON_STRING;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_payroll);

        bLogout = (Button) findViewById(R.id.bLogout);
        bMonth_check = (Button) findViewById(R.id.bMonth_check);
        sMonth_spinner = (Spinner)findViewById(R.id.sMonth_spinner);

        tvTry = (TextView) findViewById(R.id.tvTry);
        listView = (ListView) findViewById(R.id.listView);

        tvResult_data = (TextView) findViewById(R.id.tvResult_data);

        Intent i = getIntent();
        Username = i.getStringExtra(Config.Username_Pref_Time);

        ArrayAdapter <String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, MonthA);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sMonth_spinner.setAdapter(spinnerArrayAdapter);

        bLogout.setOnClickListener(this);
        bMonth_check.setOnClickListener(this);

        sMonth_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Spinner_selected = sMonth_spinner.getSelectedItem().toString();
                Toast.makeText(viewPayroll.this, sMonth_spinner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
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

            String date_current = sdf.format(cal.getTime());

            int Count_full =0 , Count_not_full = 0, Total_hour = 0 , Total_money = 0, Count_w=0 , diff_hour = 0;

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
                        diff_hour = diff_hour + diffhours;
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
            Count_w = Count_w + Integer.parseInt(Total_Leave);

            double salary_day = 100;
            double salary_hour = 10;

            double salary_total = (Count_full + Integer.parseInt(Total_Leave)) * salary_day + diff_hour * salary_hour;
            //double salary_total = 3000;

            double salary_total_y = salary_total*12;
            double EPF_y = salary_total_y *11 / 100;

            if (EPF_y > 6000){
                EPF_y = 6000;
            }
            double EPF = EPF_y/12;

            double SOCSO = 0;
            if (salary_total <= 800){
                SOCSO = 4.25;
            } else if (salary_total <= 2900){
                SOCSO = 4.25 + (((salary_total/100) - 8) * 0.50 );
            } else{
                SOCSO = 14.75;
            }

            double chargeable_income = salary_total_y - EPF_y - 9000;

            double first , last,incometax_y = 0;
            if (chargeable_income <=5000){
                first = last = 0;
                incometax_y = first +last;
            } else if (chargeable_income <=10000){
                first = 0;
                last = (chargeable_income - 5000) / 100;
                incometax_y = first +last;
            } else if (chargeable_income <=20000){
                first = 50;
                last = (chargeable_income - 10000) / 100;
                incometax_y = first +last;
            } else if (chargeable_income <=35000){
                first = 150;
                last = (chargeable_income - 20000) * 5 / 100;
                incometax_y = first +last;
            } else if (chargeable_income <=50000){
                first = 900;
                last = (chargeable_income - 35000) * 10 / 100;
                incometax_y = first +last;
            } else if (chargeable_income <=70000){
                first = 2400;
                last = (chargeable_income - 50000) * 16 / 100;
                incometax_y = first +last;
            } else if (chargeable_income <=100000) {
                first = 5600;
                last = (chargeable_income - 70000) * 21 / 100;
                incometax_y = first + last;
            } else if (chargeable_income <=250000) {
                first = 11900;
                last = (chargeable_income - 100000) * 24 / 100;
                incometax_y = first + last;
            } else if (chargeable_income <=400000) {
                first = 11900;
                last = (chargeable_income - 250000) * 49 / 2  / 100;
                incometax_y = first + last;
            } else if (chargeable_income > 400000) {
                first = 84650;
                last = (chargeable_income - 400000) * 25 / 100;
                incometax_y = first + last;
            }

            double incometax = incometax_y /12 ;
            double total = EPF + SOCSO + incometax;
            double total_y = total*12;

            double annual, annual_year = 0;
            annual = salary_total - total;
            annual_year = annual* 12;

            String annual_d =String.format("%.2f", annual);

            String Text = "Current Date: " + date_current +
                    "\nTotal working days : " + Count_w  + " from " + mo + " days with " + Count_full +  " day full time, " + Count_not_full + " day less then 8 hours and " + Total_Leave + " Leave approve " +
                    "\nTotal working hour : "+ Total_hour + " /month" +
                    "\nYour salary this month is : " + salary_total + " and after deducted with tax : " + annual_d;

            if (x.matches(current_month)){
                Text = "Current Date: " + date_current +
                        "\nTotal working days : " + Count_w  + " from " + current_date + " days with " + Count_full +  " day full time, " + Count_not_full + " day less then 8 hours and " + Total_Leave + " Leave approve " +
                        "\nTotal working hour : "+ Total_hour + " /month" +
                        "\nYour salary this month is : " + salary_total + " and after deducted with tax : " + annual_d;
            }
            tvResult_data.setText(Text);

        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }

        ListAdapter adapter = new SimpleAdapter( viewPayroll.this, list, R.layout.get_payroll,
                new String[]{T_Username,T_Time_Detail,T_Time_Logout,T_Time_Date},
                new int[]{R.id.tv1, R.id.tv2, R.id.tv3, R.id.tv4});
        listView.setAdapter(adapter);

    }



    private void getStatus(){
        class GetStatus extends AsyncTask<Void,Void,String>{
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                showStatus(s);
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(Config.getLeaveDateForPayroll,Username);
                return s;
            }
        }
        GetStatus gs = new GetStatus();
        gs.execute();
    }

    private void showStatus(String json){

        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray("result");

            String x = sMonth_spinner.getSelectedItem().toString();
            int m = Integer.parseInt(x);

            String format = "yyyy-MM-dd HH:mm:ss";
            SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);

            SimpleDateFormat day_f = new SimpleDateFormat("dd", Locale.US);
            SimpleDateFormat year_f = new SimpleDateFormat("yyyy", Locale.US);

            Calendar cal=Calendar.getInstance();

            String date_current = day_f.format(cal.getTime());
            String year_current = year_f.format(cal.getTime());
            //String d = "" + (date_current.getDay());
            int d1 = Integer.parseInt(date_current);

            int leave_count =0;

            for(int i = 0; i<result.length(); i++){
                JSONObject get = result.getJSONObject(i);
                String Leave_Date = get.getString(T_Leave_Date);

                Date date_leave = sdf.parse(Leave_Date);
                String day_data = "" + (date_leave.getDay());
                String month_data = "" + (date_leave.getMonth()+1);
                String year_data = "" + (date_leave.getYear() + 1900);

                int d2 = Integer.parseInt(day_data);
                //date_leave.getTime();

                if (x.matches(month_data) && year_current.matches(year_data) ) {
                    if ( 1 <= d2 && d2 <= d1)
                    {
                        leave_count++;
                    }
                }
                Total_Leave = leave_count + "";
            }

        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }
        //tv3.setText(Status_and_count);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(viewPayroll.this);
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

                        Toast toast = Toast.makeText(viewPayroll.this, "Logout Success", Toast.LENGTH_SHORT);
                        toast.show();
                        Intent intent = new Intent(viewPayroll.this, mainPage.class);
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
            getStatus();
            getTime();
        }

    }




















    /*public static void Try{
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray("result");
                    params = new ArrayList<HashMap<String, String>>();

                    /*String time_detail = jsonObject.getString("Time_Detail");
                    String time_logout = jsonObject.getString("Time_Logout");*/

                    /*String format = "dd-MM-yyyy HH:mm:ss";
                    SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);

                    for (int i = 0; i< array.length(); i++) {
                        JSONObject get = array.getJSONObject(i);

                        String Username = get.getString(T_Username);
                        String Time_Detail = get.getString(T_Time_Detail);
                        String Time_Logout = get.getString(T_Time_Logout);

                        Date fromDate = sdf.parse(Time_Detail);
                        Date toDate = sdf.parse(Time_Logout);
                        long diff = 0;


                        if(fromDate.before(toDate))
                        {
                            diff = toDate.getTime() - fromDate.getTime();
                        }
                        if(fromDate.after(toDate))
                        {
                            diff = fromDate.getTime() - toDate.getTime();
                        }

                        //long diff = toDate.getTime() - fromDate.getTime();
                        String dateFormat="duration: ";

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

                        HashMap<String, String> detail_params = new HashMap<String, String>();

                        detail_params.put(T_Username, Username);
                        detail_params.put(T_Time_Detail, Time_Detail);
                        detail_params.put(T_Time_Logout, Time_Logout);
                        detail_params.put(T_Time_Date, dateFormat);

                        params.add(detail_params);
                    }

                    /*tvShowTime.setText(dateFormat);
                            tvShowTime2.setText(time_logout);*/

                    /*ListAdapter adapter = new SimpleAdapter(viewPayroll.this, params, R.layout.get_payroll,
                            new String[]{T_Username,T_Time_Detail,T_Time_Logout,T_Time_Date},
                            new int[]{R.id.Username, R.id.Time_Detail, R.id.Time_Logout, R.id.Time_Date}
                    );

                    List.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(viewPayroll.this, "Not working", Toast.LENGTH_SHORT).show();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(T_Username, Username);
                return params;
            }
        };
        queue2.add(stringRequest);
    };*/


}


