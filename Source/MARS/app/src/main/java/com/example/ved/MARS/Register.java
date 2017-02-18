package com.example.ved.MARS;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Register extends AppCompatActivity {

    private AutoCompleteTextView mEmailView;
    private EditText mPassword;
    private EditText mPassword2;
    private boolean isInputValid = false;
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.app_menu, menu);
        MenuItem logout = menu.findItem(R.id.action_logout);
        logout.setVisible(false);
        return true;

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
    }
    public void backToLogin(View v){
        Intent redirect = new Intent(Register.this, LoginActivity.class);
        startActivity(redirect);
    }
    public void register_user(View v) {

        mEmailView = (AutoCompleteTextView) findViewById(R.id.uname);
        mPassword = (EditText) findViewById(R.id.password);
        mPassword2 = (EditText) findViewById(R.id.password2);

        String userName = mEmailView.getText().toString();
        String password = mPassword.getText().toString();
        String password2 = mPassword2.getText().toString();


        if (TextUtils.isEmpty(userName)) {
            mEmailView.setError(getString(R.string.error_field_required));
            mEmailView.requestFocus();
        } else if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPassword.setError(getString(R.string.error_invalid_password));
            mPassword.requestFocus();
        } else if (!TextUtils.isEmpty(password2) && !isPassword2Valid(password,password2)) {
            mPassword2.setError(getString(R.string.error_invalid_password2));
            mPassword2.requestFocus();
        }
        else
        {
            isInputValid = true;
        }

        if(isInputValid){

            String data = userName+":"+password;
            //check if users file already present, if not then create one. If present then append data.
            File user_file = new File(getApplicationContext().getFilesDir(), "marsuser_data.txt");
            if(!user_file.exists()){
                create_new_file(data,user_file);
            }
            else
            {
                try {
                    BufferedWriter bw = new BufferedWriter(new FileWriter(user_file, true));
                    bw.write("/"+data);
                    bw.flush();
                    bw.close();
                    Intent redirect = new Intent(Register.this, LoginActivity.class);
                    startActivity(redirect);
                }catch (IOException e){
                    e.printStackTrace();
                }

            }
        }

    }
    private void create_new_file(String data, File user_file){
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(user_file, true));
            bw.write(data);
            bw.flush();
            bw.close();
            Intent redirect = new Intent(Register.this, LoginActivity.class);
            startActivity(redirect);

        }catch (IOException e){
            e.printStackTrace();
        }
    }
    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.isEmpty();
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }
    private boolean isPassword2Valid(String password,String password2) {
        //TODO: Replace this with your own logic
        return password.equals(password2);
    }
}