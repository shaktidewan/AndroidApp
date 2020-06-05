package com.example.gainyourmuscle;

import androidx.appcompat.app.ActionBar;
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

        //Actionbar And it's title
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Information");

        //enable back button
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        //go to previous activity
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
