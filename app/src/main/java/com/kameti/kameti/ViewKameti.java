package com.kameti.kameti;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class ViewKameti extends Activity {

    SharedPreferences sharedPref = null;
    String phoneNumber = null;
    long adminId;
    String API_MEMBER = "http://aaagrawa7-win7:8082/kameti/member.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_kameti);
        sharedPref = getPreferences(Context.MODE_PRIVATE);
        phoneNumber = getIntent().getExtras().getString("phoneNumber");

        TextView[] view = {
                null,
                (TextView) findViewById(R.id.value_kamet_name),
                (TextView) findViewById(R.id.value_admin_id),
                (TextView) findViewById(R.id.value_kameti_start_date),
                (TextView) findViewById(R.id.value_kameti_members),
                (TextView) findViewById(R.id.value_kameti_amount),
                (TextView) findViewById(R.id.value_kameti_interest_rate),
                (TextView) findViewById(R.id.value_bid_start_time),
                (TextView) findViewById(R.id.value_bid_end_time),
                (TextView) findViewById(R.id.value_bid_amount_minimum),
                (TextView) findViewById(R.id.value_bid_timer),
                (TextView) findViewById(R.id.value_lucky_draw_amount),
                (TextView) findViewById(R.id.value_lucky_members),
                (TextView) findViewById(R.id.value_runnerup_percentage),
                null
        };
        long rowId = getIntent().getExtras().getLong("rowId");
        KametiDbHelper kametiDbHelper = new KametiDbHelper(getBaseContext());
        SQLiteDatabase db = kametiDbHelper.getReadableDatabase();
        String[] dbSelect = {"*"};
        String dbWhere = "`kameti_id`=" + rowId;
        String[] dbArgs = null;
        String dbGroupBy = null;
        String dbFilterBy = null;
        String dbSortBy = null;
        Cursor c = db.query("`kameti`", dbSelect, dbWhere, dbArgs, dbGroupBy, dbFilterBy, dbSortBy);
        if (c.moveToFirst()) {
            for(int i=0; i<view.length; i++){
                if(view[i] != null){
                    if(i == 2) {
                        adminId = c.getLong(i);
                        Cursor admin = db.rawQuery("SELECT `user_name` FROM `members` WHERE `member_id`=" + c.getString(i), null);
                        if(admin.moveToFirst()) {
                            view[i].setText(admin.getString(0));
                        }
                    }
                    else if(i == 5){
                        view[i].setText(c.getString(i) + " INR");
                    }
                    else if(i == 6){
                        view[i].setText(c.getString(i) + "%");
                    }
                    else if(i == 9){
                        view[i].setText(c.getString(i) + " INR");
                    }
                    else if(i == 10){
                        view[i].setText(c.getString(i) + " minutes");
                    }
                    else if(i == 11){
                        view[i].setText(c.getString(i) + " INR");
                    }
                    else if(i == 13){
                        view[i].setText(c.getString(i) + "%");
                    }
                    else{
                        view[i].setText(c.getString(i));
                    }
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.view_kameti, menu);
        new CallAPI(new handlerCheckAdmin(menu), getApplicationContext()).execute(API_MEMBER + "?number=" + phoneNumber);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class handlerCheckAdmin implements ApiHandler {

        private Menu menu = null;

        handlerCheckAdmin(Menu menu){
            this.menu = menu;
        }
        @Override
        public void execute(String response) {
            try {
                JSONObject reader = new JSONObject(response);
                String result = reader.getString("result");
                if ("OK".equalsIgnoreCase(result.trim())) {
                    long myId = reader.getLong("member_id");
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putLong("memberId", myId);
                    editor.commit();
                }
            }
            catch (JSONException e){

            }
        }
        @Override
        public void postExecute() {
            long myId = sharedPref.getLong("memberId", -1);
            if(myId == adminId){
                MenuItem menu_edit = menu.findItem(R.id.action_edit);
                menu_edit.setVisible(true);
            }
        }
    }
}
