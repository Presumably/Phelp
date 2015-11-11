package com.isel.ps.Find_My_Beacon.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.isel.ps.Find_My_Beacon.R;
import com.isel.ps.Find_My_Beacon.communication.response.ICommunicationResponse;
import com.isel.ps.Find_My_Beacon.communication.user.UserCommunication;
import com.isel.ps.Find_My_Beacon.exception.ConnectorException;
import com.isel.ps.Find_My_Beacon.gcm.GetTokenIntent;
import com.isel.ps.Find_My_Beacon.model.User;

import java.io.IOException;

public class RegisterActivity extends Activity implements ICommunicationResponse<User> {

    private TextView userNameView;
    private TextView passwordView;
    private TextView confirmPasswordView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);

        userNameView = (TextView) findViewById(R.id.userNameTextView);
        passwordView = (TextView) findViewById(R.id.passwordTextView);
        confirmPasswordView = (TextView) findViewById(R.id.confirmPasswordTextView);
    }

    public void register(View view) {
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

        final String confirmPassword = confirmPasswordView.getText().toString();
        if(confirmPassword.isEmpty()){
            Toast.makeText(this, R.string.passwordEmpty, Toast.LENGTH_LONG).show();
            return;
        }

        if(!password.equals(confirmPassword)){
            Toast.makeText(this, R.string.passworMismatch, Toast.LENGTH_LONG).show();
            return;
        }
        Intent i = new Intent(this, GetTokenIntent.class);
        startService(i);
        Log.i("testeMeu", "pedido ja foi lançado");
        String token = "";
        User toRegist = new User(userName,password,token);
        try {
            new UserCommunication().register(toRegist, this);
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
        Toast.makeText(this,R.string.cannotRegister,Toast.LENGTH_LONG).show();
    }
}