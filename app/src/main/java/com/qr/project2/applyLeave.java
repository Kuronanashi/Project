package com.qr.project2;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.icu.util.Calendar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

public class applyLeave extends AppCompatActivity {
    int year_x,month_x, day_x;
    static final int Dialog_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_leave);

        /*Calendar calendar = Calendar.getInstance();

        year_x = calendar.get(Calendar.YEAR);
        month_x = calendar.get(Calendar.MONTH);
        day_x = calendar.get(Calendar.DAY_OF_MONTH);

        showDialogOnButtonClicked();
    }

    public void showDialogOnButtonClicked(){
        Button bDatePicker = (Button)findViewById(R.id.bDatePicker);

        bDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(Dialog_ID);
            }
        });
    }

    @Override
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
    }
}
