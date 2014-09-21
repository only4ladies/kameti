package com.kameti.kameti;

import android.os.Bundle;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AddKameti extends Activity {

    String kametiName = null;
    TextView kametiNameView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_kameti);

        Button add = (Button) findViewById(R.id.add);
        kametiNameView = (TextView) findViewById(R.id.kametiName);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kametiName = kametiNameView.getText().toString();
                if(kametiName != null && !kametiName.isEmpty()) {
                    KametiDbHelper kametiDbHelper = new KametiDbHelper(getBaseContext());
                    SQLiteDatabase db = kametiDbHelper.getWritableDatabase();
                    //ContentValues values = new ContentValues();
                    //values.put("`name`", kametiName);
                    //db.insert("`Kameti`", null, values);
                    Intent intent = new Intent(getApplicationContext(), Kameties.class);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_kameti, menu);
        return true;
    }

}