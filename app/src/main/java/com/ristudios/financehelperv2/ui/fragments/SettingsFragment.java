package com.ristudios.financehelperv2.ui.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.ristudios.financehelperv2.R;
import com.ristudios.financehelperv2.data.Item;
import com.ristudios.financehelperv2.data.database.DatabaseExecutor;
import com.ristudios.financehelperv2.data.database.ItemDatabaseHelper;
import com.ristudios.financehelperv2.utils.Utils;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SettingsFragment extends PreferenceFragmentCompat {

    private Preference prefResetBudget, prefSelectDesign, prefDeleteData, prefSelectLanguage;
    private EditTextPreference edtPrefMaxBudget;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.settings, rootKey);
            bindPrefs();
            prefSelectDesign.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    DesignDialog dialog = new DesignDialog();
                    dialog.show(getActivity().getSupportFragmentManager(), "settings:choosedesign");
                    return false;
                }
            });
            prefSelectLanguage.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {

                    return false;
                }
            });
            edtPrefMaxBudget.setOnBindEditTextListener(new EditTextPreference.OnBindEditTextListener() {
                @Override
                public void onBindEditText(@NonNull @NotNull EditText editText) {
                    editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                }
            });
            prefResetBudget.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle(getString(R.string.dialog_reset_budget_title))
                            .setPositiveButton(R.string.dialog_reset_budget_yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                                    prefs.edit().putFloat(Utils.PREFS_CURRENT_BUDGET_KEY, Float.parseFloat(prefs.getString(Utils.PREFS_MAXIMUM_BUDGET_KEY, "200"))).apply();
                                }
                            })
                            .setNegativeButton(R.string.dialog_reset_budget_no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    return false;
                }
            });
            prefDeleteData.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle(getString(R.string.dialog_delete_data_title))
                            .setPositiveButton(R.string.dialog_delete_data_yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    DatabaseExecutor executor = new DatabaseExecutor(new ItemDatabaseHelper(getActivity()));
                                    executor.databaseLoad(new DatabaseExecutor.DataLoadListener() {
                                        @Override
                                        public void onDataLoaded(List<Item> loadedItems) {
                                            for (Item item : loadedItems) {
                                                executor.databaseDelete(item);
                                            }
                                            getActivity().runOnUiThread(()->{
                                                Toast.makeText(getContext(), getString(R.string.toast_delete_complete), Toast.LENGTH_SHORT).show();
                                            });

                                        }
                                    });

                                }
                            })
                            .setNegativeButton(R.string.dialog_delete_data_no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    return false;
                }
            });
    }

    private void bindPrefs() {
        prefResetBudget = findPreference(Utils.PREF_KEY_RESET_BUDGET);
        prefDeleteData = findPreference(Utils.PREF_KEY_DELETE_DATA);
        prefSelectDesign = findPreference(Utils.PREF_KEY_CHANGE_DESIGN);
        edtPrefMaxBudget = findPreference(Utils.PREFS_MAXIMUM_BUDGET_KEY);
        prefSelectLanguage = findPreference(Utils.PREF_KEY_LANNGUAGE);
    }
}
