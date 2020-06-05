package com.example.gainyourmuscle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    //views
    EditText mEmailEt,mPasswordEt,mConfirmPasswordEt;
    Button mRegisterBtn;
    TextView mHaveAccountTv;

    //Progress bar to shown while registrating user
    ProgressDialog progressDialog;

    //Declare an instance of FirebaseAuth
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Actionbar And it's title
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Create Account");

        //enable back button
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        //INIT
        mEmailEt = findViewById(R.id.emailEt);
        mPasswordEt = findViewById(R.id.passwordEt);
        mConfirmPasswordEt = findViewById(R.id.confirmPasswordEt);
        mRegisterBtn = findViewById(R.id.registerBtn);
        mHaveAccountTv = findViewById(R.id.have_accountTv);

        //initialize the FirebaseAuth instance
        mAuth = FirebaseAuth.getInstance();

        //for ProgressBar
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("User Registeration");

        //handle registration button here:
        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //input email,password,confirm password
                String email = mEmailEt.getText().toString().trim();
                String password = mPasswordEt.getText().toString().trim();
                String confirmpassword = mConfirmPasswordEt.getText().toString().trim();
                //validation
                if ( !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    //set error and focus to email editText
                    mEmailEt.setError("INVALID ERROR");
                    mEmailEt.setFocusable(true);
                }
                if (password.length() <6){
                    //set error and focus to password edittext
                    mPasswordEt.setError("Password length at least 6 characters");
                    mPasswordEt.setFocusable(true);
                }
                if (password.equals(confirmpassword))
                {
                    registerUser(email,password);//register the user
                }else {
                    Toast.makeText(RegisterActivity.this, "Password doesnot matches", Toast.LENGTH_SHORT).show();
                }


            }
        });
        ///HANDLE LOGIN TEXTVIEW BUTTON
        mHaveAccountTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                finish();
            }
        });

    }

    //FIREBASE REGISTRATION IN THIS METHOD:
    private void registerUser(String email, String password) {
        //EMAIL AND PASSWORD PATTERN IS VALID, SHOWING DIALOG AND START REGISTRATION:
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, dismiss dailog and start register acitvity
                            progressDialog.dismiss();
                            FirebaseUser user = mAuth.getCurrentUser();
                            //Get user email and uid from auth
                            String email =user.getEmail();
                            String uid =user.getUid();
                            //When user is registered store user info in firebase realtime database too
                            //Using HashMap
                            HashMap<Object, String> hashMap = new HashMap<>();
                            //put inforamtion in hashmap
                            hashMap.put("email", email);
                            hashMap.put("uid", uid);
                            hashMap.put("name", "");//will add later
                            hashMap.put("phone", "");//will add later
                            hashMap.put("image", "");//will add later
                            hashMap.put("gender", "");//will add later
                            hashMap.put("dateOfBirth", "");//will add later
                            //firebase database instance
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            //path to store user data named="User:"
                            DatabaseReference reference = database.getReference("Users");
                            //put data within hashmap in database
                            reference.child(uid).setValue(hashMap);


                            Toast.makeText(RegisterActivity.this, "Registering User. "+user.getEmail(), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this, DashboardActivity.class));
                            finish();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            progressDialog.dismiss();
                            Toast.makeText(RegisterActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //ERROR DISMISS PROGRESS DIALOG AND GET AND SHOR THE ERROR MESSAGE
                progressDialog.dismiss();
                Toast.makeText(RegisterActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }


    @Override
    public boolean onSupportNavigateUp() {
        //go to previous activity
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
