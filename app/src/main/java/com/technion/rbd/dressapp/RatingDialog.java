package com.technion.rbd.dressapp;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

public class RatingDialog extends AppCompatDialogFragment {

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        // R.layout.my_layout - that's the layout where your textview is placed
//        View view = inflater.inflate(R.layout.activity_my_item_detail, container, false);
//        deliveryStatus = view.findViewById(R.id.myItemDetail_status);
//        // you can use your textview.
//        return view;
//    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        Bundle bundle = getArguments();

        String cat = bundle.getString("cat");
        View view = inflater.inflate(R.layout.rating, null);


        builder.setView(view).setTitle("Rate")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialoginterface, int i) {

                    }
                })
                .setPositiveButton("rate", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialoginterface, int i) {
                        //Toast.makeText(getContext(), "Searching...", Toast.LENGTH_LONG).show();
                        //deliveryStatus.setText("Pending");
                    }
                });

        return builder.create();
    }
}
