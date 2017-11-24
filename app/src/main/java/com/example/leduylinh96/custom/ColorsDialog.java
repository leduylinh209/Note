package com.example.leduylinh96.custom;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;

import com.example.leduylinh96.adapter.ColorsAdapter;
import com.example.leduylinh96.MainScreen.R;

import java.util.ArrayList;

/**
 * Created by leduylinh96 on 3/17/2017.
 */

public class ColorsDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.dialog_screen, null);
        ArrayList<Integer> arrayList = new ArrayList<>();
        arrayList.add(0);
        arrayList.add(1);
        arrayList.add(2);
        arrayList.add(3);
        ColorsAdapter colorsAdapter = new ColorsAdapter(getActivity(), R.layout.dialog_item, arrayList);
        GridView gv = (GridView) view.findViewById(R.id.gv_colors);
        gv.setAdapter(colorsAdapter);
        builder.setTitle("Choose Color").setView(view);
        return builder.create();
    }
}
