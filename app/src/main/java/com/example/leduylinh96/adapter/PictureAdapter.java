package com.example.leduylinh96.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.leduylinh96.MainScreen.R;

import java.util.ArrayList;

/**
 * Created by leduylinh96 on 3/24/2017.
 */

public class PictureAdapter extends ArrayAdapter {

    private Activity context;
    private ArrayList<Bitmap> objects;
    private int resource;
    public PictureAdapter(@NonNull Activity context, @LayoutRes int resource, @NonNull ArrayList<Bitmap> objects) {
        super(context, resource, objects);
        this.context = context;
        this.objects = objects;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = context.getLayoutInflater();
        View view = layoutInflater.inflate(resource, null);
        ImageView iv = (ImageView) view.findViewById(R.id.ivPicture);
        iv.setImageBitmap(objects.get(position));
        return view;
    }
}
