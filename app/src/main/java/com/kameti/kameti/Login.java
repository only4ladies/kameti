package com.kameti.kameti;

import org.json.JSONException;
import org.json.JSONObject;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends Activity {

    SharedPreferences sharedPref = null;
    String DEVICE_ID = null;
    String phoneNumber = null;
    String code = null;
    TextView phoneNumberView = null;
    TextView codeView = null;
    String API_SEND_SMS = "http://aaagrawa7-win7:8082/kameti/sendSMS.php";
    String API_VERIFY = "http://aaagrawa7-win7:8082/kameti/verify.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        DEVICE_ID = Secure.getString(getBaseContext().getContentResolver(), Secure.ANDROID_ID);
        sharedPref = getPreferences(Context.MODE_PRIVATE);

        String localNumber = sharedPref.getString("phoneNumber", "");
        if(localNumber != null && !localNumber.isEmpty()) {
            Intent intent = new Intent(getApplicationContext(), Kameties.class);
            startActivity(intent);
            finish();
        }

        Button send_sms = (Button) findViewById(R.id.send_sms);
        phoneNumberView = (TextView) findViewById(R.id.phoneNumber);
        send_sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNumber = phoneNumberView.getText().toString();
                if(phoneNumber != null && !phoneNumber.isEmpty()) {
                    new CallAPI(new handlerSendSMS(), getApplicationContext()).execute(API_SEND_SMS + "?number=" + phoneNumber);
                }
            }
        });

        Button verify = (Button) findViewById(R.id.verify);
        codeView = (TextView) findViewById(R.id.code);
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNumber = phoneNumberView.getText().toString();
                code = codeView.getText().toString();
                if(code != null && !code.isEmpty() && phoneNumber != null && !phoneNumber.isEmpty()) {
                    new CallAPI(new handlerVerify(), getApplicationContext()).execute(API_VERIFY + "?number=" + phoneNumber + "&code=" + code + "&deviceid=" + DEVICE_ID);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    private class handlerSendSMS implements ApiHandler {
        public void execute(String response) {
            try {
                JSONObject reader = new JSONObject(response);
                String result = reader.getString("result");
                if ("OK".equalsIgnoreCase(result.trim())) {
                    String message = reader.getString("message");
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                }
            }
            catch (JSONException e){

            }
        }
    }

    private class handlerVerify implements ApiHandler {
        public void execute(String response) {
            try {
                JSONObject reader = new JSONObject(response);
                String result = reader.getString("result");
                if ("OK".equalsIgnoreCase(result.trim())) {
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("phoneNumber", phoneNumber);
                    editor.commit();
                    int registered = reader.getInt("registered");
                    if (registered == 1) {
                        Intent intent = new Intent(getApplicationContext(), Kameties.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(getApplicationContext(), Register.class);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Invalid Code", Toast.LENGTH_SHORT).show();
                }
            }
            catch (JSONException e){

            }
        }
    }
}