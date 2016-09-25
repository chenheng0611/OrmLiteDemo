package com.xray.ormlitedemo.db;

import android.app.ActionBar;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.xray.ormlitedemo.bean.School;
import com.xray.ormlitedemo.bean.Student;

import java.sql.SQLException;

/**
 * Created by apple on 2016/5/6.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private DatabaseHelper(Context context){
        super(context,"test.db",null,5);
    }

    private static DatabaseHelper sHelper = null;

    public static synchronized DatabaseHelper getInstance(Context context){
        if(sHelper == null){
            sHelper = new DatabaseHelper(context);
        }
        return sHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource,Student.class);
            TableUtils.createTable(connectionSource, School.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i1) {
        try {
            TableUtils.dropTable(connectionSource,Student.class,true);
            TableUtils.dropTable(connectionSource,School.class,true);
            onCreate(sqLiteDatabase,connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
