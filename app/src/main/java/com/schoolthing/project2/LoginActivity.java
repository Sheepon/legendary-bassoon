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
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText etUserId,etPassword;
    private Button btLogin,btRegister;

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.btRegister:
                    Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                    startActivity(i);
                    break;
                case R.id.btLogin:
                    loginToMain();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

        etUserId = this.findViewById(R.id.etUserId);
        etPassword = this.findViewById(R.id.etPassword);
        btLogin = this.findViewById(R.id.btLogin);
        btRegister = this.findViewById(R.id.btRegister);


        btLogin.setOnClickListener(listener);
        btRegister.setOnClickListener(listener);

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();

    }


    private void loginToMain(){
        String email = etUserId.getText().toString();
        String password = etPassword.getText().toString();

        if(email.isEmpty()){
            etUserId.setError("Please input email");
            etUserId.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            etUserId.setError("Invalid email address");
            etUserId.requestFocus();
            return;
        }

        if(password.isEmpty()){
            etPassword.setError("Please input password");
            etPassword.requestFocus();
            return;
        }


        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user = mAuth.getCurrentUser();
                            //Intent i = new Intent(LoginActivity.this,MainActivity.class);
                            Intent i = new Intent(LoginActivity.this,navigation.class);
                            startActivity(i);
                        }else{
                            Toast.makeText(LoginActivity.this, "Login failed! Please try again",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}

