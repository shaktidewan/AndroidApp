package com.example.collegeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button login,register;
    EditText user,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user = (EditText) findViewById(R.id.etUser);
        password = (EditText) findViewById(R.id.etPassword);
        login = (Button) findViewById(R.id.buttonLogin);
        register = (Button) findViewById(R.id.buttonRegister);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //auth
                String name = user.getText().toString();
                String pwd = password.getText().toString();

                if (name.isEmpty() ){
                    Toast.makeText(MainActivity.this, "User Blank", Toast.LENGTH_SHORT).show();

                } else if (pwd.isEmpty()){
                    Toast.makeText(MainActivity.this, "Password Blank", Toast.LENGTH_SHORT).show();

                }
                else if(name.equals("admin") && pwd.equals("admin")){
                    startActivity(new Intent(MainActivity.this,Home.class));
                }else{
                    Toast.makeText(MainActivity.this, "Invalid Vo hau", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}
