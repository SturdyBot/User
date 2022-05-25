package com.example.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import soup.neumorphism.NeumorphButton;

public class Login extends AppCompatActivity {

    EditText editEmail, editPassword;
    Button Login_button;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editEmail = (EditText) findViewById(R.id.Login_email);
        editPassword = (EditText) findViewById(R.id.Login_password);
        Login_button = findViewById(R.id.Login_button);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        Login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performLogin();
            }
        });
    }

    private void performLogin() {
        String email = editEmail.getText().toString();
        String password = editPassword.getText().toString();

        if (!email.matches(emailPattern)) {
            editEmail.setError("Enter Correct Email");
        }
        if (password.isEmpty()) {
            editPassword.setError("Enter Password");
        } else {
            progressDialog.setTitle("User Logging in");
            progressDialog.setMessage("Checking Credentials...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()) {
                        progressDialog.dismiss();
                        LoginActivity();
                        Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT).show();

                    }
                    else{
                            progressDialog.dismiss();
                            Toast.makeText(Login.this,"Invalid Credentials"+"\n"+task.getException(),Toast.LENGTH_SHORT).show();}
                    
                }
            });
        }
    }

    private void LoginActivity() {
        Intent intent= new Intent(Login.this,HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}