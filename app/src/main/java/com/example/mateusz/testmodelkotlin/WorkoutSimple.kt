package com.example.mateusz.testmodelkotlin

import android.database.sqlite.SQLiteDatabase

class WorkoutSimple(sqLiteDatabase: SQLiteDatabase, sqlTRAININGDAYS: String, sqlWORKOUTID: Int) {
    val sqlPROGRESS: String = sqlTRAININGDAYS + sqlWORKOUTID.toString() +"PROGRESS";
    val sqlEXERCISES: String = sqlTRAININGDAYS + sqlWORKOUTID.toString() +"EXERCISES";
    var workoutid: Int = sqlWORKOUTID
    var daynr: Int = 0
    var workoutname: String
    var workoutdate: String
    var currentExercise: Int = 0;
    var is_finished: Boolean = false;
    var progress: MutableList<Progress> = mutableListOf()
    var sqlTrainingProgramNR:Int = 0
    init {
        var cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + sqlTRAININGDAYS +
                " WHERE WORKOUTID=" + sqlWORKOUTID.toString(), null)
        cursor.moveToFirst()
        daynr = cursor.getInt(cursor.getColumnIndex("DAYNR"))
        workoutname = cursor.getString(cursor.getColumnIndex("WORKOUTNAME"))
        workoutdate = cursor.getString(cursor.getColumnIndex("WORKOUTDATE"))

        cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + sqlPROGRESS, null)
        while (cursor.moveToNext()){
            progress.add(Progress(
                    cursor.getString(cursor.getColumnIndex("NAME")),
                    cursor.getInt(cursor.getColumnIndex("VALUE")),
                    cursor.getInt(cursor.getColumnIndex("PLANNED")),
                    cursor.getInt(cursor.getColumnIndex("OMITED")),
                    cursor.getString(cursor.getColumnIndex("UNIT")) ))
        }

        cursor.close()
    }
}
