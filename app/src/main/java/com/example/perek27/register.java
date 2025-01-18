package com.example.perek27;

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

import java.util.Date;

public class register extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth firebaseAuth;
    Button registerButton, dateOfBirthButton;
    EditText emailRegister, passwordRegister,firstNameRegister, lastNameRegister;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        firebaseAuth = FirebaseAuth.getInstance();

        registerButton = (Button)findViewById(R.id.registerBtn);
        registerButton.setOnClickListener(this);

        emailRegister = (EditText)findViewById(R.id.emailRegister);
        emailRegister.setOnClickListener(this);

        passwordRegister = (EditText)findViewById(R.id.passwordRegister);
        passwordRegister.setOnClickListener(this);

        firstNameRegister = (EditText)findViewById(R.id.firstname);
        firstNameRegister.setOnClickListener(this);

        lastNameRegister = (EditText)findViewById(R.id.lastName);
        lastNameRegister.setOnClickListener(this);

        dateOfBirthButton= (Button)findViewById(R.id.dateOfBirth);
        dateOfBirthButton.setOnClickListener(this);


    }

    @Override
    public void onClick(View view)
    {
        String email = "";
        String password = "";
        String firstName = "";
        String lastName = "";
        Date date = new Date();
        if(emailRegister != null && !emailRegister.equals("") && passwordRegister != null && !passwordRegister.equals("") && firstName != null && !firstName.equals("") && lastName != null && !lastName.equals(""))
        {
            email = emailRegister.getText().toString();
            password = passwordRegister.getText().toString();

            firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task)
                {
                    if (task.isSuccessful())
                    {
                        Toast.makeText(register.this, "Successfully registered", Toast.LENGTH_LONG).show();
                        registerButton.setText("logout");

                    }
                    else
                    {
                        Toast.makeText(register.this, "Registration Error", Toast.LENGTH_LONG).show();
                    }

                }
            });
        }
        else
        {
            Toast.makeText(register.this, "Not entered email or pass", Toast.LENGTH_LONG).show();
        }

    }
}