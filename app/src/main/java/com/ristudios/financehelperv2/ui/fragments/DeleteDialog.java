package com.ristudios.financehelperv2.ui.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.ristudios.financehelperv2.data.Item;

import org.jetbrains.annotations.NotNull;

public class DeleteDialog extends DialogFragment {

    private Item selectedItem;
    private DeleteDialogListener listener;

    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        AlertDialog dialog = builder.setNegativeButton("nicht löschen", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setPositiveButton("Löschen", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                    listener.onDeleteClicked(selectedItem);
            }
        }).setTitle("Wirklich löschen?").create();

        return dialog;
    }

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        listener = (DeleteDialogListener) context;
        super.onAttach(context);
    }

    public void setItem(Item item){
        selectedItem = item;
    }

    public interface DeleteDialogListener{
        void onDeleteClicked(Item toDelete);
    }
}
