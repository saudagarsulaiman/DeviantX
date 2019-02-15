package com.cryptowallet.deviantx.UI.RoomDatabase.ModelsRoomDB;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "exc_orders_table")
public class ExcOrdersDB {

    @PrimaryKey
    @NonNull
    public int id;

    public String excOrders;

    public ExcOrdersDB(int id, String excOrders) {
        this.id = id;
        this.excOrders = excOrders;
    }


}
