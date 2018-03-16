package com.example.mateusz.testmodelkotlin

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.database.sqlite.SQLiteDatabase
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.view.View
import android.widget.Button
import android.widget.TextView

class ExerciseActivity : AppCompatActivity() {
    private var sqlTableName: String = ""
    lateinit var sqLiteDatabaseName:String
    lateinit var sqLiteDatabase: SQLiteDatabase
    var trainingService: TrainingService = TrainingService()
    var isBound: Boolean = false
    var progress: MutableList<Progress> = mutableListOf()
    var sqlTrainingProgramNR:Int = 0

    lateinit var exercise:Exercise
    var exercisenr: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise)
        val bundle = intent.extras
        sqlTableName = bundle.getString("sqlTableName")
        sqlTrainingProgramNR = bundle.getInt("sqlTrainingProgramNR")
        sqLiteDatabaseName = resources.getString(R.string.sqlite_db_name)//nazwa bazy danych w resources
        sqLiteDatabase = openOrCreateDatabase(sqLiteDatabaseName, MODE_PRIVATE,null)
        val i = Intent(this, TrainingService::class.java)
        bindService(i,trainingServiceConnection, Context.BIND_AUTO_CREATE)

        exercisenr = bundle.getInt("exercisenr")
        exercise = trainingService.takeOneExercise(sqLiteDatabase,sqlTableName,exercisenr)

        val textExerciseName = findViewById<View>(R.id.textExerciseName) as TextView
        val textReps = findViewById<View>(R.id.textReps) as TextView
        val textRemained = findViewById<View>(R.id.textRemained) as TextView
        textExerciseName.text   = exercise.name
        if(exercise.type=="recovery") {
            val buttonGo = findViewById<View>(R.id.buttonGo) as Button
            val buttonInfo = findViewById<View>(R.id.buttonInfo) as Button
            buttonGo.visibility = View.INVISIBLE
            buttonInfo.visibility = View.INVISIBLE
            textReps.text = exercise.reptime.toString() + " seconds" //tworzenie stringów wyrzucić poza activity
            textRemained.text = ""
        } else if(exercise.type=="exercise"){
            if(exercise.reptime==0)
                textReps.text = "Reps: " + exercise.reps.toString() + " reps" //tworzenie stringów wyrzucić poza activity
            else
                textReps.text = "Reps: " + exercise.reps.toString() + " reps, " +
                        exercise.reptime.toString() + " sec. / " +
                        exercise.breaktime.toString() + " sec."
            textRemained.text = "Remained: " + exercise.remained.toString() + " reps"
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        sqLiteDatabase.close()
        unbindService(trainingServiceConnection)
    }

    private val trainingServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder = service as TrainingService.TrainingServiceLocalBinder //referencja do klasy TrainingServiceLocalBinder (inner class of TrainingService)
            trainingService = binder.getService() //uzyskanie dostępu do TrainingService (uzyskanie odpowiedniej referencji do tej klasy)
            isBound = true
        }

        override fun onServiceDisconnected(name: ComponentName) {
            isBound = false
        }
    }

    fun onClickInfo(view: View){
        val i = Intent(this@ExerciseActivity, ExerciseInfoActivity::class.java)
        i.putExtra("exerciseName",exercise.name)
        i.putExtra("sqlTrainingProgramNR",sqlTrainingProgramNR)
        startActivity(i)
    }
}
