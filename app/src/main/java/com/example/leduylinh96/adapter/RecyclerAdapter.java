package com.example.leduylinh96.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.leduylinh96.activity.MainActivity;
import com.example.leduylinh96.MainScreen.R;
import com.example.leduylinh96.model.MyNote;
import com.example.leduylinh96.activity.ActivityShowNote;

import java.io.Serializable;
import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.NoteHolder> {
    private ArrayList<MyNote> mArrayList;
    private Activity mActivity;
    private TextView mTvItemTitle;
    private TextView mTvItemNote;
    private TextView mTvDayCreated;
    private CardView cardView;
    private LinearLayout layout;
    private ImageView mIvAlarm;

    public RecyclerAdapter(ArrayList<MyNote> mArrayList, Activity activity) {
        this.mArrayList = mArrayList;
        this.mActivity = activity;
    }

    public class NoteHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public NoteHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
            layout = (LinearLayout) itemView.findViewById(R.id.lo);
            mTvItemTitle = (TextView) itemView.findViewById(R.id.tvItemTitle);
            mTvItemNote = (TextView) itemView.findViewById(R.id.tvItemNote);
            mTvDayCreated = (TextView) itemView.findViewById(R.id.tvItemDateTime);
            mIvAlarm = (ImageView) itemView.findViewById(R.id.rv_ivAlarm);
            itemView.setOnClickListener(this);
        }

        public void setDataForFields(int position) {
            MyNote myNote = mArrayList.get(position);
            if (myNote != null) {
                String title = myNote.getTitle();
                String node = myNote.getNote();
                String dayCreated = myNote.getDayCreated();
                int color = myNote.getColor();
                int alarm = myNote.getAlarm();
                if (alarm == 0) mIvAlarm.setVisibility(View.GONE);
                if (title != null && node != null) {
                    mTvItemNote.setText(node);
                    mTvItemTitle.setText(title);
                    mTvDayCreated.setText(dayCreated);
                    layout.setBackgroundColor(color);
                    cardView.setCardBackgroundColor(color);
                }
            }
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mActivity, ActivityShowNote.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("ARRAYLIST", (Serializable) mArrayList);
            bundle.putInt("POSITION", getAdapterPosition());
            intent.putExtra("BUNDLE", bundle);
            mActivity.startActivityForResult(intent, MainActivity.SHOWNOTE_REQUESTCODE);
        }
    }

    @Override
    public NoteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item, parent, false);
        return new NoteHolder(v);
    }

    @Override
    public void onBindViewHolder(NoteHolder holder, int position) {
        holder.setDataForFields(position);
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

}
