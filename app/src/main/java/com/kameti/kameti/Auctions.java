package com.kameti.kameti;

import android.app.ActionBar;
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
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class Auctions extends Activity {

    String phoneNumber = null;
    String kametiName = null;
    Long current_time = null;
    int kametiMembers;
    long kametiId;

    String API_TIME = "http://aaagrawa7-win7:8082/kameti/time.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auctions);
        phoneNumber = getIntent().getExtras().getString("phoneNumber");
        kametiId = getIntent().getExtras().getLong("kametiId");
        kametiName = getIntent().getExtras().getString("kametiName");
        kametiMembers = getIntent().getExtras().getInt("kametiMembers");
        TextView kametiNameView = (TextView) findViewById(R.id.value_kamet_name);
        kametiNameView.setText(kametiName);

        //TODO: Fetch latest list of auctions from server

        new CallAPI(new handlerGetTime(), getApplicationContext()).execute(API_TIME);
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
    private class handlerGetTime implements ApiHandler {
        @Override
        public void execute(String response) {
            try {
                JSONObject reader = new JSONObject(response);
                String result = reader.getString("result");
                if ("OK".equalsIgnoreCase(result.trim())) {
                    current_time = new Long(reader.getLong("current_time"));
                }
            }
            catch (JSONException e){

            }
        }
        @Override
        public void postExecute() {
            TableLayout tableLayout = (TableLayout) findViewById(R.id.auctionsTable);
            KametiDbHelper kametiDbHelper = new KametiDbHelper(getBaseContext(), phoneNumber);
            SQLiteDatabase db = kametiDbHelper.getReadableDatabase();
            String[] dbSelect = {"`auction_id` as _id", "`auction_date`", "`auction_start_time`", "`minimum_bid_amount`", "`auction_end_time`", "`auction_winner`"};
            String dbWhere = "`kameti_id`=" + kametiId;
            String[] dbArgs = null;
            String dbGroupBy = null;
            String dbFilterBy = null;
            String dbSortBy = "`auction_start_time`";
            Cursor c = db.query("`auction`", dbSelect, dbWhere, dbArgs, dbGroupBy, dbFilterBy, dbSortBy);
            if (c != null && c.moveToFirst()) {
                while(true){
                    BidDuration bidDuration = new BidDuration(c.getString(1), c.getString(2), c.getString(4), 1);

                    ShapeDrawable border = new ShapeDrawable(new RectShape());
                    border.getPaint().setStyle(Paint.Style.STROKE);
                    border.getPaint().setColor(Color.parseColor("#ffccbf7d"));

                    TableLayout.LayoutParams rowSize = new TableLayout.LayoutParams(
                            TableLayout.LayoutParams.MATCH_PARENT,
                            TableLayout.LayoutParams.WRAP_CONTENT
                    );
                    TableRow.LayoutParams imageSize = new TableRow.LayoutParams(
                            TableRow.LayoutParams.WRAP_CONTENT,
                            TableRow.LayoutParams.MATCH_PARENT
                    );
                    TableRow.LayoutParams textSize = new TableRow.LayoutParams(
                            TableRow.LayoutParams.WRAP_CONTENT,
                            TableRow.LayoutParams.MATCH_PARENT,
                            1f
                    );

                    TableRow tableRow = new TableRow(getApplicationContext());
                    tableRow.setLayoutParams(rowSize);
                    tableRow.setClickable(true);
                    tableRow.setBackground(border);
                    if(current_time != null && bidDuration.contains(current_time.longValue())){
                        tableRow.setBackgroundColor(Color.WHITE);
                    }
                    tableRow.setOnClickListener(new OnRowClickListener(c.getLong(0)) {
                        @Override
                        public void onClick(View view) {
                            //TODO: Show auction where auction_id = rowId
                        }
                    });

                    ImageView imageView = new ImageView(getApplicationContext());
                    imageView.setLayoutParams(imageSize);
                    imageView.setImageResource(R.drawable.ic_auction);

                    TextView auctionDate = new TextView(getApplicationContext());
                    auctionDate.setEllipsize(TextUtils.TruncateAt.END);
                    auctionDate.setLines(2);
                    auctionDate.setPadding(10, 10, 10, 10);
                    auctionDate.setTextSize(20f);
                    auctionDate.setTypeface(null, Typeface.BOLD);
                    auctionDate.setTextColor(Color.parseColor("#ff9f1346"));
                    auctionDate.setGravity(Gravity.CENTER_VERTICAL);
                    auctionDate.setLayoutParams(textSize);
                    auctionDate.setText(c.getString(1));

                    TextView auctionStartTime = new TextView(getApplicationContext());
                    auctionStartTime.setEllipsize(TextUtils.TruncateAt.END);
                    auctionStartTime.setLines(2);
                    auctionStartTime.setPadding(10, 10, 10, 10);
                    auctionStartTime.setTextSize(20f);
                    auctionStartTime.setTypeface(null, Typeface.ITALIC);
                    auctionStartTime.setTextColor(Color.parseColor("#dc8d8d8d"));
                    auctionStartTime.setGravity(Gravity.CENTER_VERTICAL);
                    auctionStartTime.setLayoutParams(textSize);
                    if(current_time != null && bidDuration.contains(current_time.longValue())){
                        auctionStartTime.setText("Running");
                    }
                    else if(c.isNull(5)){
                        auctionStartTime.setText(c.getString(2));
                    }
                    else {
                        auctionStartTime.setText("Closed");
                    }

                    TextView minimumBidAmount = new TextView(getApplicationContext());
                    minimumBidAmount.setEllipsize(TextUtils.TruncateAt.END);
                    minimumBidAmount.setLines(2);
                    minimumBidAmount.setPadding(10, 10, 10, 10);
                    minimumBidAmount.setTextSize(20f);
                    minimumBidAmount.setTypeface(null, Typeface.ITALIC);
                    minimumBidAmount.setTextColor(Color.parseColor("#dc8d8d8d"));
                    minimumBidAmount.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
                    minimumBidAmount.setLayoutParams(textSize);
                    minimumBidAmount.setText("Bid starts from " + c.getString(3) + " INR");

                    tableRow.addView(imageView);
                    tableRow.addView(auctionDate);
                    tableRow.addView(auctionStartTime);
                    tableRow.addView(minimumBidAmount);
                    tableLayout.addView(tableRow);

                    if(c.isLast()) break;
                    c.moveToNext();
                }
            }
        }
    }
}
