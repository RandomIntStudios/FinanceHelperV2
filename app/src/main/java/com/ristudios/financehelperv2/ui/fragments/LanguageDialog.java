package com.ristudios.financehelperv2.ui.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.DialogFragment;
import androidx.preference.PreferenceManager;

import com.ristudios.financehelperv2.R;
import com.ristudios.financehelperv2.utils.Utils;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class LanguageDialog extends DialogFragment {

    private RadioButton rbnEn, rbnDe;

    @Override
    public void onResume() {
        super.onResume();
        rbnDe = getDialog().findViewById(R.id.rbn_language_de);
        rbnEn = getDialog().findViewById(R.id.rbn_language_en);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String lang = prefs.getString(Utils.PREF_KEY_LANNGUAGE, "");
        if (lang.equals("en")){
            rbnEn.setChecked(true);
        }
        else if (lang.equals("de")){
            rbnDe.setChecked(true);
        }


    }

    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        return builder.setView(inflater.inflate(R.layout.dialog_change_language, null))
                .setPositiveButton(getString(R.string.save), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                        if (rbnDe.isChecked()){
                            prefs.edit().putString(Utils.PREF_KEY_LANNGUAGE, "de").apply();
                            getActivity().getResources().getConfiguration().setLocale(Locale.GERMANY);
                        }
                        else if (rbnEn.isChecked()){
                            prefs.edit().putString(Utils.PREF_KEY_LANNGUAGE, "en").apply();
                            getActivity().getResources().getConfiguration().setLocale(Locale.US);
                        }
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
