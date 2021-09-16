package com.ristudios.financehelperv2.ui.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.ristudios.financehelperv2.R;
import com.ristudios.financehelperv2.data.Item;
import com.ristudios.financehelperv2.utils.Utils;

import org.jetbrains.annotations.NotNull;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

public class AddOrUpdateDialog extends DialogFragment {

    public static final int MODE_NEW = 1;
    public static final int MODE_UPDATE = -1;

    private int mode;
    private Item item;
    private int position;
    private AddOrUpdateDialogListener listener;
    private TextView txtPriceHeader, txtFiller, txtCountHeader, txtCurrencySign;
    private EditText edtName, edtCount, edtPrice;
    private RadioButton rbt_spent, rbt_received;

    private void initViews() {
        edtName = getDialog().findViewById(R.id.edt_name_input_dialog);
        edtCount = getDialog().findViewById(R.id.edt_count_input_dialog);
        edtPrice = getDialog().findViewById(R.id.edt_price_input_dialog);
        txtPriceHeader = getDialog().findViewById(R.id.txt_price_header_dialog);
        txtPriceHeader.setOnClickListener(v -> {
            listener.onInfoClicked();
        });
        rbt_received = getDialog().findViewById(R.id.rbt_money_received);
        rbt_spent = getDialog().findViewById(R.id.rbt_money_spent);
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public void setData() {
        if (item != null) {
            edtName.setText(item.getName());
            edtPrice.setText(String.valueOf(item.getPrice()));
            edtCount.setText(String.valueOf(item.getCount()));
            if (item.isIncome()) {
                rbt_received.setChecked(true);
            }
            else{
                rbt_spent.setChecked(true);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initViews();
        setData();
    }


    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        AlertDialog dialog = builder.setView(inflater.inflate(R.layout.dialog_add_update_item, null))
                .setPositiveButton(getResources().getString(R.string.positive_option_add_update), null)
                .setNegativeButton(getResources().getString(R.string.negative_option_add_update), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        listener.onNegativeClicked();
                    }
                }).create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (nameInputCorrect() && countInputCorrect() && priceInputCorrect() && incomeOutcomeCheck() != 0)
                        {
                            String name = edtName.getText().toString();
                            int count = Integer.parseInt(edtCount.getText().toString());
                            float price = Float.parseFloat(edtPrice.getText().toString());
                            price = Utils.roundToTwoDecimals(price);
                            long date = Utils.getMillisForDate(Utils.getCurrentZonedTime());
                            float priceTotal = count * price;
                            priceTotal = Utils.roundToTwoDecimals(priceTotal);
                            boolean isIncome = false;
                            if (incomeOutcomeCheck() == 1){isIncome = true;}
                            Item itemNew = new Item(UUID.randomUUID().toString(), name, date, price, priceTotal, count, isIncome);
                            if (mode == MODE_NEW) {
                                listener.onItemNew(itemNew);
                                Toast.makeText(getActivity(), getResources().getString(R.string.toast_saved), Toast.LENGTH_SHORT).show();
                            }
                            else {
                                listener.onItemUpdate(item, itemNew);
                                Toast.makeText(getActivity(), getResources().getString(R.string.toast_saved), Toast.LENGTH_SHORT).show();
                            }
                            dialog.dismiss();
                        }
                        else{
                            Toast.makeText(getActivity(), getResources().getString(R.string.toast_hint_input), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });
        return dialog;
    }

    private int incomeOutcomeCheck(){
        if (rbt_received.isChecked())
        {
            return 1;
        }
        else if (rbt_spent.isChecked()){
            return -1;
        }
        return 0;
    }

    private boolean nameInputCorrect() {
        return !edtName.getText().toString().equals("");
    }

    private boolean countInputCorrect() {
        return !edtCount.getText().toString().equals("");
    }

    private boolean priceInputCorrect() {
        return !edtPrice.getText().toString().equals("");
    }

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        listener = (AddOrUpdateDialogListener) context;
    }

    public interface AddOrUpdateDialogListener {
        void onItemNew(Item newItem);

        void onItemUpdate(Item toUpdate, Item updatedItem);

        void onNegativeClicked();

        void onInfoClicked();
    }
}
