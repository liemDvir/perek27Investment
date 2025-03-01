package com.example.perek27;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public interface IObserver {

    public void SignInWithEmailAndPasswordCompleate(@NonNull Task<AuthResult> task);
}
