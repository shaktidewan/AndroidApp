package com.example.collegeapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private RecyclerView rv;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_frag,container,false);
        rv = (RecyclerView) view.findViewById(R.id.rv_id);

        LinearLayoutManager lilama = new LinearLayoutManager(getActivity());
        lilama.setOrientation(RecyclerView.VERTICAL);
        rv.setLayoutManager(lilama);

        List<GymModelClass> gymList = new ArrayList<>();
        gymList.add(new GymModelClass(R.drawable.ic_launcher_background,"Active Recovery","This is one way to spend your “rest” day. So instead of lounging on the couch all day you’ll schedule some sort of low-intensity activity like light walking or gentle yoga. "));
        gymList.add(new GymModelClass(R.drawable.ic_launcher_background,"Aerobic Exercise","“Often we call all cardio ‘aerobics,’ but aerobic is actually a specific energy system,” explains Lefkowith."));
        gymList.add(new GymModelClass(R.drawable.ic_launcher_background,"Anaerobic Exercise","On the other hand, your anaerobic energy system is taxed when you do high-intensity workouts that skyrocket your heart rate."));
        gymList.add(new GymModelClass(R.drawable.ic_launcher_background,"Boot Camp","These classes are rooted in military-style training, so are typically pretty tough, and they often include a combination of cardio and strength exercises. "));
        gymList.add(new GymModelClass(R.drawable.ic_launcher_background," Circuit","Think of this as a “round” of exercises. For example, in this bodyweight circuit workout, one circuit consists of 5 burpees, 10 push-ups, 15 plank jacks, and 20 jump squats."));
        gymList.add(new GymModelClass(R.drawable.ic_launcher_foreground,"Compound Exercises","A compound exercise is a move that incorporates multiple muscle groups, like lunges, deadlifts, and squats."));
        gymList.add(new GymModelClass(R.drawable.ic_launcher_foreground,"Cool-Down","This is what you do at the end of your workout. The goal is to gradually bring your body back to a resting state by lowering your heart rate and calming your nervous system."));
        gymList.add(new GymModelClass(R.drawable.ic_launcher_foreground," Cross-Training","Cross-training means mixing in different workouts and training methods rather than focusing on just one type of workout."));

        ToriAdpater haudeAdpater = new ToriAdpater(gymList);
        rv.setAdapter(haudeAdpater);

        return view;

    }
}
