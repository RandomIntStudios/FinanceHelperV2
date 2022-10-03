package com.ristudios.financehelperv2.data.monthlypayments;

import android.content.Context;

import com.ristudios.financehelperv2.data.database.DatabaseExecutor;
import com.ristudios.financehelperv2.data.database.ItemDatabaseHelper;
import com.ristudios.financehelperv2.ui.activities.MonthlyPaymentActivity;

import java.util.ArrayList;
import java.util.List;

public class MonthlyPaymentManager {

    private ArrayList<MonthlyPayment> payments;
    private DatabaseExecutor databaseExecutor;
    private MonthlyPaymentManagerListener listener;

    public MonthlyPaymentManager (Context context, MonthlyPaymentManagerListener listener){
        this.payments = new ArrayList<>();
        databaseExecutor = new DatabaseExecutor(new ItemDatabaseHelper(context));
        this.listener = listener;
    }

    public void addPayment(MonthlyPayment payment){
        payments.add(payment);
        databaseExecutor.databaseMonthlyPaymentsAdd(payment);
        listener.onListUpdated();
    }

    public void removePayment(MonthlyPayment payment){
        MonthlyPayment match = null;
        for (MonthlyPayment monthlyPayment : payments){
            if (monthlyPayment.getUuid().equals(payment.getUuid())){
                match = monthlyPayment;
            }
        }
        if (match != null){
            payments.remove(match);
            databaseExecutor.databaseMonthlyPaymentsDelete(match);
            listener.onListUpdated();
        }
    }

    public void updatePayment(MonthlyPayment toUpdate, MonthlyPayment updated){
        int pos = payments.indexOf(toUpdate);
        removePayment(toUpdate);
        payments.add(pos, updated);
        databaseExecutor.databaseMonthlyPaymentsDelete(toUpdate);
        databaseExecutor.databaseMonthlyPaymentsAdd(updated);
        listener.onListUpdated();
    }

    public void loadAllPayments()
    {
        databaseExecutor.databaseMonthlyPaymentsLoadAll(new DatabaseExecutor.MonthlyPaymentDataLoadListener() {
            @Override
            public void onPaymentsLoaded(List<MonthlyPayment> loadedPayments) {
                payments.addAll(loadedPayments);
                listener.onListUpdated();
            }
        });
    }

    public interface MonthlyPaymentManagerListener{
        void onListUpdated();
    }

}
