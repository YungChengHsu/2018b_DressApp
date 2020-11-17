package com.technion.rbd.dressapp;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;

import org.florescu.android.rangeseekbar.RangeSeekBar;

public class AdvancedSearchDialog extends AppCompatDialogFragment {

    RangeSeekBar seekBarLength, seekBarWaist, seekBarBust, seekBarSize, seekBarHips;
    TextView advancedSearchLengthMin, advancedSearchLengthMax;
    TextView advancedSearchWaistMin, advancedSearchWaistMax;
    TextView advancedSearchBustMin, advancedSearchBustMax;
    TextView advancedSearchHipsMin, advancedSearchHipsMax;
    TextView advancedSearchSeekBarSizeMin, advancedSearchSeekBarSizeMax;
    Spinner advancedSearchSizeMin, advancedSearchSizeMax;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        Bundle bundle = getArguments();

        String cat = bundle.getString("cat");
        View view;

        if (cat.equals("Tops")) {
            view = inflater.inflate(R.layout.advanced_search_layout, null);

            advancedSearchSizeMin = view.findViewById(R.id.advancedSearchTopsSizeMin);
            advancedSearchSizeMax = view.findViewById(R.id.advancedSearchTopsSizeMax);
        } else if (cat.equals("Pants")) {
            view = inflater.inflate(R.layout.advanced_search_pants_layout, null);

            seekBarLength = view.findViewById(R.id.seekBarPantsLength);
            advancedSearchLengthMin = view.findViewById(R.id.advancedSearchPantsLengthMin);
            advancedSearchLengthMax = view.findViewById(R.id.advancedSearchPantsLengthMax);

            seekBarLength.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
                @Override
                public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Object minValue, Object maxValue) {
                    advancedSearchLengthMin.setText(String.valueOf((int) minValue * 2));
                    advancedSearchLengthMax.setText(String.valueOf((int) maxValue * 2));
                }
            });

            seekBarWaist = view.findViewById(R.id.seekBarPantsWaist);
            advancedSearchWaistMin = view.findViewById(R.id.advancedSearchPantsWaistMin);
            advancedSearchWaistMax = view.findViewById(R.id.advancedSearchPantsWaistMax);

            seekBarWaist.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
                @Override
                public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Object minValue, Object maxValue) {
                    advancedSearchWaistMin.setText(String.format("%.01f", (float) ((int) minValue) * 0.5));
                    advancedSearchWaistMax.setText(String.format("%.01f", (float) ((int) maxValue) * 0.5));
                }
            });
        } else if (cat.equals("Shoes")) {
            view = inflater.inflate(R.layout.advanced_search_shoes_layout, null);

            seekBarSize = view.findViewById(R.id.seekBarShoesSize);
            advancedSearchSeekBarSizeMin = view.findViewById(R.id.advancedSearchShoesSizeMin);
            advancedSearchSeekBarSizeMax = view.findViewById(R.id.advancedSearchShoesSizeMax);

            seekBarSize.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
                @Override
                public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Object minValue, Object maxValue) {
                    advancedSearchSeekBarSizeMin.setText(String.format("%.01f", (float) ((int) minValue) * 0.4));
                    advancedSearchSeekBarSizeMax.setText(String.format("%.01f", (float) ((int) maxValue) * 0.4));
                }
            });
        } else if (cat.equals("Accessories"))
            view = inflater.inflate(R.layout.advanced_search_accessories_layout, null);
        else if (cat.equals("Dress")) {
            view = inflater.inflate(R.layout.advanced_search_dress_layout, null);

            seekBarBust = view.findViewById(R.id.seekBarDressBust);
            advancedSearchBustMin = view.findViewById(R.id.advancedSearchDressBustMin);
            advancedSearchBustMax = view.findViewById(R.id.advancedSearchDressBustMax);

            seekBarBust.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
                @Override
                public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Object minValue, Object maxValue) {
                    advancedSearchBustMin.setText(String.format("%.01f", (float) ((int) minValue) * 1.1));
                    advancedSearchBustMax.setText(String.format("%.01f", (float) ((int) maxValue) * 1.1));
                }
            });

            seekBarWaist = view.findViewById(R.id.seekBarDressWaist);
            advancedSearchWaistMin = view.findViewById(R.id.advancedSearchDressWaistMin);
            advancedSearchWaistMax = view.findViewById(R.id.advancedSearchDressWaistMax);

            seekBarWaist.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
                @Override
                public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Object minValue, Object maxValue) {
                    advancedSearchWaistMin.setText(String.format("%.01f", (float) ((int) minValue) * 0.5));
                    advancedSearchWaistMax.setText(String.format("%.01f", (float) ((int) maxValue) * 0.5));
                }
            });

            seekBarHips = view.findViewById(R.id.seekBarDressHips);
            advancedSearchHipsMin = view.findViewById(R.id.advancedSearchDressHipsMin);
            advancedSearchHipsMax = view.findViewById(R.id.advancedSearchDressHipsMax);

            seekBarHips.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
                @Override
                public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Object minValue, Object maxValue) {
                    advancedSearchHipsMin.setText(String.format("%.01f", (float) ((int) minValue) * 0.5));
                    advancedSearchHipsMax.setText(String.format("%.01f", (float) ((int) maxValue) * 0.5));
                }
            });
        } else if (cat.equals("Skirts")) {
            view = inflater.inflate(R.layout.advanced_search_skirts_layout, null);

            advancedSearchSizeMin = view.findViewById(R.id.advancedSearchTopsSizeMin);
            advancedSearchSizeMax = view.findViewById(R.id.advancedSearchTopsSizeMax);
        } else view = inflater.inflate(R.layout.advanced_search_not_support_layout, null);

        if (cat.equals("Accessories") || cat.equals("Category")) {
            builder.setView(view).setTitle("Advanced Search")
                    .setNegativeButton("Back", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialoginterface, int i) {

                        }
                    });
        } else {
            builder.setView(view).setTitle("Advanced Search")
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialoginterface, int i) {

                        }
                    })
                    .setPositiveButton("Search", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialoginterface, int i) {
                            Toast.makeText(getContext(), "Searching...", Toast.LENGTH_LONG).show();
                        }
                    });
        }

        return builder.create();
    }
}
