package com.example.mateusz.testmodelkotlin

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log

/**
 * Created by Mateusz on 3/7/2018.
 */
class CreateSampleDatabase {
    fun clearAndCreateTRAININGPROGRAMS(sqLiteDatabase: SQLiteDatabase){
        clearDatabase(sqLiteDatabase)
        createTRAININGPROGRAMS(sqLiteDatabase)
    }
    fun clearDatabase(sqLiteDatabase: SQLiteDatabase){
        val c = sqLiteDatabase.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null)
        c.moveToFirst()
        while(c.moveToNext())
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + c.getString(0) + ";")
        c.close()
    }
    fun createTRAININGPROGRAMS(sqLiteDatabase: SQLiteDatabase) {
        val sqlTRAININGPROGRAMS     = "TRAININGPROGRAMS"
        //creating database
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS $sqlTRAININGPROGRAMS (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "NAME TINYTEXT NOT NULL," +
                "DESCRIPTION TEXT NOT NULL," +
                "TARGETGENDER TINYTEXT NOT NULL," +
                "GOALS TEXT NOT NULL,"  +
                "NR INT NOT NULL" + ")")
        for(i in 0..2){
            val sqlAUTHORS: String              = sqlTRAININGPROGRAMS+i.toString() +"AUTHORS";
            val sqlEXERCISEDESCRIPTION: String  = sqlTRAININGPROGRAMS+i.toString() +"EXERCISEDESCRIPTION";
            val sqlTRAININGDAYS: String         = sqlTRAININGPROGRAMS+i.toString() +"TRAININGDAYS";
            val sqlPROGRESS: String             = sqlTRAININGPROGRAMS+i.toString() +"PROGRESS";
            val  name: String           = "Name of training program " + i.toString()
            val  description: String    = "Very long description of training program " + i.toString()
            val  targetGender: String   = "Male or Female " + i.toString()
            val  goals: String          = "Test " + i.toString()
            //sending to database
            sqLiteDatabase.execSQL("INSERT INTO $sqlTRAININGPROGRAMS"+
                    " (NAME, DESCRIPTION, TARGETGENDER, GOALS, NR) VALUES (" +
                    "'" + name + "'," +
                    "'" + description + "'," +
                    "'" + targetGender + "'," +//"'2016-01-09'"
                    "'" + goals + "'," +
                    "" + i + ")")

            createAUTHORS(sqLiteDatabase,sqlAUTHORS)
            createEXERCISEDESCRIPTION(sqLiteDatabase,sqlEXERCISEDESCRIPTION)
            createTRAININGDAYS(sqLiteDatabase,sqlTRAININGDAYS)
            createPROGRESS(sqLiteDatabase,sqlPROGRESS)
            //fillProgress(sqLiteDatabase, sqlTRAININGPROGRAMS)
        }
    }
    private fun createAUTHORS(sqLiteDatabase: SQLiteDatabase, sqlTable: String) {
        val sqlAUTHORS: String      = sqlTable
        //creating database
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS $sqlAUTHORS (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "NAME TINYTEXT NOT NULL," +
                "CONTACT TINYTEXT NOT NULL)")
            //sending to database
        sqLiteDatabase.execSQL("INSERT INTO $sqlAUTHORS"+
                " (NAME, CONTACT) VALUES (" +
                "'Artur Dębowski','Kontakt do Artura')")
        sqLiteDatabase.execSQL("INSERT INTO $sqlAUTHORS"+
                " (NAME, CONTACT) VALUES (" +
                "'Mateusz Jeż','mateuszjez@gmail.com')")
        sqLiteDatabase.execSQL("INSERT INTO $sqlAUTHORS"+
                " (NAME, CONTACT) VALUES (" +
                "'Zuzanna Kulej','Kontakt do Zuzy')")
        sqLiteDatabase.execSQL("INSERT INTO $sqlAUTHORS"+
                " (NAME, CONTACT) VALUES (" +
                "'Bartosz Zaczyński','Kontakt do Bartka')")
    }
    private fun createEXERCISEDESCRIPTION(sqLiteDatabase: SQLiteDatabase, sqlTable: String) {
        val sqlEXERCISEDESCRIPTION: String      = sqlTable
        //creating database
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS $sqlEXERCISEDESCRIPTION (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "NAME TINYTEXT NOT NULL," +
                "DESCRIPTION TEXT NOT NULL,"+
                "MUSCLES TINYTEXT NOT NULL)")
        for(i in 0..5){
            val  name: String       = exerciseName(i)
            val  description: String= exerciseDescription(name)
            val  muscles: String    = exerciseMuscles(name)
            //sending to database
            sqLiteDatabase.execSQL("INSERT INTO $sqlEXERCISEDESCRIPTION"+
                    " (NAME, DESCRIPTION, MUSCLES) VALUES (" +
                    "'" + name + "'," +
                    "'" + description + "'," +
                    "'" + muscles + "')")
        }
    }
    private fun createPROGRESS(sqLiteDatabase: SQLiteDatabase, sqlTable: String) {
        val sqlPROGRESS: String      = sqlTable
        //creating database
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS $sqlPROGRESS (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "NAME TINYTEXT NOT NULL," +
                "VALUE INT NOT NULL," +
                "PLANNED INT NOT NULL," +
                "OMITED INT NOT NULL," +
                "UNIT CHAR (20) NOT NULL" + ")")
        for(i in 0..4){
            val  name: String    = exerciseName(i)
            val  value: Int      = 0
            val  planned: Int     = 0
            val  omited: Int     = 0
            val  unit: String    = exerciseUnit(name)
            //sending to database
            sqLiteDatabase.execSQL("INSERT INTO $sqlPROGRESS"+
                    " (NAME, VALUE, PLANNED, OMITED, UNIT) VALUES (" +
                    "'" + name + "'," +
                    "" + value + "," +
                    "" + planned + "," +
                    "" + omited + "," +
                    "'" + unit + "')")
        }
    }
    private fun fillProgress(sqLiteDatabase: SQLiteDatabase, sqlTableName: String):MutableList<Progress>{
        val cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + sqlTableName , null)
        var progress_out:MutableList<Progress> = mutableListOf()
        if(sqlTableName=="TRAININGPROGRAMS"){
            while(cursor.moveToNext()){
                val progressTRAININGPROGRAMS: MutableList<Progress> =
                        fillProgress(sqLiteDatabase,sqlTableName +
                                cursor.getInt(cursor.getColumnIndex("NR")) +
                                "TRAININGDAYS")
                progress_out = Progress().add(progress_out,progressTRAININGPROGRAMS)
                val sqlTableProgressName = sqlTableName+cursor.getInt(cursor.getColumnIndex("NR"))+"PROGRESS"
                for(ix in 0..(progressTRAININGPROGRAMS.size-1)){
                    sqLiteDatabase.execSQL("UPDATE $sqlTableProgressName SET"+
                            " VALUE = " + progressTRAININGPROGRAMS[ix].value.toString() +
                            " WHERE NAME = '" + progressTRAININGPROGRAMS[ix].name + "'")
                    sqLiteDatabase.execSQL("UPDATE $sqlTableProgressName SET"+
                            " PLANNED = " + progressTRAININGPROGRAMS[ix].planned.toString() +
                            " WHERE NAME = '" + progressTRAININGPROGRAMS[ix].name + "'")
                    sqLiteDatabase.execSQL("UPDATE $sqlTableProgressName SET"+
                            " OMITED = " + progressTRAININGPROGRAMS[ix].omited.toString() +
                            " WHERE NAME = '" + progressTRAININGPROGRAMS[ix].name + "'")
                }
            }
        }
        else{
            val cursorWorkout = sqLiteDatabase.rawQuery("SELECT * FROM " + sqlTableName , null)
            while (cursorWorkout.moveToNext()){
                val sqlTableExerciesName = sqlTableName+cursor.getInt(cursor.getColumnIndex("WORKOUTID"))+"EXERCISES"
                val progressWorkouts: MutableList<Progress> = readWorkoutProgress(sqLiteDatabase,sqlTableExerciesName)
                val sqlTableProgressName = sqlTableName+cursor.getInt(cursor.getColumnIndex("WORKOUTID"))+"PROGRESS"
                progress_out = Progress().add(progress_out,progressWorkouts)
                for(ix in 0..(progressWorkouts.size-1)){
                    sqLiteDatabase.execSQL("UPDATE $sqlTableProgressName SET"+
                            " VALUE = " + progressWorkouts[ix].value.toString() +
                            " WHERE NAME = '" + progressWorkouts[ix].name + "'")
                    sqLiteDatabase.execSQL("UPDATE $sqlTableProgressName SET"+
                            " PLANNED = " + progressWorkouts[ix].planned.toString() +
                            " WHERE NAME = '" + progressWorkouts[ix].name + "'")
                    sqLiteDatabase.execSQL("UPDATE $sqlTableProgressName SET"+
                            " OMITED = " + progressWorkouts[ix].omited.toString() +
                            " WHERE NAME = '" + progressWorkouts[ix].name + "'")
                }
            }
            cursorWorkout.close()
        }
        cursor.close()
        return progress_out
    }
    private fun createTRAININGDAYS(sqLiteDatabase: SQLiteDatabase, sqlTable: String) {
        val sqlTRAININGDAYS: String      = sqlTable
        //creating database
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS $sqlTRAININGDAYS (" +
                "WORKOUTID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "DAYNR INT NOT NULL," +
                "WORKOUTNAME TINYTEXT NOT NULL," +
                "WORKOUTDATE TINYTEXT NOT NULL" + ")")
        val workoutdate = MyDate(2018,2, 12)
        for(i in 2..61){
            val  daynr: Int             = i/2 - 1
            workoutdate.addday((i-1)%2)
            val  workoutname: String    = "Workout " + (i%2+1).toString()
            //sending to database
            sqLiteDatabase.execSQL("INSERT INTO $sqlTRAININGDAYS"+
                    " (DAYNR, WORKOUTNAME, WORKOUTDATE) VALUES (" +
                    daynr.toString() + "," +
                    "'" + workoutname + "'," +
                    "'" + workoutdate.to_String() + "')")
            val cursor: Cursor = sqLiteDatabase.rawQuery("SELECT * FROM $sqlTRAININGDAYS", null);
            cursor.moveToLast()
            val workoutid: Int = cursor.getInt(cursor.getColumnIndex("WORKOUTID")) //read from created table
            cursor.close()
            createEXERCISES(sqLiteDatabase,sqlTRAININGDAYS+workoutid.toString()+"EXERCISES")
            createPROGRESS(sqLiteDatabase,sqlTRAININGDAYS+workoutid.toString()+"PROGRESS")
        }
    }
    private fun createEXERCISES(sqLiteDatabase: SQLiteDatabase, sqlTable: String) {
        val sqlEXERCISES: String      = sqlTable
        //creating database
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS $sqlEXERCISES (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "NR INTEGER NOT NULL," +
                "TYPE INT NOT NULL," +
                "NAME TINYTEXT NOT NULL," +
                "REPS INT NOT NULL," +
                "REPTIME INT NOT NULL," +
                "BREAKTIME INT NOT NULL," +
                "DURATION INT NOT NULL," +
                "REMAINED INT NOT NULL," +
                "UNIT TINYTEXT NOT NULL" + ")")
        for(i in 0..9){
            val  name: String
            if(i%2==0) name     = exerciseName(i/2)
            else name           = exerciseName(5)
            val  type: String    = exerciseType(name)
            val  reps: Int       = exerciseReps(name)
            val  reptime: Int    = exerciseRepTime(name)
            val  breaktime: Int  = exerciseBreakTime(name)
            val  duration: Int   = exerciseDuration(name)
            val  remained: Int   = exerciseRemained(reps)
            val  unit: String    = exerciseUnit(name)
            //sending to database
            sqLiteDatabase.execSQL("INSERT INTO $sqlEXERCISES"+
                    " (NR, TYPE, NAME, REPS, REPTIME, BREAKTIME, DURATION, REMAINED, UNIT) VALUES (" +
                    "" + i + "," +
                    "'" + type + "'," +
                    "'" + name + "'," +
                    "" + reps + "," +
                    "" + reptime + "," +
                    "" + breaktime + "," +
                    "" + duration + "," +
                    "" + remained + "," +
                    "'" + unit + "')")
        }
    }
    fun checkDate(Y:Int, M:Int, D:Int, N:Int){
        Log.d("CHD","CreateSampleDatabase - fun checkDate")
        val sample_date: MyDate = MyDate(2018,2,25)
        for(i in 1..N){
            sample_date.addday(1)
            Log.d("CHD","sample_date.addday(1) = "+ sample_date.to_String())
        }
    }
    private class MyDate(Y:Int, M:Int, D:Int){//temporary class
        var y:Int = Y
        var m:Int = M
        var d:Int = D
        fun addday(days_to_add:Int){
            var d_to_add = days_to_add
            d += d_to_add
            if (d>31 && (m==1||m==3||m==5||m==7||m==8||m==10||m==12)){
                if(m<12) m += 1
                else {
                    m = 1
                    y += 1
                }
                d_to_add = d - 31
            } else if(!(m==1||m==3||m==5||m==7||m==8||m==10||m==12)){
                if(d > 30 && m != 2){
                    d_to_add = d - 30
                } else if (d > 29 && m == 2 && (y % 4 == 0)) {
                    d_to_add = d - 29
                } else if (d > 28 && m == 2 && (y % 4 != 0)){
                    d_to_add = d - 28
                } else return
                m += 1
            } else return
            d = 0
            addday(d_to_add)
        }
        fun to_String():String{
            var mstr:String = ""
            if(m<10) mstr = "0"
            mstr += m.toString()
            var dstr:String = ""
            if(d<10) dstr = "0"
            dstr += d.toString()
            return (y.toString() + "-" + mstr + "-" + d.toString())
        }
    }
    fun exerciseName(i: Int):String{
        val check = i%6
        if(check==0) return "Planks"
        if(check==1) return "Push-ups"
        if(check==2) return "Squats"
        if(check==3) return "Bird-dogs"
        if(check==4) return "Lying-hip-raises"
        return "Recovery"
    }
    fun exerciseDescription(name:String):String{
        if(name=="Planks")   return "The plank is one of the greatest and most underrated exercises ever. It’s practically a one-move static exercise that will help you build a core of steel, ripped abs and strong shoulders. Just get into push-up position on the floor, bend your elbows 90 degrees and prop yourself on the elbows, forearms and forefeet, forming a straight line from head to feet, then hold it for as long as you can without moving your waist or butt."
        if(name=="Push-ups")   return "The push-up is the ultimate bodyweight exercise that utilizes literally every major muscle in your body, thereby helping you firm your whole body. Get into a plank position, placing your hands directly under the shoulders and push your whole body up, maintaining a straight line with the legs, back and butt. Lower your body down on the same way and repeat."
        if(name=="Squats")   return "Squats will help you build your quads, hams and calves, while also strengthening your whole core and enhancing greater overall fat burning. For the standard squat, your feet should be shoulder-width apart or slightly wider. Extend your hands out in front of you and sit back and down, keeping your head facing forward. Make sure that your back doesn’t round. Keep lowering yourself until your thighs are parallel to the floor (if possible). Press back up through your heels."
        if(name=="Bird-dogs")   return "From a plank position, prop yourself on your knees and hands and simultaneously stretch one leg and the opposite arm, maintaining both perfectly straight. Hold for a moment, then lower them down and repeat with the other leg and arm. This exercise increases core strength in both abs and lower back."
        if(name=="Lying-hip-raises")   return "The lying hip raise is the perfect bodyweight exercise for building powerful glutes and hamstrings while also strengthening your abs, back and thighs. Lie on your back on the floor with bent knees and flat feet. Extend your arms out to your sides at a 45-degree angle. Squeeze your glutes and lift your hips toward the ceiling, making sure to tilt your pelvis. Lift them up as high as possible, squeezing your glutes. Slowly lower yourself down and repeat."
        return ""
    }
    fun exerciseMuscles(name:String):String{
        if(name=="Planks")   return "abs, core, shoulders"
        if(name=="Push-ups")   return "pectoral, triceps, biceps, deltoids, rhomboids, trapezius, Latissimusdorsi"
        if(name=="Squats")   return "glutes, quadriceps, hamstrings, thighs, calves"
        if(name=="Bird-dogs")   return "erectorspinae, latissimusdorsi, rectusabdominis, transverseabdominus, gluteusmaximus, trapezius, supraspinatus, infraspinatus, subscapularis, teresminor, anteriorandmedialdeltoids, posteriordeltoids, serratusanterior, gluteusmedius/minimus"
        if(name=="Lying-hip-raises")   return "gluteal, hamstrings, abs, obliques, lats"
        return ""
    }
    fun exerciseType(name: String):String{
        if(name=="Recovery") return "recovery"
        return "exercise"
    }
    fun exerciseReps(name: String):Int{
        if(name=="Planks")   return 10
        if(name=="Push-ups")   return 10
        if(name=="Squats")   return 20
        if(name=="Bird-dogs")   return 20
        if(name=="Lying-hip-raises")   return 20
        if(name=="Recovery")   return 1
        return 1
    }
    fun exerciseRepTime(name: String):Int{
        if(name=="Planks")   return 60
        if(name=="Push-ups")   return 0
        if(name=="Squats")   return 0
        if(name=="Bird-dogs")   return 10
        if(name=="Lying-hip-raises")   return 30
        if(name=="Recovery")   return 120
        return 0
    }
    fun exerciseBreakTime(name: String):Int{
        if(name=="Planks")   return 30
        if(name=="Push-ups")   return 0
        if(name=="Squats")   return 0
        if(name=="Bird-dogs")   return 10
        if(name=="Lying-hip-raises")   return 5
        if(name=="Recovery")   return 0
        return 0
    }
    fun exerciseDuration(name: String):Int{
        return 0
    }
    fun exerciseRemained(reps: Int):Int{
        return reps;
    }
    fun exerciseUnit(name: String):String{
        return "reps"
    }
    fun readWorkoutProgress(sqLiteDatabase: SQLiteDatabase,sqlTableName: String):MutableList<Progress>{
        var workoutProgress: MutableList<Progress> = mutableListOf()
        val cursor = sqLiteDatabase.rawQuery("SELECT * FROM $sqlTableName",null)
        while (cursor.moveToNext()){
            val exercise = Exercise(sqLiteDatabase,sqlTableName,cursor)
            workoutProgress = Progress().add(workoutProgress,exercise.to_Progress())
        }
        cursor.close()
        return workoutProgress
    }
}
