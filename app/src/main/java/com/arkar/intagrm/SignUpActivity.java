package com.arkar.intagrm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    AnimationDrawable animationDrawable;
    ConstraintLayout constrain_layout;
    Button btnSignIn,btnSignUp;
    EditText edtUserEmail, edtUserName, edtUserPass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setTitle("Sign Up");

        constrain_layout = findViewById(R.id.constrain_layout);
        animationDrawable = (AnimationDrawable) constrain_layout.getBackground();
        animationDrawable.setEnterFadeDuration(5000);
        animationDrawable.setExitFadeDuration(2000);

        // SignUp variable declaration
        edtUserEmail = findViewById(R.id.txt_sign_up_email);
        edtUserName = findViewById(R.id.txt_sign_up_user_name);
        edtUserPass = findViewById(R.id.txt_sign_up_user_pass);
        btnSignIn = findViewById(R.id.btn_sign_in);
        btnSignUp =findViewById(R.id.btn_sign_up);

        btnSignUp.setOnClickListener(this);
        btnSignIn.setOnClickListener(this);

        if (ParseUser.getCurrentUser() != null) {
            transitionToSignInPage();
        }
    }

    private void transitionToSignInPage() {
        Intent intent = new Intent(SignUpActivity.this,LoginActivity.class);
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
            case R.id.btn_sign_in:
                Intent intent = new Intent(SignUpActivity.this,SignInActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.btn_sign_up:
                String UserEmail,UserPass,UserName;
                 UserEmail = edtUserEmail.getText().toString();
                 UserPass = edtUserPass.getText().toString();
                 UserName = edtUserName.getText().toString();

                if (UserName.equals("")) {
                    FancyToast.makeText(SignUpActivity.this, "Please Fill User Name!!",FancyToast.LENGTH_SHORT,FancyToast.ERROR,true).show();
                } else if (UserPass.equals(""))  {
                    FancyToast.makeText(SignUpActivity.this, "Please Fill User Password!!",FancyToast.LENGTH_SHORT,FancyToast.ERROR,true).show();
                } else if (UserEmail.equals("")) {
                    FancyToast.makeText(SignUpActivity.this, "Please Fill User Email!!",FancyToast.LENGTH_SHORT,FancyToast.ERROR,true).show();
                } else {
                    FancyToast.makeText(SignUpActivity.this, "Login Success!!",FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,true).show();

                    final ParseUser appUser = new ParseUser();
                    appUser.setEmail(edtUserEmail.getText().toString());
                    appUser.setUsername(edtUserName.getText().toString());
                    appUser.setPassword(edtUserPass.getText().toString());

                    final ProgressDialog progressDialog = new ProgressDialog(this);
                    progressDialog.setMessage("Signing Up User"+edtUserName.getText().toString());
                    progressDialog.show();
                    appUser.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                FancyToast.makeText(SignUpActivity.this,
                                        appUser.getUsername().toString() + "is signed up!",
                                        FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,
                                        true).show();
                                Intent intent = new Intent(SignUpActivity.this,LoginActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                FancyToast.makeText(SignUpActivity.this,
                                        "There was an error: "+e.getMessage(),
                                        FancyToast.LENGTH_SHORT,
                                        FancyToast.SUCCESS,
                                        true).show();
                            }
                                progressDialog.dismiss();
                        }
                    });
                }
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