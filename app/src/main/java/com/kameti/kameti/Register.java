package com.kameti.kameti;

import android.os.Bundle;
import android.provider.Settings.Secure;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;

public class Register extends Activity {

    String DEVICE_ID = null;
    String fullName = null;
    TextView fullNameView = null;
    String API_REGISTER = "http://aaagrawa7-win7:8082/kameti/register.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        DEVICE_ID = Secure.getString(getBaseContext().getContentResolver(), Secure.ANDROID_ID);

        Button done = (Button) findViewById(R.id.done);
        fullNameView = (TextView) findViewById(R.id.fullName);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fullName = fullNameView.getText().toString();
                if(fullName != null && !fullName.isEmpty()) {
                    new CallAPI(new handlerRegister(), getApplicationContext()).execute(API_REGISTER + "?fullName=" + fullName + "&deviceid=" + DEVICE_ID);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.register, menu);
        return true;
    }

    private class handlerRegister implements ApiHandler {
        public void execute(String response) {
            try {
                JSONObject reader = new JSONObject(response);
                String result = reader.getString("result");
                if ("OK".equalsIgnoreCase(result.trim())) {
                    Intent intent = new Intent(getApplicationContext(), Kameties.class);
                    startActivity(intent);
                }
            }
            catch (JSONException e){

            }
        }
    }
}