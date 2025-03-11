package com.example.perek27;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;

public class Register extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth firebaseAuth;
    Button registerButton, dateOfBirthButton, takePicture;
    EditText emailRegister, passwordRegister, firstNameRegister, lastNameRegister;
    Date date;
    Bitmap bitmap;

    FirebaseDatabase firebaseDatabase;
    ImageView pictureView;

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
        firebaseDatabase = FirebaseDatabase.getInstance();

        registerButton = (Button) findViewById(R.id.registerBtn);
        registerButton.setOnClickListener(this);

        emailRegister = (EditText) findViewById(R.id.emailRegister);
        emailRegister.setOnClickListener(this);

        passwordRegister = (EditText) findViewById(R.id.passwordRegister);
        passwordRegister.setOnClickListener(this);

        firstNameRegister = (EditText) findViewById(R.id.firstname);
        firstNameRegister.setOnClickListener(this);

        lastNameRegister = (EditText) findViewById(R.id.lastName);
        lastNameRegister.setOnClickListener(this);

        dateOfBirthButton = (Button) findViewById(R.id.dateOfBirth);
        dateOfBirthButton.setOnClickListener(this);

        takePicture = (Button) findViewById(R.id.takePicture);
        takePicture.setOnClickListener(this);

        pictureView = (ImageView) findViewById(R.id.imageRegister);
        pictureView.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        String email = "";
        String password = "";
        if (view == registerButton && emailRegister != null && !(emailRegister.equals("")) && passwordRegister != null && !(passwordRegister.equals(""))) {
            email = emailRegister.getText().toString();
            password = passwordRegister.getText().toString();

            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        String fName = firstNameRegister.getText().toString();
                        String lName = lastNameRegister.getText().toString();
                        String uid = firebaseAuth.getCurrentUser().getUid();
                        String em = firebaseAuth.getCurrentUser().getEmail();
                        UserData user = new UserData(fName,lName,em,date,uid,"");
                        user.setPicAsString(bitmap);
                        DatabaseReference ref = firebaseDatabase.getReference("user").child(uid);
                        ref.setValue(user);
                        Toast.makeText(Register.this, "Successfully registered", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Register.this, SummaryActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(Register.this, "Registration Error", Toast.LENGTH_LONG).show();
                    }

                }
            });
        } else if (view == dateOfBirthButton) {
            Toast.makeText(Register.this, "uploading", Toast.LENGTH_LONG).show();
            Calendar systemCalender = Calendar.getInstance();
            int year = systemCalender.get(Calendar.YEAR);
            int month = systemCalender.get(Calendar.MONTH);
            int day = systemCalender.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(Register.this, new SetDate(), year, month, day);
            datePickerDialog.show();

        } else if (view == takePicture)
        {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent , 0);
        }
        else
        {
            Toast.makeText(Register.this, "Not entered email or pass", Toast.LENGTH_LONG).show();
        }

    }

    public class SetDate implements DatePickerDialog.OnDateSetListener {

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            date = new Date(year, month, dayOfMonth);
            String str = dayOfMonth + "/" + (month + 1) + "/" + year;
            dateOfBirthButton.setText(str);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                bitmap = (Bitmap) data.getExtras().get("data");
                pictureView.setImageBitmap(bitmap);
            }
        }
    }
}
