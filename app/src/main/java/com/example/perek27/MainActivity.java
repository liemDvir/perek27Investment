package com.example.perek27;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.Firebase;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    FirebaseAuth firebaseAuth;
    Button loginButton,goRegister;
    EditText loginEmail, loginPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        firebaseAuth = FirebaseAuth.getInstance();

        loginButton = (Button)findViewById(R.id.loginBtn);
        loginButton.setOnClickListener(this);

        loginEmail = (EditText)findViewById(R.id.emailLogin);
        loginEmail.setOnClickListener(this);

        loginPassword = (EditText)findViewById(R.id.passwordLogin);
        loginPassword.setOnClickListener(this);

        goRegister = (Button)findViewById(R.id.goToRegister);
        goRegister.setOnClickListener(this);

    }

    @Override
    public void onClick(View view)
    {
        if(view == loginButton) {
            if (loginButton.getText().equals("login"))
            {

                String email = "";
                String password = "";
                if (loginEmail != null && !loginEmail.equals("") && loginPassword != null && !loginPassword.equals("")) {
                    email = loginEmail.getText().toString();
                    password = loginPassword.getText().toString();

                    firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(MainActivity.this, "Successfully logged in", Toast.LENGTH_LONG).show();
                                loginButton.setText("Logout");
                            } else {
                                Toast.makeText(MainActivity.this, " Error ", Toast.LENGTH_LONG).show();

                            }
                        }
                    });
                } else {
                    Toast.makeText(MainActivity.this, "Not entered email or pass", Toast.LENGTH_LONG).show();
                }
            } else if (loginButton.getText().equals("logout"))
            {
                firebaseAuth.signOut();
            }
        } else if (view == goRegister)
        {
            Intent intent = new Intent(MainActivity.this,register.class);
            startActivity(intent);
        }
    }
}