package com.example.mateusz.testmodelkotlin

import android.database.sqlite.SQLiteDatabase

/**
 * Created by Mateusz on 3/13/2018.
 */
class TrainingProgramSimple constructor(sqLiteDatabase: SQLiteDatabase, sqlID: Int) {
    val sqlAUTHORS: String              = "TRAININGPROGRAMS" + sqlID.toString() +"AUTHORS";
    val sqlEXERCISEDESCRIPTION: String  = "TRAININGPROGRAMS" + sqlID.toString() +"EXERCISEDESCRIPTION";
    val sqlPROGRESS: String             = "TRAININGPROGRAMS" + sqlID.toString() +"PROGRESS";
    val sqlTRAININGDAYS: String         = "TRAININGPROGRAMS" + sqlID.toString() +"TRAININGDAYS";
    var name: String
    var description: String
    var targetGender: String
    var goals: String
    var sqlTrainingProgramNR:Int = 0
    var is_started: Boolean = false
    var is_finished: Boolean = false

    init {
        val cursor = sqLiteDatabase.rawQuery("SELECT * FROM TRAININGPROGRAMS WHERE ID=" + sqlID.toString() , null)
        cursor.moveToFirst()
        name = cursor.getString(cursor.getColumnIndex("NAME"))
        description = cursor.getString(cursor.getColumnIndex("DESCRIPTION"))
        targetGender = cursor.getString(cursor.getColumnIndex("TARGETGENDER"))
        goals = cursor.getString(cursor.getColumnIndex("GOALS"))
        sqlTrainingProgramNR = cursor.getInt(cursor.getColumnIndex("NR"))
cursor.close()
    }
}