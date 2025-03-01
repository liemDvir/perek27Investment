package com.example.perek27;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class settingsActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    Button summaryBtn, discoverBtn, historyBtn, logoutBtn;

    TextView firstNameTxt, lastNameTxt, emailTxt,passwordTxt, dateTxt;
    ImageView pictureView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance("https://insvestment-85820-default-rtdb.firebaseio.com/");

        firstNameTxt = (TextView)findViewById(R.id.firstNameTextView);
        firstNameTxt.setOnClickListener(this);

        lastNameTxt = (TextView)findViewById(R.id.lastNameTextView);
        lastNameTxt.setOnClickListener(this);

        emailTxt = (TextView)findViewById(R.id.lastNameTextView);
        emailTxt.setOnClickListener(this);

        passwordTxt = (TextView)findViewById(R.id.passwordTextView);
        passwordTxt.setOnClickListener(this);

        pictureView = (ImageView)findViewById(R.id.imageView);
        pictureView.setOnClickListener(this);

        summaryBtn = (Button)findViewById(R.id.summaryButton);
        summaryBtn.setOnClickListener(this);

        discoverBtn = (Button)findViewById(R.id.discoverButton);
        discoverBtn.setOnClickListener(this);

        historyBtn = (Button)findViewById(R.id.historyButton);
        historyBtn.setOnClickListener(this);

        logoutBtn = (Button)findViewById(R.id.logoutButton);
        logoutBtn.setOnClickListener(this);

        if (firebaseAuth.getCurrentUser() != null) {
            (firebaseDatabase.getReference("user").child(firebaseAuth.getCurrentUser().getUid())).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot)
                {
                    userData user = snapshot.getValue(userData.class);
                    if(user!=null)
                    {
                        String fname = user.getFirstName();
                        firstNameTxt.setText(fname);

                        String lname = user.getLastName();
                        lastNameTxt.setText(lname);

                        String email = user.getEmail();
                        emailTxt.setText(email);

                        String password = user.getPassword();
                        passwordTxt.setText(password);

                        String date = user.getDate().getDate() +"/" + (user.getDate().getMonth()+1) + "/" + user.getDate().getYear();
                        dateTxt.setText(date);



                    }
                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
    }

    @Override
    public void onClick(View view) {
        if (view == summaryBtn)
        {
            Intent intent = new Intent(settingsActivity.this, UserInfoActivity.class);
            startActivity(intent);
        } else if (view == discoverBtn)
        {
            Intent intent = new Intent(settingsActivity.this, DiscoverActivity.class);
            startActivity(intent);
        } else if (view == historyBtn)
        {
            Intent intent = new Intent(settingsActivity.this,postListActivity.class);
            startActivity(intent);
        } else if (view == logoutBtn)
        {
            firebaseAuth.signOut();
            finish();

        }

    }
}