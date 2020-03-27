package edu.neu.foodiefriendfinder;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Arrays;

public class LanguageMultiSelect extends DialogFragment {

    private boolean[] selectedOptions;
    private boolean[] clickedItems;
    private String[] languageOptions;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        languageOptions = getResources().getStringArray(R.array.languageOptions);

        selectedOptions = new boolean[languageOptions.length];
        clickedItems = new boolean[selectedOptions.length];

        Arrays.fill(selectedOptions, false);
        System.out.println("options array values");
        for (int i = 0; i < selectedOptions.length; i++) {
            System.out.println("boolean value index at: " + i + " " + selectedOptions[i]);
        }
        clickedItems = selectedOptions;

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
        alertBuilder.setCancelable(false);
        alertBuilder.setMultiChoiceItems(languageOptions, selectedOptions, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                clickedItems[which] = isChecked;
                Toast.makeText(getActivity(), languageOptions[which], Toast.LENGTH_SHORT).show();
            }
        });

        alertBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedOptions = clickedItems;
            }
        });

        alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertBuilder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for (int i = 0; i < selectedOptions.length; i++) {
                    selectedOptions[i] = false;

                }
            }
        })

        AlertDialog alertDialog = alertBuilder.create();
        return alertDialog;
    }
}
