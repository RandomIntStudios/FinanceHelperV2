package com.ristudios.financehelperv2.data.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.ristudios.financehelperv2.data.monthlypayments.MonthlyPayment;

import java.util.List;

@Dao
public interface MonthlyDAO {

    @Insert
    void insertMonthlyPayment(MonthlyPayment payment);

    @Delete
    void deleteMonthlyPayment(MonthlyPayment payment);

    @Query("SELECT * FROM monthlyPayments")
    List<MonthlyPayment> getAllMonthlyPayments();

}
