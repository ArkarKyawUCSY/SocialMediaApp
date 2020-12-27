package com.arkar.intagrm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {
    AnimationDrawable animationDrawable;
    ConstraintLayout constrain_layout;
    Button btnSignIn,btnSignUp;
    EditText userName, userPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        setTitle("Log In");

        //background color animation create
        constrain_layout = findViewById(R.id.constrain_layout);
        animationDrawable = (AnimationDrawable) constrain_layout.getBackground();
        animationDrawable.setEnterFadeDuration(5000);
        animationDrawable.setExitFadeDuration(2000);

        userName = findViewById(R.id.txt_sign_in_user_name);
        userPass = findViewById(R.id.txt_sign_in_user_pass);

        btnSignIn = findViewById(R.id.btn_login_sign_in);
        btnSignUp =findViewById(R.id.btn_login_sign_up);
        btnSignIn.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);

        userPass.setOnKeyListener(new View.OnKeyListener(){
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER &&
                        event.getAction() == KeyEvent.ACTION_DOWN) {
                    onClick(btnSignIn);
                }
                return false;
            }
        });
        if (ParseUser.getCurrentUser() != null) {

            if (ParseUser.getCurrentUser() != null) {
                transitionToSignInPage();
            }
        }
    }

    private void transitionToSignInPage() {
        Intent intent = new Intent(SignInActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        animationDrawable.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login_sign_in:

                if (userName.getText().toString().equals("")) {
                    FancyToast.makeText(SignInActivity.this, "Please Fill User Name", FancyToast.LENGTH_LONG,FancyToast.ERROR,true).show();
                    Log.d("TAG","userName null");
                } else if (userPass.getText().toString().equals("")) {
                    FancyToast.makeText(SignInActivity.this, "Please Fill User Password", FancyToast.LENGTH_LONG,FancyToast.ERROR,true).show();
                    Log.d("TAG","password null");
                } else {
                    Log.d("TAG","userName password ok"+userName.getText().toString()+"AA"+userPass.getText().toString());
                    try {
                        ParseUser.logInInBackground(userName.getText().toString(), userPass.getText().toString(),
                                new LogInCallback() {
                                    @Override
                                    public void done(ParseUser user, ParseException e) {
                                        if (user != null && e == null) {
                                            Log.d("TAG", "userName password second ok");
                                            FancyToast.makeText(SignInActivity.this, user.getUsername() + " is Logged in", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show();
                                        Intent intent = new Intent(SignInActivity.this,LoginActivity.class);
                                        startActivity(intent);
                                        finish();

                                        }
                                    }
                                });
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.btn_login_sign_up:
                Intent intent = new Intent(SignInActivity.this,SignUpActivity.class);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
    }

    public void rootLayoutTapped(View v) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}