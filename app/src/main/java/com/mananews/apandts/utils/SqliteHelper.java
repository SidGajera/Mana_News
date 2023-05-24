package com.mananews.apandts.utils;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class SqliteHelper extends SQLiteAssetHelper {

    private static final String DB_NAME = "database.sqlite";
    private static final int DB_VER = 1;

    public SqliteHelper(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }
}
