package com.qr.project2;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rizky on 7/14/2016.
 */
public class editAddRequest extends StringRequest{
//extends from volley gradle
    private final static String Add_REQUEST_URL = "http://tryqr123.tk/AddProfile.php";
    private Map<String, String> params;

    public editAddRequest (String Name, String Username, String Email, String Password, Response.Listener<String>listener){
//constructor to pass data
        super (Method.POST, Add_REQUEST_URL, listener, null);
//method super to pass data to volley
        params = new HashMap<>();
        params.put("Name",      Name);
        params.put("Username",  Username);
        params.put("Email",     Email);
        params.put("Password",  Password);
//put data into params
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
