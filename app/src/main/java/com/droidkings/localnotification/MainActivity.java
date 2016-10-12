package com.droidkings.localnotification;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    AlarmManager am;  //This should be Global Variable
    PendingIntent pendingIntent;///Global variable
    TextView switchStatus,timetv;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Switch_Mode =  "switch_mode";
    public static final String Hour = "hourKey";
    public static final String Minute = "minutesKey";
    SharedPreferences sharedpreferences;
    Switch firstSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Button set = (Button) findViewById(R.id.setime);

        timetv = (TextView) findViewById(R.id.alarmTime);
         firstSwitch = (Switch) findViewById(R.id.switch1);

       firstSwitch.setChecked(read_sharedprefarance());
        read_time();
        /**Edit the shared preference*/
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        //editor.putString(Phone, ph);String
        //editor.putString(Email, e);

        switchStatus = (TextView) findViewById(R.id.status);

        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(888);
            }
        });

        firstSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                if(isChecked){
                    SharedPreferences sharedPref = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

                    int hour = sharedPref.getInt(Hour,0);
                    int minute = sharedPref.getInt(Minute,0);
                    notification(hour,minute);
                   editor.putBoolean(Switch_Mode, true);
                    switchStatus.setText("ON");
                }else{
                    editor.putBoolean(Switch_Mode,false);
                    if (am!= null) {
                        am.cancel(pendingIntent);//Cancel the pre set Alarm From Alarm manager
                        pendingIntent.cancel();//Release The panding intent
                    }
                    switchStatus.setText("OFF");
                }
                editor.commit();

            }
        });

        //check the current state before we display the screen
        if(firstSwitch.isChecked()){
            switchStatus.setText("ON");
        }
        else {
            switchStatus.setText("OFF");
        }
    }
    public boolean read_sharedprefarance(){
        SharedPreferences sharedPref = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        //int defaultValue = getResources().getInteger(R.string.saved_high_score_default);\

        boolean mode = sharedPref.getBoolean(Switch_Mode,false);

        return mode;
    }
    public void read_time()
    {
        SharedPreferences sharedPref = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        int hour = sharedPref.getInt(Hour,0);
        int minute = sharedPref.getInt(Minute,0);

        String time = showTime(hour, minute);
        timetv.setText(time);


    }
    private TimePickerDialog.OnTimeSetListener myTimeListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

         //  notification(hourOfDay, minute);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putInt(Hour, hourOfDay);
            editor.putInt(Minute,minute);
            editor.commit();
            read_time();


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
        calendar.set(Calendar.MINUTE,m);
        calendar.set(Calendar.SECOND, 0);

        Toast.makeText(this,  showTime(h,m), Toast.LENGTH_SHORT).show();

        Intent intent1 = new Intent(MainActivity.this, MyReceiver.class);
       pendingIntent = PendingIntent.getBroadcast(
                MainActivity.this, 0 , intent1,
                PendingIntent.FLAG_UPDATE_CURRENT);

        am = (AlarmManager) MainActivity.this
                .getSystemService(MainActivity.this.ALARM_SERVICE);

        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                1000*60, pendingIntent);
       // am.cancel(pendingIntent);
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
