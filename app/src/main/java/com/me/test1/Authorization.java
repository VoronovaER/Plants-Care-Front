package com.me.test1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.me.test1.dto.florist.BaseFloristDTO;
import com.me.test1.dto.florist.FloristDTO;
import com.me.test1.network.ApiClient;
import com.me.test1.network.PlantTypeApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Authorization extends AppCompatActivity {
    private Boolean isLoggedIn = true;
    private TextInputEditText email, password, name;
    private Button login;
    private TextView text, btnText;
    private FirebaseAuth mAuth;
    public PlantTypeApi plantTypeApi;
    private static final String TAG = "EmailPassword";
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.authorization);
        mAuth = FirebaseAuth.getInstance();
        plantTypeApi = ApiClient.getClient().create(PlantTypeApi.class);

        name = findViewById(R.id.nameAuth);
        email = findViewById(R.id.emailAuth);
        password = findViewById(R.id.passwordAuth);
        login = findViewById(R.id.loginAuth);
        text = findViewById(R.id.register1Auth);
        btnText = findViewById(R.id.register2Auth);
        name.setVisibility(View.GONE);
        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (email.getText().toString().isEmpty() || password.getText().toString().isEmpty()) {
                    Toast.makeText(Authorization.this, "Необходимы пароль и email", Toast.LENGTH_LONG).show();
                }else if (checkPassword(password.getText().toString()) && !isLoggedIn){
                    password.setError("Пароль должен содержать от 6 до 20 символов и содержать буквы и цифры");
                }else if(checkEmail(email.getText().toString())){
                    email.setError("Email не соответствует формату");
                }else{
                    if (isLoggedIn) {
                        login();
                    }else{
                        register();
                    }
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null){
            startActivity(new Intent(Authorization.this, MainActivity.class));
        }
    }

    public void login() {
        mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithEmail:success");
                            successLogin();
                        } else if(task.getException().toString().equals("com.google.firebase.auth.FirebaseAuthInvalidCredentialsException")){
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(Authorization.this, "Неверные данные",
                                    Toast.LENGTH_SHORT).show();
                        }else if (task.getException().toString().equals("com.google.firebase.auth.FirebaseAuthUserCollisionException")){
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(Authorization.this, "Пользователь уже существует, войдите в аккаунт",
                               Toast.LENGTH_LONG).show();
                        }else{
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(Authorization.this, "Ошибка",
                               Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void register() {
        mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail:success");
                            successRegistration();
                        }else{
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(Authorization.this, "Неверные данные",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void Registration(View view) {
        if (isLoggedIn) {
            isLoggedIn = false;
            login.setText("Зарегистрироваться");
            text.setText("Уже есть аккаунт?");
            btnText.setText("Войти");
            name.setVisibility(View.VISIBLE);
        }else{
            isLoggedIn = true;
            login.setText("Войти");
            text.setText("Ещё нет аккаунта?");
            btnText.setText("Зарегистрироваться");
            name.setVisibility(View.GONE);
        }

    }

    public void successRegistration() {
        FirebaseUser user = mAuth.getCurrentUser();
        Toast.makeText(Authorization.this, "Успешно", Toast.LENGTH_LONG).show();
        isLoggedIn = true;
        BaseFloristDTO florist = new BaseFloristDTO();
        florist.setEmail(email.getText().toString());
        florist.setName(name.getText().toString());
        plantTypeApi.createFlorist(florist).enqueue(new Callback<FloristDTO>() {
            @Override
            public void onResponse(Call<FloristDTO> call, Response<FloristDTO> response) {
                startActivity(new Intent(Authorization.this, MainActivity.class));
            }

            @Override
            public void onFailure(Call<FloristDTO> call, Throwable throwable) {
                Toast.makeText(Authorization.this, "Ошибка", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void successLogin() {
        FirebaseUser user = mAuth.getCurrentUser();
        Toast.makeText(Authorization.this, "Успешно", Toast.LENGTH_LONG).show();
        Info.setEmail(email.getText().toString());
        Info.setName(password.getText().toString());
        Info.setName(name.getText().toString());
        plantTypeApi.getFloristByEmail(email.getText().toString()).enqueue(new Callback<FloristDTO>() {
            @Override
            public void onResponse(Call<FloristDTO> call, Response<FloristDTO> response) {
                startActivity(new Intent(Authorization.this, MainActivity.class));
            }

            @Override
            public void onFailure(Call<FloristDTO> call, Throwable throwable) {
                Toast.makeText(Authorization.this, "Ошибка", Toast.LENGTH_LONG).show();
            }
        });
    }

    Boolean checkPassword(String password){
        return password.length() < 6 || password.length() > 20 || password.chars().noneMatch(Character::isLetter) || password.chars().noneMatch(Character::isDigit);
    }

    Boolean checkEmail(String email){
        return !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
