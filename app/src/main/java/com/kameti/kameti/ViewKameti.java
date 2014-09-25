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
import org.json.JSONException;
import org.json.JSONObject;

public class ViewKameti extends Activity {

    SharedPreferences sharedPref = null;
    String phoneNumber = null;
    BidDuration bidDuration = null;
    String kametiName = null;
    int kametiMembers;
    long adminId;
    long kametiId;

    String API_MEMBER = "http://aaagrawa7-win7:8082/kameti/member.php";
    String API_TIME = "http://aaagrawa7-win7:8082/kameti/time.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_kameti);
        sharedPref = getPreferences(Context.MODE_PRIVATE);
        phoneNumber = getIntent().getExtras().getString("phoneNumber");
        kametiId = getIntent().getExtras().getLong("kametiId");

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
        KametiDbHelper kametiDbHelper = new KametiDbHelper(getBaseContext(), phoneNumber);
        SQLiteDatabase db = kametiDbHelper.getReadableDatabase();
        String[] dbSelect = {"*"};
        String dbWhere = "`kameti_id`=" + kametiId;
        String[] dbArgs = null;
        String dbGroupBy = null;
        String dbFilterBy = null;
        String dbSortBy = null;
        Cursor c = db.query("`kameti`", dbSelect, dbWhere, dbArgs, dbGroupBy, dbFilterBy, dbSortBy);
        if (c.moveToFirst()) {
            kametiName = c.getString(1);
            kametiMembers = c.getInt(4);
            adminId = c.getLong(2);
            bidDuration = new BidDuration(c.getString(3), c.getString(7), c.getString(8), c.getInt(4));
            for(int i=0; i<view.length; i++){
                if(view[i] != null){
                    if(i == 2) {
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
        new CallAPI(new handlerGetTime(menu), getApplicationContext()).execute(API_TIME);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_auctions) {
            Intent intent = new Intent(getApplicationContext(), Auctions.class);
            intent.putExtra("phoneNumber", phoneNumber);
            intent.putExtra("kametiId", kametiId);
            intent.putExtra("kametiName", kametiName);
            intent.putExtra("kametiMembers", kametiMembers);
            startActivity(intent);
        }
        if (id == R.id.action_edit) {
            //TODO: Edit kameti feature
        }
        if (id == R.id.action_bid) {
            //TODO: Show bids of currently running auction
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

    private class handlerGetTime implements ApiHandler {

        private Menu menu = null;

        handlerGetTime(Menu menu){
            this.menu = menu;
        }
        @Override
        public void execute(String response) {
            try {
                JSONObject reader = new JSONObject(response);
                String result = reader.getString("result");
                if ("OK".equalsIgnoreCase(result.trim())) {
                    long current_time = reader.getLong("current_time");
                    if(bidDuration.contains(current_time)){
                        MenuItem menu_bid = menu.findItem(R.id.action_bid);
                        menu_bid.setEnabled(true);
                    }
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
