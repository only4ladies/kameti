package com.kameti.kameti;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class Kameties extends Activity {

    String phoneNumber = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kameties);
        phoneNumber = getIntent().getExtras().getString("phoneNumber");

        //TODO: Fetch latest list of kameties from server

        TableLayout tableLayout = (TableLayout) findViewById(R.id.tableLayout);
        KametiDbHelper kametiDbHelper = new KametiDbHelper(getBaseContext(), phoneNumber);
        SQLiteDatabase db = kametiDbHelper.getReadableDatabase();
        String[] dbSelect = {"`kameti_id` as _id", "`kameti_name`", "`kameti_amount`", "`user_name`"};
        String dbWhere = "`kameti`.`admin_id` = `members`.`member_id`";
        String[] dbArgs = null;
        String dbGroupBy = null;
        String dbFilterBy = null;
        String dbSortBy = null;
        Cursor c = db.query("`kameti`, `members`", dbSelect, dbWhere, dbArgs, dbGroupBy, dbFilterBy, dbSortBy);
        if (c != null && c.moveToFirst()) {
            while(true){
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

                TableRow tableRow = new TableRow(this);
                tableRow.setLayoutParams(rowSize);
                tableRow.setClickable(true);
                tableRow.setBackground(border);
                tableRow.setOnClickListener(new OnRowClickListener(c.getLong(0)) {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), ViewKameti.class);
                        intent.putExtra("phoneNumber", phoneNumber);
                        intent.putExtra("kametiId", rowId);
                        startActivity(intent);
                    }
                });

                ImageView imageView = new ImageView(this);
                imageView.setLayoutParams(imageSize);
                imageView.setImageResource(R.drawable.ic_kameti);

                TextView kametiName = new TextView(this);
                kametiName.setEllipsize(TextUtils.TruncateAt.END);
                kametiName.setLines(2);
                kametiName.setPadding(10, 10, 10, 10);
                kametiName.setTextSize(20f);
                kametiName.setTypeface(null, Typeface.BOLD);
                kametiName.setTextColor(Color.parseColor("#ff9f1346"));
                kametiName.setGravity(Gravity.CENTER_VERTICAL);
                kametiName.setLayoutParams(textSize);
                kametiName.setText(c.getString(1));

                TextView kametiAmount = new TextView(this);
                kametiAmount.setEllipsize(TextUtils.TruncateAt.END);
                kametiAmount.setLines(2);
                kametiAmount.setPadding(10, 10, 10, 10);
                kametiAmount.setTextSize(20f);
                kametiAmount.setTypeface(null, Typeface.ITALIC);
                kametiAmount.setTextColor(Color.parseColor("#dc8d8d8d"));
                kametiAmount.setGravity(Gravity.CENTER_VERTICAL);
                kametiAmount.setLayoutParams(textSize);
                kametiAmount.setText(c.getString(2) + " INR");

                TextView userName = new TextView(this);
                userName.setEllipsize(TextUtils.TruncateAt.END);
                userName.setLines(2);
                userName.setPadding(10, 10, 10, 10);
                userName.setTextSize(20f);
                userName.setTypeface(null, Typeface.ITALIC);
                userName.setTextColor(Color.parseColor("#dc8d8d8d"));
                userName.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
                userName.setLayoutParams(textSize);
                userName.setText("Created by " + c.getString(3));

                tableRow.addView(imageView);
                tableRow.addView(kametiName);
                tableRow.addView(kametiAmount);
                tableRow.addView(userName);
                tableLayout.addView(tableRow);

                if(c.isLast()) break;
                c.moveToNext();
            }
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
        //TODO: Edit profile feature
        return super.onOptionsItemSelected(item);
    }
}
