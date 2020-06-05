package com.example.gainyourmuscle;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;


import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class ScheduleFragment extends Fragment implements View.OnClickListener {
    private  int notificationId =1;
    View view;

    public ScheduleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =inflater.inflate(R.layout.fragment_schedule, container, false);

        view.findViewById(R.id.setBtn).setOnClickListener(this);
        view.findViewById(R.id.cancelBtn).setOnClickListener(this);


        return view;
    }


    @Override
    public void onClick(View v) {

        EditText editText = view.findViewById(R.id.editText);
        TimePicker timePicker =view.findViewById(R.id.timePicker);

        //set notification and text
        Intent intent = new Intent(getActivity(),AlarmReceiver.class);
        intent.putExtra("notificationId",notificationId);
        intent.putExtra("todo",editText.getText().toString());

        //getBroadcast(context,requestCode,intent,flag)
        PendingIntent alarmIntent = PendingIntent.getBroadcast(getContext(),0,intent,PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager alarm = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

        switch (v.getId()){

            case  R.id.setBtn:
                int hour = timePicker.getCurrentHour();
                int minute = timePicker.getCurrentMinute();

                //Create time
                Calendar startTime = Calendar.getInstance();
                startTime.set(Calendar.HOUR_OF_DAY,hour);
                startTime.set(Calendar.MINUTE,minute);
                startTime.set(Calendar.SECOND,0);
                long alarmStartTime = startTime.getTimeInMillis();

                //set alarm
                //set(type, millisecond,intent)
                alarm.set(AlarmManager.RTC_WAKEUP, alarmStartTime, alarmIntent);

                Toast.makeText(getActivity(), "Done", Toast.LENGTH_SHORT).show();
                break;

            case R.id.cancelBtn:
                alarm.cancel(alarmIntent);
                Toast.makeText(getActivity(), "Cancelled", Toast.LENGTH_SHORT).show();
                break;

        }


    }
}
