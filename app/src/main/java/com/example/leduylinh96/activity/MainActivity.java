package com.example.leduylinh96.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.leduylinh96.receiver.NoteBroadcast;
import com.example.leduylinh96.MainScreen.R;
import com.example.leduylinh96.custom.GridSpacingItemDecoration;
import com.example.leduylinh96.adapter.RecyclerAdapter;
import com.example.leduylinh96.model.MyNote;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private final int REQUEST_CODE = 0;
    private RecyclerView mrvShowNote;
    private ArrayList<MyNote> mArrayList = new ArrayList<MyNote>();
    private RecyclerAdapter mRecyclerAdapter;
    private Resources resources;
    private SQLiteDatabase mDatabase;
    public final String MY_TABLE = "myNote";
    public static final int SHOWNOTE_REQUESTCODE = 9999;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resources = getResources();
        String sqlCreateTable = "create table if not exists " + MY_TABLE + "(" +
                "id long not null," +
                "title varchar(100), " +
                "note varchar(200), " +
                "date varchar(10), " +
                "time varchar(10)," +
                "dayCreated varchar(12)," +
                "color int," +
                "imagesUri varchar(400)," +
                "alarm int," +
                "primary key (id));";

        mDatabase = openOrCreateDatabase("note.sqlite", SQLiteDatabase.CREATE_IF_NECESSARY, null);
        mDatabase.execSQL(sqlCreateTable);
        readAllData();
        mrvShowNote.addItemDecoration(new GridSpacingItemDecoration(2, 10, true, 0));
    }

    private void readAllData() {
        Cursor cursor = mDatabase.rawQuery("select * from " + MY_TABLE, null);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        mArrayList.clear();
        while (cursor.moveToNext()) {
            MyNote myNote = new MyNote(cursor.getString(1),
                    cursor.getString(2), cursor.getString(3),
                    cursor.getString(4), cursor.getString(5),
                    cursor.getInt(6), cursor.getString(7), cursor.getInt(8), cursor.getLong(0));
            mArrayList.add(myNote);
        }
        cursor.close();
        mrvShowNote = (RecyclerView) findViewById(R.id.rvShowNote);
        mRecyclerAdapter = new RecyclerAdapter(mArrayList, this);
        mrvShowNote.setLayoutManager(gridLayoutManager);
        mrvShowNote.setAdapter(mRecyclerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnuAdd:
                Intent intent = new Intent(MainActivity.this, ActivityAddNote.class);
                startActivityForResult(intent, REQUEST_CODE);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            //TO-DO
            switch (requestCode) {
                case REQUEST_CODE:
                    SQLiteDatabase db = openOrCreateDatabase("note.sqlite", MODE_PRIVATE, null);
                    MyNote myNote = (MyNote) data.getSerializableExtra("key");
                    ContentValues contentValues = new ContentValues();

                    contentValues.put("id", myNote.getId());
                    contentValues.put("title", myNote.getTitle());
                    contentValues.put("note", myNote.getNote());
                    contentValues.put("date", myNote.getDate());
                    contentValues.put("time", myNote.getTime());
                    contentValues.put("dayCreated", myNote.getDayCreated());
                    contentValues.put("color", myNote.getColor());
                    contentValues.put("imagesUri", myNote.getImagesUri());
                    contentValues.put("alarm", myNote.getAlarm());

                    long j = db.insert(MY_TABLE, null, contentValues);
                    if (j == -1)
                        Toast.makeText(this, "Fail" + j, Toast.LENGTH_LONG).show();
                    mArrayList.add(myNote);
                    mRecyclerAdapter.notifyDataSetChanged();
                    break;
                case SHOWNOTE_REQUESTCODE:
                    int position = data.getIntExtra(ActivityShowNote.DELETED_POSITION, -1);
                    if (position != -1) {
                        mArrayList.remove(position);
                        mRecyclerAdapter.notifyItemRemoved(position);
                    } else {
                        Toast.makeText(this, "Error ocured, Can't remove", Toast.LENGTH_LONG);
                    }
                    break;
            }
        }
        if (resultCode == ActivityShowNote.RESULTCODE_NODECREATED) {
            switch (requestCode) {
                case SHOWNOTE_REQUESTCODE:
                    SQLiteDatabase db = openOrCreateDatabase("note.sqlite", MODE_PRIVATE, null);
                    MyNote myNote = (MyNote) data.getSerializableExtra("key");
                    ContentValues contentValues = new ContentValues();

                    contentValues.put("id",myNote.getId());
                    contentValues.put("title", myNote.getTitle());
                    contentValues.put("note", myNote.getNote());
                    contentValues.put("date", myNote.getDate());
                    contentValues.put("time", myNote.getTime());
                    contentValues.put("dayCreated", myNote.getDayCreated());
                    contentValues.put("color", myNote.getColor());
                    contentValues.put("imagesUri", myNote.getImagesUri());
                    contentValues.put("alarm", myNote.getAlarm());

                    long j = db.insert(MY_TABLE, null, contentValues);
                    if (j == -1)
                        Toast.makeText(this, "Fail" + j, Toast.LENGTH_LONG).show();
                    mArrayList.add(myNote);
                    mRecyclerAdapter.notifyDataSetChanged();
                    break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        readAllData();
    }
}


