package com.example.collegeapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MessageFragment extends Fragment  {
    private FirebaseDatabase mDatabase;
    private  DatabaseReference mRef;
    private EditText eText;
    private TextView vText;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.message_frag,container,false);

        Button insert = view.findViewById(R.id.inser_id);
        Button read = view.findViewById(R.id.button2);

        eText = view.findViewById(R.id.editTexit1);
        vText = view.findViewById(R.id.textView2);

        mDatabase =FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference("users");



        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data =eText.getText().toString();
                mRef.child("user1").setValue(data);
                Toast.makeText(getActivity(), "Successful", Toast.LENGTH_SHORT).show();
            }
        });

        read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRef.child("user1").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String data = dataSnapshot.getValue(String.class);
                        vText.setText(data);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });


        return view;
    }


}
