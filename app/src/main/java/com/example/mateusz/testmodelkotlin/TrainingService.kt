package com.example.mateusz.testmodelkotlin

import android.app.Service
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Binder
import android.os.IBinder
import android.util.Log

class TrainingService : Service() {
    private val objectBinder: IBinder = TrainingServiceLocalBinder()
    private var is_loaded: Boolean = false


    inner class TrainingServiceLocalBinder : Binder() {//zwraca referencję do klasy TraininService (mając do niej dostęp możemy używać jej metod)
        fun getService(): TrainingService{
            Log.d("TRS","5: TrainingService - fun getService(): TrainingService")
            return this@TrainingService
        }
    }
    override fun onBind(intent: Intent): IBinder? {
        Log.d("TRS","2: TrainingService - fun onBind before return objectBinder")
        is_loaded = true
        return objectBinder
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("TRS","TrainingService - fun onDestroy()")
    }

    /*fun takeTrainingPrograms(sqLiteDatabase: SQLiteDatabase):MutableList<String>{
        private var trainingPrograms: MutableList<TrainingProgram> = mutableListOf()
        Log.d("TRS","1: TrainingService - fun takeTrainingPrograms(sqLiteDatabase)")
        if(trainingPrograms.size==0)
            trainingPrograms = ProgramListLoader().loadFromDatabase(sqLiteDatabase)
        val trainingNames: MutableList<String> = mutableListOf()
        for (ix in 0..(trainingPrograms.size-1))
            trainingNames.add(trainingPrograms[ix].name)
        is_loaded = true
        return trainingNames
    }*/
    fun takeListOfNames(sqLiteDatabase: SQLiteDatabase,sqlTableName: String):MutableList<String>{
        Log.d("TRS","1: TrainingService - fun takeListOfNames()")
        val listOfNames: MutableList<String> = mutableListOf()
        val cursor = sqLiteDatabase.rawQuery("SELECT NAME FROM $sqlTableName",null)
        while (cursor.moveToNext())
            listOfNames.add(cursor.getString(cursor.getColumnIndex("NAME")))
        cursor.close()
        return listOfNames
    }
    fun takeListOfTrainingPrograms(sqLiteDatabase: SQLiteDatabase,sqlTableName: String):MutableList<TrainingProgramSimple>{
        Log.d("TRS","1: TrainingService - fun takeListOfTrainingPrograms()")
        val listOfTrainingPrograms: MutableList<TrainingProgramSimple> = mutableListOf()
        val cursor = sqLiteDatabase.rawQuery("SELECT ID FROM $sqlTableName",null)
        while (cursor.moveToNext())
            listOfTrainingPrograms.add(TrainingProgramSimple(sqLiteDatabase,cursor.getInt(cursor.getColumnIndex("ID"))))
        cursor.close()
        return listOfTrainingPrograms
    }
    fun takeListOfDays(sqLiteDatabase: SQLiteDatabase,sqlTableName: String):MutableList<TrainingDaySimple>{
        val listOfDays: MutableList<TrainingDaySimple> = mutableListOf()
        val cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + sqlTableName , null)
        var maxday:Int = 0;
        val workouts: MutableList<WorkoutSimple> = mutableListOf()
        while (cursor.moveToNext()){
            workouts.add(WorkoutSimple(sqLiteDatabase,sqlTableName,cursor.getInt(cursor.getColumnIndex("WORKOUTID"))))
            if(workouts.last().daynr>maxday) maxday = workouts.last().daynr
        }
        for(i in 0..maxday)
            listOfDays.add(TrainingDaySimple())
        for (i in 0..(workouts.size-1))
            listOfDays[workouts[i].daynr].add(workouts[i])

        cursor.close()
        return listOfDays
    }
    fun takeWorkoutsInDay(sqLiteDatabase: SQLiteDatabase,sqlTableName: String,currentDay:Int):MutableList<WorkoutSimple>{
        val cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + sqlTableName + " WHERE DAYNR = " + currentDay.toString() , null)
        val listOfWorkouts:MutableList<WorkoutSimple> = mutableListOf()
        while (cursor.moveToNext()){
            listOfWorkouts.add(WorkoutSimple(sqLiteDatabase,sqlTableName,cursor.getInt(cursor.getColumnIndex("WORKOUTID"))))
        }
        cursor.close()
        return listOfWorkouts
    }
    fun takeExercises(sqLiteDatabase: SQLiteDatabase,sqlTableName: String):MutableList<Exercise>{
        val cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + sqlTableName, null)
        val listOfExercises:MutableList<Exercise> = mutableListOf()
        while (cursor.moveToNext())
            listOfExercises.add(Exercise(sqLiteDatabase,sqlTableName,cursor))
        cursor.close()
        return listOfExercises
    }
    fun takeOneExercise(sqLiteDatabase: SQLiteDatabase,sqlTableName: String,exercisenr: Int):Exercise{
        val cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + sqlTableName + " WHERE NR = " + exercisenr.toString(), null)
        cursor.moveToFirst()
        val exercise: Exercise = Exercise(sqLiteDatabase,sqlTableName,cursor)
        cursor.close()
        return exercise
    }
    fun takeExerciseInfo(sqLiteDatabase: SQLiteDatabase,sqlTableName: String,exercisename: String):ExerciseDescription{
        val cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + sqlTableName +
                "EXERCISEDESCRIPTION WHERE NAME = '" + exercisename + "'", null)
        cursor.moveToFirst()
        val description:String = cursor.getString(cursor.getColumnIndex("DESCRIPTION"))
        val muscles:String = cursor.getString(cursor.getColumnIndex("MUSCLES"))
        val exerciseDescription:ExerciseDescription = ExerciseDescription(exercisename,description,muscles)
        cursor.close()
        return exerciseDescription
    }
    fun readWorkoutProgress(sqLiteDatabase: SQLiteDatabase,sqlTableName: String):MutableList<Progress>{
        var workoutProgress: MutableList<Progress> = mutableListOf()
        return workoutProgress
    }
}
