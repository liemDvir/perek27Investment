package com.example.perek27;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {



    StockModel mStockModel;
    Button loginButton,goRegister;
    EditText loginEmail, loginPassword;

    private Observer mMainActivityObserver = new MainActivityObserver();

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

        mStockModel = StockModel.GetInstance();
        mStockModel.Init();
        mStockModel.register(mMainActivityObserver);

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
                Log.d("Strting the function", (String) loginButton.getText());

                String email = "";
                String password = "";
                if ((loginEmail != null) && (!loginEmail.getText().toString().isEmpty()) && (loginPassword != null) && (!loginPassword.getText().toString().isEmpty())) {
                    email = loginEmail.getText().toString();
                    password = loginPassword.getText().toString();
                    loginButton.setEnabled(false);

                    mStockModel.SignInWithEmailAndPassword(email,password);
                } else {
                    Toast.makeText(MainActivity.this, "Not entered email or pass", Toast.LENGTH_LONG).show();
                }
            }
        } else if (view == goRegister)
        {
            Intent intent = new Intent(MainActivity.this, Register.class);
            startActivity(intent);
        }
    }

    public class MainActivityObserver implements Observer{

        @Override
        public void SignInWithEmailAndPasswordCompleate(@NonNull Task<AuthResult> task) {
            Log.d("MainActivityObserver", "SignInWithEmailAndPasswordCompleate");
            if (task.isSuccessful()) {
                Toast.makeText(MainActivity.this, "Successfully logged in", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainActivity.this, SummaryActivity.class);
                startActivity(intent);
            } else {
                //TODO - enable to button
                loginButton.setEnabled(true);
                Toast.makeText(MainActivity.this, " Error ", Toast.LENGTH_LONG).show();

            }
        }
    }
}