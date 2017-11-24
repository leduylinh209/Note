package com.example.leduylinh96.adapter;

import android.app.Activity;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.leduylinh96.custom.DiaglogCameraItem;
import com.example.leduylinh96.MainScreen.R;

import java.util.List;

/**
 * Created by leduylinh96 on 4/18/2017.
 */

public class CameraDialogAdapter extends ArrayAdapter<DiaglogCameraItem> {
    private Activity mContext;
    private int mResource;
    private List<DiaglogCameraItem> mOjects;
    public CameraDialogAdapter(@NonNull Activity context, @LayoutRes int resource, @NonNull List<DiaglogCameraItem> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
        mOjects = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = mContext.getLayoutInflater().inflate(mResource, null);
        ImageView iv = (ImageView) v.findViewById(R.id.iv);
        TextView tv = (TextView) v.findViewById(R.id.tv);
        iv.setImageResource(mOjects.get(position).getmDrawable());
        tv.setText(mOjects.get(position).getmStr());
        return v;
    }
}
