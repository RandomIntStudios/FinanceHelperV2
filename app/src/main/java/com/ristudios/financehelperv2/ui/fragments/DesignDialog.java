package com.ristudios.financehelperv2.ui.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.DialogFragment;
import androidx.preference.PreferenceManager;

import com.ristudios.financehelperv2.R;
import com.ristudios.financehelperv2.ui.activities.SettingsActivity;
import com.ristudios.financehelperv2.utils.Utils;

import org.jetbrains.annotations.NotNull;

public class DesignDialog extends DialogFragment {

    private RadioButton rbnNight, rbnDay, rbnSysDef;

    @Override
    public void onResume() {
        super.onResume();
        rbnDay = getDialog().findViewById(R.id.rbn_light_mode);
        rbnNight = getDialog().findViewById(R.id.rbn_dark_mode);
        rbnSysDef = getDialog().findViewById(R.id.rbn_follow_system);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O_MR1){
            rbnSysDef.setVisibility(View.INVISIBLE);
        }
        int stat = AppCompatDelegate.getDefaultNightMode();
        if (stat == AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM){
            rbnSysDef.setChecked(true);
        }
        else if (stat == AppCompatDelegate.MODE_NIGHT_NO){
            rbnDay.setChecked(true);
        }
        else if (stat == AppCompatDelegate.MODE_NIGHT_YES){
            rbnNight.setChecked(true);
        }
    }

    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        return builder.setView(inflater.inflate(R.layout.dialog_change_design_layout, null))
                .setPositiveButton(getString(R.string.save), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                        if (rbnDay.isChecked()){
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                            prefs.edit().putInt(Utils.PREF_KEY_DESIGN_MODE, AppCompatDelegate.getDefaultNightMode()).apply();
                        }
                        else if (rbnNight.isChecked()){
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                            prefs.edit().putInt(Utils.PREF_KEY_DESIGN_MODE, AppCompatDelegate.getDefaultNightMode()).apply();

                        }
                        else {
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                            prefs.edit().putInt(Utils.PREF_KEY_DESIGN_MODE, AppCompatDelegate.getDefaultNightMode()).apply();
                        }
                        getActivity().finish();
                        getActivity().startActivity(new Intent(getActivity(), SettingsActivity.class));
                        dismiss();
                    }
                })
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create();
    }
}
