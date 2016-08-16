package com.qr.project2;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class applyLeave extends AppCompatActivity {

    /*static final int Dialog_ID = 0;

    private DatePicker datePicker;
    private Calendar calendar;
    private TextView dateView;
    private int year, month, day;*/

    TextView tvLeave_status_current;
    EditText etLeave_date,etLeave_description;
    DatePickerDialog datePickerDialog;
    Button bLeave_apply,bLeaveStatusDetail;
    ListView lvStatus;

    private final static String T_Leave_date = "Leave_Date";
    private final static String T_Leave_description = "Leave_Description";
    private final static String T_Username = "Username";
    private final static String T_Leave_Date = "Leave_Date";
    private final static String T_Description = "Description";
    private final static String T_Status = "Status";

    String Leave_date,Leave_description, Username;
    int year_x, month_x, day_x;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_leave);

        etLeave_date = (EditText) findViewById(R.id.etLeave_date);
        etLeave_description = (EditText) findViewById(R.id.etLeave_description);
        bLeave_apply = (Button) findViewById(R.id.bLeave_apply);
        tvLeave_status_current = (TextView) findViewById(R.id.tvLeave_status_current);
        lvStatus = (ListView)findViewById(R.id.lvStatus);

        bLeaveStatusDetail = (Button) findViewById(R.id.bLeaveStatusDetail);


        Intent i = getIntent();
        //Username = i.getStringExtra(Config.Username_Pref);
        Username = i.getStringExtra(Config.Username_Pref_Time);


        /*if (Username.matches("")) {
            Username = i.getStringExtra(Config.Username_Pref_Time);
        }*/

        final TextView tvLeave_status = (TextView) findViewById(R.id.tvLeave_status);

        getStatus();

        etLeave_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();

                year_x = c.get(Calendar.YEAR);
                month_x = c.get(Calendar.MONTH);
                day_x = c.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(applyLeave.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        String date = i2 + "-" + (i1 +1) + "-"+ i;
                        etLeave_date.setText(date);
                        Toast.makeText(applyLeave.this, date, Toast.LENGTH_LONG).show();
                    }
                },year_x,month_x,day_x);
                datePickerDialog.show();
            }
        });

        bLeave_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(applyLeave.this, "Leave Applied", Toast.LENGTH_LONG).show();
                addLeaveDate();
            }
        });

        bLeaveStatusDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(applyLeave.this, applyLeaveDetail.class);
                i.putExtra(Config.Username_Pref_Time, Username);
                startActivity(i);
            }
        });



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
        tvLeave_status_current.setText(Status_and_count);
    }

    private void addLeaveDate(){
        final String Leave_date = etLeave_date.getText().toString().trim();
        final String Leave_description = etLeave_description.getText().toString().trim();

        class AddLeaveDate extends AsyncTask<Void,Void,String> {
            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put(T_Leave_date,Leave_date);
                hashMap.put(T_Leave_description,Leave_description);
                hashMap.put(T_Username,Username);

                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest(Config.applyLeave_url, hashMap);
                return s;
            }
        }
        AddLeaveDate ald = new AddLeaveDate();
        ald.execute();
    }

}









        //Calendar cal=Calendar.getInstance();
        /*calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month+1, day +1);
    }

    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
        Toast.makeText(getApplicationContext(), "ca", Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this, myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
            // arg1 = year
            // arg2 = month
            // arg3 = day
            showDate(arg1, arg2+1, arg3 +1);
        }
    };

    private void showDate(int year, int month, int day) {
        dateView.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }*//*

        Calendar calendar = Calendar.getInstance();

        year_x = calendar.get(Calendar.YEAR);
        month_x = calendar.get(Calendar.MONTH);
        day_x = calendar.get(Calendar.DAY_OF_MONTH);

        showDialogOnButtonClicked();
    }

    public void showDialogOnButtonClicked(){
        //Button bDatePicker = (Button)findViewById(R.id.bDatePicker);

        /*bDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(Dialog_ID);
            }
        });*/
    //}



    /*@Override
    protected Dialog onCreateDialog(int id){
        if (id ==Dialog_ID)
            return new DatePickerDialog(this, dpListener, year_x,month_x,day_x);
        return null;
    }

    private DatePickerDialog.OnDateSetListener dpListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            year_x = i;
            month_x=i1 +1;
            day_x=i2;
            Toast.makeText(applyLeave.this, year_x + "/" +month_x + "/" +day_x , Toast.LENGTH_LONG).show();
        }
    };*/
