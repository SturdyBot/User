package com.example.user;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import soup.neumorphism.NeumorphButton;


public class MainActivity extends AppCompatActivity {

    EditText editName, editEmail, editPhone, editPassword, confirmPassword;
    NeumorphButton SignUp_button;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    FirebaseUser mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editName = (EditText) findViewById(R.id.name);
        editEmail = (EditText) findViewById(R.id.email);
        editPhone = (EditText) findViewById(R.id.mobile_number);
        editPassword = (EditText) findViewById(R.id.password);
        confirmPassword = (EditText) findViewById(R.id.confirm_password);
        SignUp_button = (NeumorphButton) findViewById(R.id.register_button);
        progressDialog= new ProgressDialog(this);
        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();

        //Start Login Activity
        TextView Login = (findViewById(R.id.Login));
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginClick = new Intent(getApplicationContext(), Login.class);
                startActivity(loginClick);
            }
        });

        SignUp_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PerformAuth();
            }
        });
    }

    private void PerformAuth() {
        String name= editName.getText().toString();
        String email= editEmail.getText().toString();
        String phone= editPhone.getText().toString();
        String password= editPassword.getText().toString();
        String checkPassword= confirmPassword.getText().toString();

        if(!email.matches(emailPattern)){
            editEmail.setError("Enter Correct Email");
        }
        if(password.isEmpty()){
            editPassword.setError("Enter Password");
        }
        if(!password.equals(checkPassword)) {
        confirmPassword.setError("Passwords doesn't match");
        }
        if(name.isEmpty()) {
                editName.setError("Enter Name");
            }
        if(phone.length()!= 10) {
            editPhone.setError("Enter valid phone number");
        }
        else {
            progressDialog.setTitle("Registering new User");
            progressDialog.setMessage("Registering...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        progressDialog.dismiss();
                        sendUserToNextActivity();
                        Toast.makeText(MainActivity.this,"Registration Successful",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this,""+task.getException(),Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    private void sendUserToNextActivity() {
        Intent intent= new Intent(MainActivity.this,Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}