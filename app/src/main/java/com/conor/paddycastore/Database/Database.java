package com.conor.paddycastore.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.conor.paddycastore.Model.Order;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteAssetHelper {

    private static final String DB_NAME="ElectronicStore.db";
    private static final int DB_VER = 1;

    public Database(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    public List<Order> getCart(){
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        // Added to try and get the type of drink to display in the cart view
        String[] sqlSelect = {"ProductName", "Quantity", "Price", "Image"};
        String sqlTable = "OrderInformation";

        qb.setTables(sqlTable);
        Cursor cursor = qb.query(db, sqlSelect, null, null,null, null, null);

        final List<Order> result = new ArrayList<>();
        if(cursor.moveToFirst()){
            do{
                result.add(new Order(
                        cursor.getString(cursor.getColumnIndex("ProductName")),
                        cursor.getString(cursor.getColumnIndex("Quantity")),
                        cursor.getString(cursor.getColumnIndex("Price")),
                        cursor.getString(cursor.getColumnIndex("Image"))
                ));
            }while(cursor.moveToNext());
        }
        return result;
    }

    public void addToCart(Order order){
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("INSERT INTO OrderInformation(ProductName,Quantity,Price,Image) VALUES('%s','%s','%s','%s');",
                order.getProductName(),
                order.getQuantity(),
                order.getPrice(),
                order.getImage());
        db.execSQL(query);
    }

    public void cleanCart(){
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM OrderInformation;");
        db.execSQL(query);
    }

    public void removeFromCart(String productId) {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM OrderInformation WHERE ProductId='%s'", productId);
        db.execSQL(query);
    }

    public int getCountCart() {
        int count = 0;

        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("SELECT COUNT(*) FROM OrderInformation;");
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToFirst())
        {
            do {
                count = cursor.getInt(0);
            }while (cursor.moveToNext());
        }
        return count;
    }

//    public void updateCart(Order order) {
//        SQLiteDatabase db = getReadableDatabase();
//        String query = String.format("UPDATE OrderInformation SET Quantity='%s' WHERE ID ='%s';", order.getQuantity());
//        db.execSQL(query);
//    }

}

