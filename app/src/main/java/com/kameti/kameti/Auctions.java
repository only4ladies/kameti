package com.kameti.kameti;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class Auctions extends Activity {

    String phoneNumber = null;
    String kametiName = null;
    long kametiId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auctions);
        phoneNumber = getIntent().getExtras().getString("phoneNumber");
        kametiId = getIntent().getExtras().getLong("kametiId");
        kametiName = getIntent().getExtras().getString("kametiName");
        TextView kametiNameView = (TextView) findViewById(R.id.value_kamet_name);
        kametiNameView.setText(kametiName);

        //TODO: Fetch latest list of auctions from server

        KametiDbHelper kametiDbHelper = new KametiDbHelper(getBaseContext(), phoneNumber);
        SQLiteDatabase db = kametiDbHelper.getReadableDatabase();
        String[] dbSelect = {"`auction_id` as _id", "`auction_date`", "`auction_start_time`", "`minimum_bid_amount`"};
        String dbWhere = "`kameti_id`=" + kametiId;
        String[] dbArgs = null;
        String dbGroupBy = null;
        String dbFilterBy = null;
        String dbSortBy = "`auction_start_time`";
        Cursor c = db.query("`auction`", dbSelect, dbWhere, dbArgs, dbGroupBy, dbFilterBy, dbSortBy);
        if (c != null) {
            ListView list = (ListView) findViewById(R.id.listView);
            String[] from = {"auction_date", "auction_start_time", "minimum_bid_amount"};
            int[] to = {R.id.auctionDate, R.id.auctionStartTime, R.id.minimumBidAmount};
            SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                    this,
                    R.layout.list_auction,
                    c,
                    from,
                    to,
                    CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER
            );
            list.setAdapter(adapter);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> row, View element, int elementPosition, long rowId) {
                    //TODO: Open auction details
                }
            });
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.auctions, menu);
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
}
