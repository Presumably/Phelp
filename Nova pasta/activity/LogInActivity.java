package com.isel.ps.Find_My_Beacon.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.isel.ps.Find_My_Beacon.R;
import com.isel.ps.Find_My_Beacon.communication.response.ICommunicationResponse;
import com.isel.ps.Find_My_Beacon.communication.user.UserCommunication;
import com.isel.ps.Find_My_Beacon.exception.ConnectorException;
import com.isel.ps.Find_My_Beacon.model.User;

public class LogInActivity extends Activity implements ICommunicationResponse<User> {

    private TextView userNameView;
    private TextView passwordView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in_layout);
        userNameView = (TextView) findViewById(R.id.userNameTextView);
        passwordView = (TextView) findViewById(R.id.passwordTextView);
    }

    public void doLogIn(View view) {
        final String userName = userNameView.getText().toString();
        if(userName.isEmpty()){
            Toast.makeText(this, R.string.userNameEmpty, Toast.LENGTH_LONG).show();
            return;
        }

        final String password = passwordView.getText().toString();
        if(password.isEmpty()){
            Toast.makeText(this, R.string.passwordEmpty, Toast.LENGTH_LONG).show();
            return;
        }

        User toLog = new User(userName,password);
        try {
            new UserCommunication().doLogin(toLog,this);
        } catch (ConnectorException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResponse(User user) {
        if(user!= null){
            Intent menu = new Intent(this, MainMenuActivity.class);
            startActivity(menu);
            finish();
            return;
        }
        Toast.makeText(this,R.string.cannotLogIn,Toast.LENGTH_LONG).show();
    }
}