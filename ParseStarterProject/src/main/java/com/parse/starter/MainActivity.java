/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener {

    boolean signupmode = true;
    TextView logintextview;
    EditText password;

    public void showuserlist(){

        Intent intent = new Intent(getApplicationContext(),userList.class);
        startActivity(intent);
    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {

        if (i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction()== KeyEvent.ACTION_DOWN){
            signUp(view);
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.logintextview){
            Button signupbutton = (Button) findViewById(R.id.signupbutton);

            if (signupmode){
               signupmode = false;
               signupbutton.setText("login");
               logintextview.setText("Sign Up");
            }else{
                signupmode = true;
                signupbutton.setText("signup");
                logintextview.setText("Log In");
            }
        }else if (view.getId()==R.id.backgroundlayout||view.getId()==R.id.igfont||view.getId()==R.id.iglogo){

            InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }
    }

    public void signUp(View view){
      EditText username = (EditText)findViewById(R.id.username);
      EditText password = (EditText)findViewById(R.id.password);

      if (username.getText().toString().matches("")|| password.getText().toString().matches("")){
          Toast.makeText(this, "Username and Password required", Toast.LENGTH_SHORT).show();
      }else{

          if(signupmode) {

              ParseUser user = new ParseUser();
              user.setUsername(username.getText().toString());
              user.setPassword(password.getText().toString());

              user.signUpInBackground(new SignUpCallback() {
                  @Override
                  public void done(ParseException e) {
                      if (e == null) {
                          Log.i("Sign Up", "Successful");
                          showuserlist();
                      } else {
                          Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                      }
                  }
              });
          }else{
              ParseUser.logInInBackground(username.getText().toString(), password.getText().toString(), new LogInCallback() {
                  @Override
                  public void done(ParseUser user, ParseException e) {
                    if (user!=null){
                        Log.i("Login Successful","Yeah!!!");
                        showuserlist();
                    }else{
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                  }
              });
          }
      }
  }


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    setTitle("My Instagram");

      logintextview = (TextView)findViewById(R.id.logintextview);
      logintextview.setOnClickListener(this);

      RelativeLayout backgroundlayout = (RelativeLayout) findViewById(R.id.backgroundlayout);
      ImageView igfont = (ImageView)findViewById(R.id.igfont);
      ImageView iglogo = (ImageView)findViewById(R.id.iglogo);

      backgroundlayout.setOnClickListener(this);
      igfont.setOnClickListener(this);
      iglogo.setOnClickListener(this);

      EditText password = (EditText)findViewById(R.id.password);
      password.setOnKeyListener(this);

      /*if (ParseUser.getCurrentUser()!=null){

          showuserlist();
      }*/


    ParseAnalytics.trackAppOpenedInBackground(getIntent());
  }


}