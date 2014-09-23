package com.kameti.kameti;

import android.os.Bundle;
import android.provider.Settings.Secure;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class Register extends Activity {

    String phoneNumber = null;
    String fullName = null;
    TextView fullNameView = null;
    String API_REGISTER = "http://aaagrawa7-win7:8082/kameti/register.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        phoneNumber = getIntent().getExtras().getString("phoneNumber");

        Button done = (Button) findViewById(R.id.done);
        fullNameView = (TextView) findViewById(R.id.fullName);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fullName = fullNameView.getText().toString();
                if(fullName != null && !fullName.isEmpty()) {
                    new CallAPI(new handlerRegister(), getApplicationContext()).execute(API_REGISTER + "?fullName=" + fullName + "&phoneNumber=" + phoneNumber);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.register, menu);
        return true;
    }

    private class handlerRegister implements ApiHandler {
        @Override
        public void execute(String response) {
            try {
                JSONObject reader = new JSONObject(response);
                String result = reader.getString("result");
                if ("OK".equalsIgnoreCase(result.trim())) {
                    Intent intent = new Intent(getApplicationContext(), Kameties.class);
                    intent.putExtra("phoneNumber", phoneNumber);
                    startActivity(intent);
                    finish();
                }
            }
            catch (JSONException e){

            }
        }
        @Override
        public void postExecute() {

        }
    }
}