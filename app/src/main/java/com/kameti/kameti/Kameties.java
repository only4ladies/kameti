package com.kameti.kameti;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class Kameties extends Activity {

    String phoneNumber = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kameties);
        phoneNumber = getIntent().getExtras().getString("phoneNumber");
        //GET_API

        KametiDbHelper kametiDbHelper = new KametiDbHelper(getBaseContext(), phoneNumber);
        SQLiteDatabase db = kametiDbHelper.getReadableDatabase();
        String[] dbSelect = {"`kameti_id` as _id", "`kameti_name`", "`kameti_amount`", "`user_name`"};
        String dbWhere = "`kameti`.`admin_id` = `members`.`member_id`";
        String[] dbArgs = null;
        String dbGroupBy = null;
        String dbFilterBy = null;
        String dbSortBy = null;
        Cursor c = db.query("`kameti`, `members`", dbSelect, dbWhere, dbArgs, dbGroupBy, dbFilterBy, dbSortBy);
        if (c != null) {
            ListView list = (ListView) findViewById(R.id.listView);
            String[] from = {"kameti_name", "kameti_amount", "user_name"};
            int[] to = {R.id.kametiName, R.id.lastBid, R.id.remainingMonths};
            SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                    this,
                    R.layout.list_kameti,
                    c,
                    from,
                    to,
                    CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER
            );
            list.setAdapter(adapter);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> row, View element, int elementPosition, long rowId) {
                    Intent intent = new Intent(getApplicationContext(), ViewKameti.class);
                    intent.putExtra("phoneNumber", phoneNumber);
                    intent.putExtra("rowId", rowId);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.kameties, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_add) {
            Intent intent = new Intent(getApplicationContext(), AddKameti.class);
            intent.putExtra("phoneNumber", phoneNumber);
            startActivity(intent);
        }
        if (id == R.id.action_logout) {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            intent.putExtra("action", "logout");
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
