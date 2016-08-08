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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class viewPayroll extends AppCompatActivity {

    /*private final static String T_Username = "Username";
    private final static String T_Time_Detail = "Time_Detail";
    private final static String T_Time_Logout = "Time_Logout";
    private final static String T_Time_Date = "T_date";

    ArrayList<HashMap<String, String>> params;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_payroll);

        /*final AlertDialog.Builder builder = new AlertDialog.Builder(viewPayroll.this);

        SharedPreferences pref = getSharedPreferences(Config.Pref_Name, Context.MODE_PRIVATE);
        final String Username = pref.getString(Config.Username_Pref_Time, "Empty");
        //final String log_url = pref.getString(Config.Logout_url, "Empty");

        RequestQueue queue = Volley.newRequestQueue(this);
        //RequestQueue queue2 = Volley.newRequestQueue(this);

        final ListView List = (ListView) findViewById(R.id.listview);
        String url = "http://tryqr123.tk/getTime.php";
        //String url2 = "http://tryqr123.tk/getLogoutTime.php";

        final Button bLogout = (Button) findViewById(R.id.bLogout);

        //params = new ArrayList<HashMap<String, String>>();
        params = new ArrayList<>();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray("result");

                    String format = "dd-MM-yyyy HH:mm:ss";
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

                        //HashMap<String, String> detail_params = new HashMap<String, String>();
                        HashMap<String, String> detail_params = new HashMap<>();


                        detail_params.put(T_Username, Username);
                        detail_params.put(T_Time_Detail, Time_Detail);
                        detail_params.put(T_Time_Logout, Time_Logout);
                        detail_params.put(T_Time_Date, dateFormat);

                        params.add(detail_params);
                    }

                    ListAdapter adapter = new SimpleAdapter(viewPayroll.this, params, R.layout.get_payroll,
                            new String[]{T_Username,T_Time_Detail,T_Time_Logout,T_Time_Date},
                            new int[]{R.id.Username, R.id.Time_Detail, R.id.Time_Logout, R.id.Time_Date}
                    );


                    List.setAdapter(adapter);
                } catch (JSONException | ParseException e) {
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


        queue.add(stringRequest);

        bLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.setMessage("Proceed?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                /*StringRequest stringRequest1 = new StringRequest(Request.Method.POST, url2, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            /*JSONObject jsonResponse = new JSONObject(response);
                                            int getTimeLogout = jsonResponse.getInt("getTimeMessage");

                                            String a = jsonResponse.getString("Name");

                                            if (getTimeLogout == 1) {
                                                Toast toast = Toast.makeText(viewPayroll.this, "Logout Recorded", Toast.LENGTH_SHORT);
                                                toast.show();
                                            }
                                            else {
                                                Toast toast = Toast.makeText(viewPayroll.this, "Logout Not Recorded", Toast.LENGTH_SHORT);
                                                toast.show();
                                            }*/
                                        /*} catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }){
                                    protected Map<String, String> getParams() {
                                        Map<String, String> params = new HashMap<>();
                                        params.put(T_Username, Username);
                                        return params;
                                    }
                                };

                                RequestQueue queue = Volley.newRequestQueue(viewPayroll.this);
                                queue.add(stringRequest1);*/

                                /*SharedPreferences pref = getSharedPreferences(Config.Pref_Name, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editorPref = pref.edit();

                                editorPref.putBoolean(Config.Login_Status_Pref, false);
                                editorPref.putBoolean(Config.Login_Status_Pref_Admin, false);
                                editorPref.putString(Config.Name_Pref, "");
                                editorPref.putString(Config.Username_Pref, "");
                                editorPref.putString(Config.Email_Pref, "");
                                editorPref.putString(Config.Password_Pref, "");
                                editorPref.putString(Config.Username_Pref_Time, "");
                                editorPref.apply();

                                Intent intent = new Intent(viewPayroll.this, mainPage.class);
                                Toast toast = Toast.makeText(viewPayroll.this, "Logout Success", Toast.LENGTH_SHORT);
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
}


