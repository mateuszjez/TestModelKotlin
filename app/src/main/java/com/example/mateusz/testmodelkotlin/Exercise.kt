package com.example.mateusz.testmodelkotlin

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

/**
 * Created by Mateusz on 3/1/2018.
 */
class Exercise(sqLiteDatabase: SQLiteDatabase,sqlEXERCISES: String,cursor: Cursor) {
    val sqldb: SQLiteDatabase = sqLiteDatabase
    val sqlTABLE: String = sqlEXERCISES
    var type: String = cursor.getString(cursor.getColumnIndex("TYPE"))
    var name: String = cursor.getString(cursor.getColumnIndex("NAME"));
    var reps: Int = cursor.getInt(cursor.getColumnIndex("REPS")); //indicates how many repetitions was planned for particular exercise
    var reptime: Int = cursor.getInt(cursor.getColumnIndex("REPTIME")); //time of one repetition for static exercises (like plank)
    var breaktime: Int = cursor.getInt(cursor.getColumnIndex("BREAKTIME")); //time for short recovery between repetitions
    var duration: Int = cursor.getInt(cursor.getColumnIndex("DURATION")); //indicates elapsed time
    var remained: Int = cursor.getInt(cursor.getColumnIndex("REMAINED")); //indicates how much of time or how many repetitions remained to finish the exercise
    var is_finished = false
    fun run() {
        if(type=="recovery"){
        }
        else if (type=="exercise"){
        }
        else if (type=="running"){
        }
    }
    fun to_Progress():Progress{
        var omited = 0;
        if(is_finished) omited = remained
        return Progress(name,reps-remained,reps,remained,"reps")
    }
}