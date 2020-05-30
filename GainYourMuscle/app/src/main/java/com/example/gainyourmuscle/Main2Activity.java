package com.example.gainyourmuscle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class Main2Activity extends AppCompatActivity {

    TextView name,desc;
    String tname,tdesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        name=findViewById(R.id.name);
        desc=findViewById(R.id.desc);

        tname=getIntent().getStringExtra("name");
        tdesc=getIntent().getStringExtra("desc");
        name.setText(tname);
        desc.setText(tdesc);
    }
}
