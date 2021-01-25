package com.schoolthing.project2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Button btRegister;
    private EditText etRPassword,etPassword,etuserid,etName;
    String password;
    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.btRegister:
                    register();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        btRegister = this.findViewById(R.id.btRegister);
        etRPassword = this.findViewById(R.id.etRPassword);
        etPassword = this.findViewById(R.id.etPassword);
        etuserid = this.findViewById(R.id.etuserid);
        etName = this.findViewById(R.id.etName);

        btRegister.setOnClickListener(listener);
    }

    private void DefaultData(){
        Budget budget = new Budget(0.0f,0.0f);
        FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("Account").setValue(budget);
    }

    private void register(){
        String email = etuserid.getText().toString();
        String name = etName.getText().toString();
        if(etPassword.getText().toString().equals(etRPassword.getText().toString())){
            password = etPassword.getText().toString();
        }else{
            Toast.makeText(this, "Please check your password again", Toast.LENGTH_SHORT).show();
            return;
        }

        if(name.isEmpty()){
            Toast.makeText(this, "Please type in your name", Toast.LENGTH_SHORT).show();
            return;
        }
        if(email.isEmpty()){
            Toast.makeText(this, "Please type in  your email", Toast.LENGTH_SHORT).show();
            return;
        }

        if(etPassword.getText().toString().isEmpty()){
            Toast.makeText(this, "Please type in your password", Toast.LENGTH_SHORT).show();
            return;
        }

        if(etRPassword.getText().toString().isEmpty()){
            etRPassword.setError("Please type in your password again");
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(this, "Please type in a valid email address", Toast.LENGTH_SHORT).show();
            return;
        }


        //Authentication
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //DefaultData();
                        if(task.isSuccessful()){
                            //Database
                            User user = new User(email,name);
                            //FirebaseUser user = mAuth.getCurrentUser();
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user);
                            Toast.makeText(RegisterActivity.this, "Successfully registered", Toast.LENGTH_SHORT).show();
                            DefaultData();
                            Intent i = new Intent(RegisterActivity.this,LoginActivity.class);
                            startActivity(i);
/*
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(RegisterActivity.this, "Successfully Registered", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(RegisterActivity.this, "Registration failed, Please try again", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });*/
                        }else
                        {
                            Toast.makeText(RegisterActivity.this, "Registration failed, Please try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                });



    }
}