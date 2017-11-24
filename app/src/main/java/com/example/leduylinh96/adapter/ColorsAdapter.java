package com.example.leduylinh96.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.example.leduylinh96.MainScreen.R;

import java.util.List;

/**
 * Created by leduylinh96 on 3/17/2017.
 */

public class ColorsAdapter extends ArrayAdapter<Integer> {

    private Activity mContext;
    private int mResource;
    List<Integer> mObjects;

    public ColorsAdapter(@NonNull Activity context, @LayoutRes int resource, @NonNull List<Integer> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
        mObjects = objects;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = mContext.getLayoutInflater();
        View itemView = layoutInflater.inflate(mResource, null);
        Button btn = (Button) itemView.findViewById(R.id.btn_dialog_item);
        switch (position) {
            case 0:
                btn.setBackgroundColor(Color.WHITE);
                break;
            case 1:
                btn.setBackgroundColor(Color.parseColor("#ffa500"));
                break;
            case 2:
                btn.setBackgroundColor(Color.parseColor("#98fb98"));
                break;
            case 3:
                btn.setBackgroundColor(Color.parseColor("#00cccc"));
                break;
        }
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View rootView = mContext.getWindow().getDecorView().getRootView();
                switch (position) {
                    case 0:
                        rootView.setBackgroundColor(Color.WHITE);
                        break;
                    case 1:
                        rootView.setBackgroundColor(Color.parseColor("#ffa500"));
                        break;
                    case 2:
                        rootView.setBackgroundColor(Color.parseColor("#98fb98"));
                        break;
                    case 3:
                        rootView.setBackgroundColor(Color.parseColor("#00cccc"));
                        break;
                }
            }
        });
        return itemView;
    }
}
