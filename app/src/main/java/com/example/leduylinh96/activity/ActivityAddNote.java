package com.example.leduylinh96.activity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.leduylinh96.adapter.CameraDialogAdapter;
import com.example.leduylinh96.adapter.PictureAdapter;
import com.example.leduylinh96.custom.ColorsDialog;
import com.example.leduylinh96.custom.DiaglogCameraItem;
import com.example.leduylinh96.model.MyNote;
import com.example.leduylinh96.receiver.NoteBroadcast;
import com.example.leduylinh96.MainScreen.R;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

import static android.view.View.GONE;

public class ActivityAddNote extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private TextView mtvDate;
    private ImageView ivCamera;
    private ImageView mIvClear;
    private TextView mTvAlarm;
    private GridView mGridView;
    private Calendar calendar = Calendar.getInstance();
    private SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
    private SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
    private SimpleDateFormat sdf3 = new SimpleDateFormat("EEE");
    private SimpleDateFormat sdf4 = new SimpleDateFormat("dd/MM HH:mm");
    private String mUri = "";

    private Spinner mSpDate;
    private Spinner mSpTime;
    private EditText medTitle, medNote;
    private final int CAMERA_REQUEST = 6996, RESULT_CODE = 1;

    private final int PICK_IMAGE = 2017;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_screen);
        getWindow().getDecorView().getRootView().setBackgroundColor(Color.WHITE);
        addControls();
        addEvents();
    }

    private void addControls() {
        mSpDate = (Spinner) findViewById(R.id.addnote_spDate);
        ArrayList<String> arrDate = new ArrayList<String>();
        String[] str = getResources().getStringArray(R.array.strArrDate);
        for (String item : str)
            arrDate.add(item);
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrDate);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        adapter.remove("Next");
        adapter.insert("Next " + sdf3.format(new Date()), 2);
        mSpDate.setAdapter(adapter);

        mSpTime = (Spinner) findViewById(R.id.addnote_spTime);
        ArrayList<String> arrTime = new ArrayList<String>();
        str = getResources().getStringArray(R.array.strArrTime);
        for (String item : str)
            arrTime.add(item);
        ArrayAdapter<String> adapter1 =
                new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrTime);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpTime.setAdapter(adapter1);

        mtvDate = (TextView) findViewById(R.id.tvDate);
        ivCamera = (ImageView) findViewById(R.id.ivCamera);
        medNote = (EditText) findViewById(R.id.editTextNote);
        medTitle = (EditText) findViewById((R.id.editTextTitle));
        mIvClear = (ImageView) findViewById(R.id.addnote_imgClear);
        mTvAlarm = (TextView) findViewById(R.id.addnote_tvAlarm);

        PictureAdapter pa = new PictureAdapter(this, R.layout.gv_picture_item, arrayList);
        mGridView = (GridView) findViewById(R.id.gvPicture);
        mGridView.setAdapter(pa);
    }

    private void addEvents() {
        String str = sdf1.format(calendar.getTime()) + " " + sdf2.format(calendar.getTime());
        mtvDate.setText(str);
        mSpTime.setOnItemSelectedListener(this);
        mSpDate.setOnItemSelectedListener(this);
        mIvClear.setOnClickListener(this);
        mTvAlarm.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mni = getMenuInflater();
        mni.inflate(R.menu.menu_sub_screen, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_camera:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                ArrayList<DiaglogCameraItem> arr = new ArrayList<DiaglogCameraItem>();
                arr.add(new DiaglogCameraItem("Take Photo", R.drawable.take_photo));
                arr.add(new DiaglogCameraItem("Choose Photo", R.drawable.choose_photo));
                CameraDialogAdapter cda = new CameraDialogAdapter(this, R.layout.camera_dialog_item, arr);
                builder.setTitle("Insert Picture").setAdapter(cda, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                                }
                                break;
                            case 1:
                                Toast.makeText(ActivityAddNote.this, "u click choose photo", Toast.LENGTH_SHORT).show();

                                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                                    Intent intent;
                                    intent = new Intent();
                                    intent.setAction(Intent.ACTION_GET_CONTENT);
                                    intent.setType("image/*");
                                    startActivityForResult(intent, PICK_IMAGE);
                                } else {
                                    Intent intent;
                                    intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                                    intent.setType("image/*");
                                    startActivityForResult(intent, PICK_IMAGE);
                                }
                                break;
                        }
                    }
                }).show();
                break;
            case R.id.item_view:
                ColorsDialog colorsDialog = new ColorsDialog();
                colorsDialog.show(getFragmentManager(), "abc");
                break;
            case R.id.item_done:
                String myEdNoteStr = medNote.getText().toString();
                String myEdTitleStr = medTitle.getText().toString();
                Drawable background = getWindow().getDecorView().getRootView().getBackground();
                int color = ((ColorDrawable) background).getColor();

                ((ColorDrawable) background).setColor(color);

                int check = mIvClear.getVisibility();
                if (myEdNoteStr.matches("") && myEdTitleStr.matches("") && check == GONE) {
                    finish();
                } else {
                    int alarmOrNot = 0;
                    if (mIvClear.getVisibility() != GONE)
                        alarmOrNot = 1;
                    Calendar calendar = Calendar.getInstance();
                    Date myDate = new Date();
                    MyNote myNote = new MyNote(medTitle.getText().toString(),
                            medNote.getText().toString(), mSpDate.getSelectedItem().toString(),
                            mSpTime.getSelectedItem().toString(), sdf4.format(myDate), color, mUri.trim(),
                            alarmOrNot, calendar.getTimeInMillis() % 1000000);
                    if (alarmOrNot == 1) {
                        switch (mSpDate.getSelectedItemPosition()) {
                            case 3:
                                StringTokenizer stringTokenizer = new StringTokenizer(mSpDate.getSelectedItem().toString(), "/");
                                int day = Integer.valueOf(stringTokenizer.nextToken());
                                int month = Integer.valueOf(stringTokenizer.nextToken());
                                int year = Integer.valueOf(stringTokenizer.nextToken());
                                calendar.set(Calendar.DAY_OF_MONTH, day);
                                calendar.set(Calendar.MONTH, month - 1);
                                calendar.set(Calendar.YEAR, year);
                                break;
                            case 0:
                                break;
                            case 1:
                                day = calendar.get(Calendar.DAY_OF_MONTH);
                                calendar.set(Calendar.DAY_OF_MONTH, day + 1);
                                break;
                            case 2:
                                day = calendar.get(Calendar.DAY_OF_MONTH);
                                calendar.set(Calendar.DAY_OF_MONTH, day + 7);
                                break;
                        }
                        StringTokenizer stringTokenizer = new StringTokenizer(mSpTime.getSelectedItem().toString(), ":");
                        int hour = Integer.valueOf(stringTokenizer.nextToken());
                        int minute = Integer.valueOf(stringTokenizer.nextToken());
                        calendar.set(Calendar.HOUR_OF_DAY, hour);
                        calendar.set(Calendar.MINUTE, minute);
                        calendar.set(Calendar.SECOND, 0);

                        Intent myIntent = new Intent(this, NoteBroadcast.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("myNote", myNote);
                        bundle.putInt("int", 1);
                        myIntent.putExtras(bundle);

                        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, (int) myNote.getId(), myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

                        //Test
                    } else {
                        Intent intent = new Intent(this, NoteBroadcast.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("myNote", myNote);
                        bundle.putInt("int", 1);
                        intent.putExtras(bundle);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, (int) myNote.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
                        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                        alarmManager.cancel(pendingIntent);
                    }
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("key", myNote);
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    ArrayList<Bitmap> arrayList = new ArrayList<Bitmap>();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CAMERA_REQUEST) {
            if (data == null) return;
            try {
                Bundle extras = data.getExtras();
                File file = createImageFile();
                mUri = mUri + " " + file.toURI();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                FileOutputStream fot = new FileOutputStream(file);
                BufferedOutputStream bot = new BufferedOutputStream(fot);
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bot);
                bot.flush();
                bot.close();
                fot.close();
                arrayList.add(imageBitmap);
                PictureAdapter a = (PictureAdapter) mGridView.getAdapter();
                a.notifyDataSetChanged();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (requestCode == PICK_IMAGE && data != null) {
            try {
                Uri selectedimg = data.getData();
                mUri = mUri + " " + selectedimg;
                Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedimg);
                arrayList.add(imageBitmap);
                PictureAdapter a = (PictureAdapter) mGridView.getAdapter();
                a.notifyDataSetChanged();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addnote_imgClear:
                mTvAlarm.setVisibility(View.VISIBLE);
                mSpDate.setVisibility(View.INVISIBLE);
                mSpTime.setVisibility(View.INVISIBLE);
                mIvClear.setVisibility(GONE);

                break;
            case R.id.addnote_tvAlarm:
                mTvAlarm.setVisibility(GONE);
                mSpDate.setVisibility(View.VISIBLE);
                mSpTime.setVisibility(View.VISIBLE);
                mIvClear.setVisibility(View.VISIBLE);

                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.addnote_spTime:
                timeItemSelected(position);
                break;
            case R.id.addnote_spDate:
                dateItemSelected(position);
                break;
        }
    }

    private void dateItemSelected(int position) {
        switch (position) {
            case 0:
            case 1:
            case 2:
                ArrayAdapter<String> myDateArr = (ArrayAdapter<String>) mSpDate.getAdapter();
                myDateArr.remove(str);
                if (myDateArr.getCount() == 3)
                    myDateArr.insert("Other...", 3);
                break;
            case 3:
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        ActivityAddNote.this, datePicked, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
                break;
        }
    }

    private String str;
    DatePickerDialog.OnDateSetListener datePicked = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            str = String.format("%d/%d/%d", dayOfMonth, month + 1, year);
            ArrayAdapter<String> myDateArr = (ArrayAdapter<String>) mSpDate.getAdapter();
            myDateArr.remove("Other...");
            if (myDateArr.getCount() == 3)
                myDateArr.insert(str, 3);
        }
    };

    private void timeItemSelected(int position) {
        switch (position) {
            case 0:
            case 1:
            case 2:
            case 3:
                ArrayAdapter<String> myArrTime = (ArrayAdapter<String>) mSpTime.getAdapter();
                myArrTime.remove(strDate);
                if (myArrTime.getCount() == 4)
                    myArrTime.insert("Other...", myArrTime.getCount());
                break;
            case 4:
                TimePickerDialog timePickerDialog = new TimePickerDialog(ActivityAddNote.this, timePicked, 16, 0, true);
                timePickerDialog.show();
                break;
        }
    }

    private String strDate;
    TimePickerDialog.OnTimeSetListener timePicked = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            strDate = String.format("%d:%d", hourOfDay, minute);
            ArrayAdapter<String> myArrTime = (ArrayAdapter<String>) mSpTime.getAdapter();
            myArrTime.remove("Other...");
            if (myArrTime.getCount() == 4)
                myArrTime.insert(strDate, myArrTime.getCount());
        }
    };

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        return image;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public Notification createNewNotification(MyNote myNote) {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        if (myNote != null) {
            notificationBuilder.setContentTitle(myNote.getTitle());
            notificationBuilder.setContentText(myNote.getNote());
            notificationBuilder.setSmallIcon(R.drawable.delete_white);

            Intent intent = new Intent(this, MainActivity.class);
            TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
            taskStackBuilder.addParentStack(MainActivity.class);
            taskStackBuilder.addNextIntent(intent);

            PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            notificationBuilder.setContentIntent(pendingIntent);
        }
        return notificationBuilder.build();
    }

}
