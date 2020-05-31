package com.example.gainyourmuscle;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FavouriteFragment extends Fragment {

    MediaPlayer songMusic;
    private Button playM,pauseM;
    //for timer
    private Button tstart,tpause,treset;
    private Chronometer chronometer;
    private long pauseOffset;
    private boolean running;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favourites,container,false);
        songMusic = MediaPlayer.create(getActivity().getApplicationContext(),R.raw.song);
        Switch switch_looping = view.findViewById(R.id.switch_looping);
        switch_looping.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                songMusic.setLooping(isChecked);
            }
        });

        playM = view.findViewById(R.id.playMusic);
        pauseM = view.findViewById(R.id.pauseMusic);

        playM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                songMusic.start();
            }
        });

        pauseM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(songMusic.isPlaying())
                    songMusic.pause();
            }
        });
        //FOR TIMER
        chronometer = view.findViewById(R.id.chronometer);
        tstart =view.findViewById(R.id.start);
        tpause =view.findViewById(R.id.pause);
        treset =view.findViewById(R.id.reset);

        tstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startChronometer(v);
            }
        });

        tpause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseChronometer(v);
            }
        });

        treset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetChronometer(v);
            }
        });


        return view;
    }
    //end of oncreateview



    public void startChronometer(View view){
        if(!running){
            chronometer.setBase(SystemClock.elapsedRealtime()-pauseOffset);
            chronometer.start();
            running=true;
        }

    }

    public void pauseChronometer(View view){
        if (running){
            chronometer.stop();
            pauseOffset =SystemClock.elapsedRealtime()-chronometer.getBase();
            running=false;
        }
    }


    public void resetChronometer(View view){
        chronometer.setBase(SystemClock.elapsedRealtime());
        pauseOffset=0;

    }
}
