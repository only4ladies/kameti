package com.kameti.kameti;

import android.os.Bundle;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

public class AddKameti extends Activity {

    String kameti_name = null;
    String kameti_start_date = null;
    TextView value_kameti_name = null;
    DatePicker value_kameti_start_date = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_kameti);

        value_kameti_name = (TextView) findViewById(R.id.value_kamet_name);
        value_kameti_start_date = (DatePicker) findViewById(R.id.value_kameti_start_date);

        value_kameti_start_date.setCalendarViewShown(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_kameti, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_save) {
            kameti_name = value_kameti_name.getText().toString();
            if(kameti_name == null || kameti_name.isEmpty()) {
                return false;
            }
            kameti_start_date = "" + value_kameti_start_date.getYear()
                                   + value_kameti_start_date.getMonth()
                                   + value_kameti_start_date.getDayOfMonth();
            /*
            KametiDbHelper kametiDbHelper = new KametiDbHelper(getBaseContext());
            SQLiteDatabase db = kametiDbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("`name`", kameti_name);
            db.insert("`kameti`", null, values);
            Intent intent = new Intent(getApplicationContext(), Kameties.class);
            startActivity(intent);
            finish();
            */
        }
        return super.onOptionsItemSelected(item);
    }
}