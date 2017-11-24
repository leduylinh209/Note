package com.example.leduylinh96.activity;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.Menu;
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

import com.example.leduylinh96.receiver.NoteBroadcast;
import com.example.leduylinh96.MainScreen.R;
import com.example.leduylinh96.model.MyNote;
import com.example.leduylinh96.adapter.CameraDialogAdapter;
import com.example.leduylinh96.custom.DiaglogCameraItem;
import com.example.leduylinh96.custom.ColorsDialog;
import com.example.leduylinh96.adapter.PictureAdapter;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

import static android.view.View.GONE;

public class ActivityShowNote extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private static final int PERMISSION_REQUEST_CODE = 996;
    private ArrayList<String> mMyArrTime;
    private ArrayList<String> mMyArrDate;

    private ArrayList<MyNote> mMyNotes;
    private int mMyNotesPosition;

    private ImageView mIvLeftArrow, mIvRightArrow, mIvDelete, mIvClear, mIvShareButton, mIvTest;
    private EditText mEdtNote, mEdtTitle;
    private TextView mTvDate, mTvAlarm;
    private Spinner mSpDate, mSpTime;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE");
    private Date date = new Date();
    private Calendar calendar = Calendar.getInstance();
    private GridView mGvShowImages;
    private ArrayList<Bitmap> arrayList = new ArrayList<>();
    private SQLiteDatabase myDatabase;

    public static final int RESULTCODE_NODECREATED = 311;
    public static final String DELETED_POSITION = "deleted position";
    public static final int CAMERA_REQUEST = 123;
    public static final int PICK_IMAGE = 911;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        calendar.setTime(date);
        setContentView(R.layout.activity_show_note);
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("BUNDLE");

        myDatabase = openOrCreateDatabase("note.sqlite", SQLiteDatabase.CREATE_IF_NECESSARY, null);

        mMyNotes = (ArrayList<MyNote>) bundle.getSerializable("ARRAYLIST");
        mMyNotesPosition = bundle.getInt("POSITION");
        mGvShowImages = (GridView) findViewById(R.id.shownote_gvShowImage);
        PictureAdapter pa = new PictureAdapter(this, R.layout.gv_picture_item, arrayList);
        mGvShowImages.setAdapter(pa);

        mIvDelete = (ImageView) findViewById(R.id.shownote_ivDelete);
        mIvLeftArrow = (ImageView) findViewById(R.id.shownote_ivLeftArrow);
        mIvRightArrow = (ImageView) findViewById(R.id.shownote_ivRightArrow);
        mIvClear = (ImageView) findViewById(R.id.shownote_imgClear);
        mIvShareButton = (ImageView) findViewById(R.id.shownote_share);

        mTvDate = (TextView) findViewById(R.id.shownote_tvDate);
        mTvAlarm = (TextView) findViewById(R.id.shownote_tvAlarm);
        mSpDate = (Spinner) findViewById(R.id.shownote_spDate);
        mSpTime = (Spinner) findViewById(R.id.shownote_spTime);
        mEdtNote = (EditText) findViewById(R.id.shownote_edtNote);
        mEdtTitle = (EditText) findViewById(R.id.shownote_edtTitle);

        mMyArrDate = new ArrayList<String>();
        String[] str = getResources().getStringArray(R.array.strArrDate);
        for (String item : str) mMyArrDate.add(item);
        ArrayAdapter<String> adapterDate =
                new ArrayAdapter<String>(
                        this, android.R.layout.simple_spinner_item, mMyArrDate);
        adapterDate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpDate.setAdapter(adapterDate);
        adapterDate.remove("Next");
        adapterDate.insert("Next " + simpleDateFormat.format(date), 2);

        mMyArrTime = new ArrayList<String>();
        str = getResources().getStringArray(R.array.strArrTime);
        for (String item : str) mMyArrTime.add(item);
        ArrayAdapter<String> adapterTime =
                new ArrayAdapter<String>(
                        this, android.R.layout.simple_spinner_item, mMyArrTime);
        adapterTime.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpTime.setAdapter(adapterTime);

        setData(adapterDate, adapterTime);

        mIvShareButton.setOnClickListener(this);
        mIvLeftArrow.setOnClickListener(this);
        mIvRightArrow.setOnClickListener(this);
        mTvAlarm.setOnClickListener(this);
        mIvClear.setOnClickListener(this);
        mSpDate.setOnItemSelectedListener(this);
        mSpTime.setOnItemSelectedListener(this);
        mIvDelete.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_show_note, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.shownote_option:
                View view = findViewById(R.id.shownote_option);
                PopupMenu popupMenu = new PopupMenu(this, view);
                popupMenu.getMenuInflater().inflate(R.menu.shownode_popup_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Intent intent = new Intent(ActivityShowNote.this, ActivityAddNote.class);
                        startActivityForResult(intent, 113);
                        return true;
                    }
                });
                popupMenu.show();
                break;
            case R.id.shownote_camera:
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
                                Toast.makeText(ActivityShowNote.this, "u click take photo", Toast.LENGTH_SHORT).show();
                                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                                }
                                break;
                            case 1:
                                Toast.makeText(ActivityShowNote.this, "u click choose photo", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent();
                                intent.setType("image/*");
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
                                break;
                        }
                    }
                }).show();
                break;
            case R.id.shownote_gridcolor:
                ColorsDialog colorsDialog = new ColorsDialog();
                colorsDialog.show(getFragmentManager(), "abc");
                break;
            case R.id.shownote_done:
                upDateDatabase();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private String mUri = "";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 113:
                    System.out.println(1);
                    setResult(RESULTCODE_NODECREATED, data);
                    finish();
                    break;
                case CAMERA_REQUEST:
                    try {
                        Bundle bundle = data.getExtras();
                        Bitmap bitmap = (Bitmap) bundle.get("data");
                        File file = createImageFile();
                        mUri = mUri + " " + file.toURI();
                        FileOutputStream fileOutputStream = new FileOutputStream(file);
                        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bufferedOutputStream);
                        bufferedOutputStream.flush();
                        bufferedOutputStream.close();
                        fileOutputStream.close();
                        arrayList.add(bitmap);
                        PictureAdapter pa = (PictureAdapter) mGvShowImages.getAdapter();
                        pa.notifyDataSetChanged();
                        break;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                case PICK_IMAGE:
                    try {
                        Uri uri = data.getData();
                        mUri = mUri + " " + uri;
                        Bitmap bitmap1 = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                        arrayList.add(bitmap1);
                        PictureAdapter abc = (PictureAdapter) mGvShowImages.getAdapter();
                        abc.notifyDataSetChanged();
                        System.out.println(uri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.shownote_tvAlarm:
                mTvAlarm.setVisibility(GONE);
                mSpTime.setVisibility(View.VISIBLE);
                mSpDate.setVisibility(View.VISIBLE);
                mIvClear.setVisibility(View.VISIBLE);
                break;
            case R.id.shownote_imgClear:
                mSpDate.setVisibility(GONE);
                mSpTime.setVisibility(GONE);
                mTvAlarm.setVisibility(View.VISIBLE);
                mIvClear.setVisibility(GONE);

                Intent intent = new Intent(this, NoteBroadcast.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("myNote",mMyNotes.get(mMyNotesPosition));
                bundle.putInt("int", 1);
                intent.putExtras(bundle);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(this, (int)mMyNotes.get(mMyNotesPosition).getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                alarmManager.cancel(pendingIntent);
                break;
            case R.id.shownote_ivLeftArrow:
                if (mMyNotesPosition > 0) {
                    mMyNotesPosition = mMyNotesPosition - 1;
                    setData((ArrayAdapter<String>) mSpDate.getAdapter(), (ArrayAdapter<String>) mSpTime.getAdapter());
                    mIvLeftArrow.setColorFilter(Color.parseColor("#000000"));
                } else {
                    mIvLeftArrow.setColorFilter(Color.parseColor("#ff0000"));
                }
                upDateDatabase();
                break;
            case R.id.shownote_ivRightArrow:
                if (mMyNotesPosition < mMyNotes.size() - 1) {
                    mMyNotesPosition = mMyNotesPosition + 1;
                    setData((ArrayAdapter<String>) mSpDate.getAdapter(), (ArrayAdapter<String>) mSpTime.getAdapter());
                    mIvRightArrow.setColorFilter(Color.parseColor("#000000"));
                } else {
                    mIvRightArrow.setColorFilter(Color.parseColor("#ff0000"));
                }
                upDateDatabase();
                break;
            case R.id.shownote_ivDelete:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Confirm Delete").setMessage("Are you sure want to delete this ?");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SQLiteDatabase database = openOrCreateDatabase("note.sqlite", MODE_PRIVATE, null);
                        database.execSQL("delete from myNote where note=? and title=?",
                                new String[]{mMyNotes.get(mMyNotesPosition).getNote(), mMyNotes.get(mMyNotesPosition).getTitle()});
                        Intent intentResult = new Intent();
                        intentResult.putExtra(DELETED_POSITION, mMyNotesPosition);
                        setResult(RESULT_OK, intentResult);
                        finish();
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();

                break;
            case R.id.shownote_share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, mEdtTitle.getText() + "\n" + mEdtNote.getText() + "\n");
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, "Share with..."));
                break;
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.shownote_spTime:
                timeItemSelected(position);
                break;
            case R.id.shownote_spDate:
                dateItemSelected(position);
                break;
        }
    }

    private void dateItemSelected(int position) {
        switch (position) {
            case 0:
            case 1:
            case 2:
                ArrayAdapter<String> arr = (ArrayAdapter<String>) mSpDate.getAdapter();
                arr.remove(arr.getItem(3));
                if (arr.getCount() == 3)
                    arr.add("Other...");
                break;
            case 3:
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        ActivityShowNote.this, datePicked, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
                break;
        }
    }

    String newStr;
    DatePickerDialog.OnDateSetListener datePicked = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            newStr = String.format("%d/%d/%d", dayOfMonth, month + 1, year);
            ArrayAdapter<String> myDateArr = (ArrayAdapter<String>) mSpDate.getAdapter();
            String oldStr = myDateArr.getItem(3);
            myDateArr.remove(oldStr);
            if (myDateArr.getCount() == 3)
                myDateArr.insert(newStr, 3);
        }
    };

    private void timeItemSelected(int position) {
        switch (position) {
            case 0:
            case 1:
            case 2:
            case 3:
                ArrayAdapter<String> myArrTime = (ArrayAdapter<String>) mSpTime.getAdapter();
                myArrTime.remove(myArrTime.getItem(4));
                if (myArrTime.getCount() == 4)
                    myArrTime.insert("Other...", myArrTime.getCount());
                break;
            case 4:
                TimePickerDialog timePickerDialog = new TimePickerDialog(ActivityShowNote.this, timePicked, 16, 0, true);
                timePickerDialog.show();
                break;
        }
    }

    String strDate;
    TimePickerDialog.OnTimeSetListener timePicked = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            strDate = String.format("%d:%d", hourOfDay, minute);
            ArrayAdapter<String> myArrTime = (ArrayAdapter<String>) mSpTime.getAdapter();
            String oldStr = myArrTime.getItem(4);
            myArrTime.remove(oldStr);
            if (myArrTime.getCount() == 4)
                myArrTime.insert(strDate, myArrTime.getCount());
        }
    };

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void setData(ArrayAdapter<String> adapterDate, ArrayAdapter<String> adapterTime) {
        mTvAlarm.setVisibility(GONE);
        mSpTime.setVisibility(View.VISIBLE);
        mSpDate.setVisibility(View.VISIBLE);
        mIvClear.setVisibility(View.VISIBLE);
        arrayList.clear();
        PictureAdapter pa = (PictureAdapter) mGvShowImages.getAdapter();
        pa.notifyDataSetChanged();
        mEdtNote.getRootView().setBackgroundColor(mMyNotes.get(mMyNotesPosition).getColor());
        mEdtNote.setText(mMyNotes.get(mMyNotesPosition).getNote());
        mEdtTitle.setText(mMyNotes.get(mMyNotesPosition).getTitle());
        mTvDate.setText(mMyNotes.get(mMyNotesPosition).getDayCreated());
        String oldStr = adapterDate.getItem(3);
        adapterDate.remove(oldStr);
        adapterDate.add(mMyNotes.get(mMyNotesPosition).getDate());
        mSpDate.setSelection(3, false);
        oldStr = adapterTime.getItem(4);
        adapterTime.remove(oldStr);
        adapterTime.add(mMyNotes.get(mMyNotesPosition).getTime());
        mSpTime.setSelection(4, false);
        if (mMyNotes.get(mMyNotesPosition).getAlarm() == 0) {
            mSpDate.setVisibility(GONE);
            mSpTime.setVisibility(GONE);
            mTvAlarm.setVisibility(View.VISIBLE);
            mIvClear.setVisibility(GONE);
        }
        showImage();
    }

    public void upDateDatabase() {
        MyNote myNote = mMyNotes.get(mMyNotesPosition);
        Drawable background = getWindow().getDecorView().getRootView().getBackground();
        int color = ((ColorDrawable) background).getColor();
        myDatabase.execSQL("delete from myNote where title = ? and note = ?", new String[]{myNote.getTitle(), myNote.getNote()});
        int check = 0;
        if (mIvClear.getVisibility() != GONE) check = 1;
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", myNote.getId());
        contentValues.put("title", mEdtTitle.getText().toString());
        contentValues.put("note", mEdtNote.getText().toString());
        contentValues.put("date", mSpDate.getSelectedItem().toString());
        contentValues.put("time", mSpTime.getSelectedItem().toString());
        contentValues.put("dayCreated", myNote.getDayCreated());
        contentValues.put("color", color);
        System.out.println(mUri);
        contentValues.put("imagesUri", mUri.trim());
        contentValues.put("alarm", check);
        myDatabase.insert("myNote", null, contentValues);
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, " .jpg", storageDir);
        return image;
    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

    private void showImage() {
        mUri = mUri + " " + mMyNotes.get(mMyNotesPosition).getImagesUri();
        StringTokenizer str = new StringTokenizer(mMyNotes.get(mMyNotesPosition).getImagesUri(), " ");
        while (str.hasMoreTokens()) {
            System.out.println(str);
            try {
                Uri uri = Uri.parse(str.nextToken());
                Bitmap bitmap = getBitmapFromUri(uri);
                arrayList.add(bitmap);
                PictureAdapter a = (PictureAdapter) mGvShowImages.getAdapter();
                a.notifyDataSetChanged();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
