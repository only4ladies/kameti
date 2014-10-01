package com.kameti.kameti;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Bids extends Activity {

    String phoneNumber = null;
    long kametiId;
    String kametiName = null;
    long auctionId;
    String auctionName = null;
    String auctionStatus = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bids);
        phoneNumber = getIntent().getExtras().getString("phoneNumber");
        kametiId = getIntent().getExtras().getLong("kametiId");
        kametiName = getIntent().getExtras().getString("kametiName");
        auctionId = getIntent().getExtras().getLong("auctionId");
        auctionName = getIntent().getExtras().getString("auctionName");
        auctionStatus = getIntent().getExtras().getString("auctionStatus");

        TextView auctionNameView = (TextView) findViewById(R.id.value_auction_name);
        auctionNameView.setText(kametiName + " " + auctionName);

        //TODO: Fetch latest list of auctions from server

        TableLayout tableLayout = (TableLayout) findViewById(R.id.bidsTable);
        KametiDbHelper kametiDbHelper = new KametiDbHelper(getBaseContext(), phoneNumber);
        SQLiteDatabase db = kametiDbHelper.getReadableDatabase();
        String[] dbSelect = {"`id` as _id", "`member_id`", "`bid_time`", "`bid_amount`"};
        String dbWhere = "`kameti_id`=" + kametiId + " AND `auction_id`=" + auctionId;
        String[] dbArgs = null;
        String dbGroupBy = null;
        String dbFilterBy = null;
        String dbSortBy = "`bid_time` DESC";
        Cursor c = db.query("`bid`", dbSelect, dbWhere, dbArgs, dbGroupBy, dbFilterBy, dbSortBy);
        if (c != null && c.moveToFirst()) {
            while (true) {
                ShapeDrawable border = new ShapeDrawable(new RectShape());
                border.getPaint().setStyle(Paint.Style.STROKE);
                border.getPaint().setColor(Color.parseColor("#ffccbf7d"));

                TableLayout.LayoutParams rowSize = new TableLayout.LayoutParams(
                        TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.WRAP_CONTENT
                );
                TableRow.LayoutParams textSize = new TableRow.LayoutParams(
                        TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.MATCH_PARENT,
                        1f
                );

                TableRow tableRow = new TableRow(getApplicationContext());
                tableRow.setLayoutParams(rowSize);
                tableRow.setBackground(border);

                Cursor bidder = db.rawQuery("SELECT `user_name` from `members` WHERE `member_id`=" + c.getString(1), null);
                String bidderNameValue = "";
                if(bidder.moveToFirst()){
                    bidderNameValue = bidder.getString(0);
                }
                TextView bidderName = new TextView(getApplicationContext());
                bidderName.setEllipsize(TextUtils.TruncateAt.END);
                bidderName.setLines(2);
                bidderName.setPadding(20, 10, 10, 10);
                bidderName.setTextSize(20f);
                bidderName.setTextColor(Color.parseColor("#ff9f1346"));
                bidderName.setGravity(Gravity.CENTER_VERTICAL);
                bidderName.setLayoutParams(textSize);
                bidderName.setText(bidderNameValue);

                String bidTimeValue = "";
                try {
                    Date bidTimestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(c.getString(2));
                    bidTimeValue = new SimpleDateFormat("HH:mm:ss").format(bidTimestamp);
                } catch (ParseException e) {
                    bidTimeValue = "";
                }
                TextView bidTime = new TextView(getApplicationContext());
                bidTime.setEllipsize(TextUtils.TruncateAt.END);
                bidTime.setLines(2);
                bidTime.setPadding(10, 10, 10, 10);
                bidTime.setTextSize(20f);
                bidTime.setTypeface(null, Typeface.ITALIC);
                bidTime.setTextColor(Color.parseColor("#dc8d8d8d"));
                bidTime.setGravity(Gravity.CENTER_VERTICAL);
                bidTime.setLayoutParams(textSize);
                bidTime.setText(bidTimeValue);

                TextView bidAmount = new TextView(getApplicationContext());
                bidAmount.setEllipsize(TextUtils.TruncateAt.END);
                bidAmount.setLines(2);
                bidAmount.setPadding(10, 10, 10, 10);
                bidAmount.setTextSize(20f);
                bidAmount.setTextColor(Color.parseColor("#ff9f1346"));
                bidAmount.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
                bidAmount.setLayoutParams(textSize);
                bidAmount.setText(c.getString(3) + " INR");

                tableRow.addView(bidderName);
                tableRow.addView(bidTime);
                tableRow.addView(bidAmount);
                tableLayout.addView(tableRow);

                if(c.isLast()) break;
                c.moveToNext();
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bids, menu);
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
