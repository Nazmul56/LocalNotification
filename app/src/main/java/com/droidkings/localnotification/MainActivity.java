package com.droidkings.localnotification;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Button set = (Button) findViewById(R.id.setime);
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(888);
            }
        });
    }
    private TimePickerDialog.OnTimeSetListener myTimeListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
           notification(hourOfDay,minute);
        }
    };

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        // showDate(year, month + 1, day);
        Calendar calendar;
        calendar = Calendar.getInstance();
       if (id == 888){
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int min = calendar.get(Calendar.MINUTE);
            return new TimePickerDialog(this, myTimeListener, hour, min, false);
        }else
            return null;
    }
    private void notification(int h ,int m){
        //Locale alocal = new Locale

        Calendar calendar = Calendar.getInstance();
        // we can set time by open date and time picker dialog
        calendar.set(Calendar.HOUR_OF_DAY, h);
        calendar.set( Calendar.MINUTE,m);
        calendar.set(Calendar.SECOND, 0);

        Toast.makeText(this,  showTime(h,m), Toast.LENGTH_SHORT).show();

        Intent intent1 = new Intent(MainActivity.this, MyReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                MainActivity.this, 0 , intent1,
                PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager am = (AlarmManager) MainActivity.this
                .getSystemService(MainActivity.this.ALARM_SERVICE);

        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    public String showTime(int hour, int min) {
        String format;
        if (hour == 0) {
            hour += 12;
            format = "AM";
        }
        else if (hour == 12) {
            format = "PM";
        } else if (hour > 12) {
            hour -= 12;
            format = "PM";
        } else {
            format = "AM";
        }
        return hour+":"+min+" "+format;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
