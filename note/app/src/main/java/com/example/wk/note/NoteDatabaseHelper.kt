package com.example.wk.note

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import org.jetbrains.anko.db.*

/**
 * Created by wk on 2018/6/12.
 */
class NoteDatabaseHelper(context: Context) : ManagedSQLiteOpenHelper(context, "NoteDatabase", null) {
    companion object {
        private var instance: NoteDatabaseHelper? = null
        @Synchronized
        fun getInstance(context: Context): NoteDatabaseHelper {
            if (instance == null) {
                instance = NoteDatabaseHelper(context.applicationContext)
            }
            return instance!!
        }
    }

    //    创建数据库表
    override fun onCreate(db: SQLiteDatabase?) {
        db!!.createTable("Note", true,
//                主键自增
                "_id" to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
                "title" to TEXT,
                "text" to TEXT,
                "time" to TEXT
        )
        Log.d("onCreate", "创建数据库表")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}

//上下文访问属性
val Context.NoteDatabase: NoteDatabaseHelper
    get() = NoteDatabaseHelper.getInstance(applicationContext)