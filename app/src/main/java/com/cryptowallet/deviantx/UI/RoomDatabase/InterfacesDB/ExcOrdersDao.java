package com.cryptowallet.deviantx.UI.RoomDatabase.InterfacesDB;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.cryptowallet.deviantx.UI.RoomDatabase.ModelsRoomDB.ExcOrdersDB;

@Dao
public interface ExcOrdersDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
        void insertExcOrders(ExcOrdersDB excOrdersDB);

    @Query("DELETE FROM exc_orders_table")
    void deleteAllExcOrders();

    @Query("SELECT * from exc_orders_table")
    ExcOrdersDB getExcOrders();

}
