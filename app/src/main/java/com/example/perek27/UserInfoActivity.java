package com.example.perek27;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class UserInfoActivity extends AppCompatActivity implements View.OnClickListener {

    Button logout, historyBtn, discoverBtn, settingBtn;

    TextView cashAmountOfMoney;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_info);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance("https://insvestment-85820-default-rtdb.firebaseio.com/");
        setContentView(R.layout.activity_user_info);

        historyBtn = (Button)findViewById(R.id.historyButton);
        historyBtn.setOnClickListener(this);

        discoverBtn =(Button)findViewById(R.id.discoverButton);
        discoverBtn.setOnClickListener(this);

        settingBtn = (Button)findViewById(R.id.settingButton);
        settingBtn.setOnClickListener(this);

        cashAmountOfMoney = (TextView)findViewById(R.id.cashAmountOfMoney);

        if(firebaseAuth.getCurrentUser()!= null)
        {
            (firebaseDatabase.getReference("user").child(firebaseAuth.getCurrentUser().getUid())).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    userData user = snapshot.getValue(userData.class);
                    if (user!=null)
                    {
                        String cash = String.valueOf(user.getCash());
                        cashAmountOfMoney.setText(cash);

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

        if (view == historyBtn) {
            Intent intent = new Intent(UserInfoActivity.this,postListActivity.class);
            this.startActivity(intent);

        } else if(view == discoverBtn)
        {
            Intent intent = new Intent(UserInfoActivity.this, DiscoverActivity.class);
            this.startActivity(intent);
        } else if (view == settingBtn)
        {
            Intent intent = new Intent(UserInfoActivity.this,settingsActivity.class);
            this.startActivity(intent);
        }

    }
}