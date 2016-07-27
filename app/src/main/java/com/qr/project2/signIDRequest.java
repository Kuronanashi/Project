package com.qr.project2;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rizky on 7/19/2016.
 */
public class signIDRequest extends StringRequest{

    private static final String SignID_REQUEST_URL = "http://tryqr123.tk/SignInID.php";
    private Map<String, String> params;

    public signIDRequest (String Username, String Password, Response.Listener<String> listener){
        super(Request.Method.POST, SignID_REQUEST_URL, listener, null);

        params = new HashMap<>();
        params.put("Username", Username);
        params.put("Password", Password);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
