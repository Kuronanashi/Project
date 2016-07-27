package com.qr.project2;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class qrtest extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrtest);

        final EditText etEdit1 = (EditText) findViewById(R.id.etEdit1);
        final EditText etEdit2 = (EditText) findViewById(R.id.etEdit2);
        final Button bHensin = (Button) findViewById(R.id.bHensin);
        final Button bHO = (Button) findViewById(R.id.bHO);
        final Button bDelete = (Button) findViewById(R.id.bDelete);

        final TextView tvChange = (TextView) findViewById(R.id.tvChange);
        final TextView tvChange2 = (TextView) findViewById(R.id.tvChange2);


        final SharedPreferences prefShare = getSharedPreferences("login.config", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editorPref = prefShare.edit();

        bHensin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String a = etEdit1.getText().toString();
                String b = etEdit2.getText().toString();

                editorPref.putString("Data 1",a);
                editorPref.putString("Data 2",b);
                editorPref.apply();
            }
        });

        bHO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String x= prefShare.getString("Data 1", "");
                String y= prefShare.getString("Data 2", "");

                tvChange.setText(x);
                tvChange2.setText(y);

            }
        });

        bDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editorPref.clear();
                editorPref.commit();

            }
        });
    }

}
