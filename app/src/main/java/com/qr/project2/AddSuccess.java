package com.qr.project2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class AddSuccess extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_success);

        final ImageView imageView = (ImageView)findViewById(R.id.imageView);
        final Button bHome = (Button) findViewById(R.id.bHome);

        final Button bLogout = (Button) findViewById(R.id.bLogout);

        Bitmap bitmap = getIntent().getParcelableExtra("pic");
        imageView.setImageBitmap(bitmap);

        bHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AddSuccess.this, adminHome.class);
                startActivity(i);
            }
        });

        bLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddSuccess.this, mainPage.class);
                if(Build.VERSION.SDK_INT >= 11) {
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                } else {
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                }
                Toast toast = Toast.makeText(AddSuccess.this, "Logout Success", Toast.LENGTH_SHORT);
                toast.show();
                startActivity(intent);
            }
        });
    }
}
