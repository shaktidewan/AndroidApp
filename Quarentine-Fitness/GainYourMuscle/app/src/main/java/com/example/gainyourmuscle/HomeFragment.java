package com.example.gainyourmuscle;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {

    ArrayList<GymModel> gymModelClasses = new ArrayList<>();
    private RecyclerView rv;
    private GymAdpater gymAdpater;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_home,container,false);
        //For Searching we use filter
        EditText editText = view.findViewById(R.id.editText);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

        rv= view.findViewById(R.id.gym_recyclerview);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));

        getGymResponse();

        return view;
    }

    private void filter(String text){
        ArrayList<GymModel> filteredList = new ArrayList<>();

        for(GymModel item:gymModelClasses){
            if(item.getName().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(item);
            }
        }
        gymAdpater.filterList(filteredList);

    }

    private void getGymResponse() {

        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://yakhha012.github.io/AndroidApp/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RequestInterface requestInterface = retrofit.create(RequestInterface.class);
        Call<List<GymModel>> mycall =requestInterface.getGymJson();

        mycall.enqueue(new Callback<List<GymModel>>() {
            @Override
            public void onResponse(Call<List<GymModel>> call, Response<List<GymModel>> response) {
                gymModelClasses = new ArrayList<>(response.body());

                //adpater ko kaam
                gymAdpater = new GymAdpater(gymModelClasses,HomeFragment.this);
                rv.setAdapter(gymAdpater);
            }

            @Override
            public void onFailure(Call<List<GymModel>> call, Throwable t) {

                Toast.makeText(getActivity(), "INTERNET IS NOT CONNECTED", Toast.LENGTH_LONG).show();
            }
        });

    }

}
