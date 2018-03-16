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
import android.widget.TextView

class ExerciseInfoActivity : AppCompatActivity() {
    private var exerciseName: String = ""
    private var sqlTableName: String = "TRAININGPROGRAMS"
    lateinit var sqLiteDatabaseName:String
    lateinit var sqLiteDatabase: SQLiteDatabase
    var trainingService: TrainingService = TrainingService()
    var isBound: Boolean = false
    var progress: MutableList<Progress> = mutableListOf()
    var sqlTrainingProgramNR:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise_info)
        val bundle = intent.extras
        exerciseName = bundle.getString("exerciseName")
        sqlTrainingProgramNR = bundle.getInt("sqlTrainingProgramNR")
        sqlTableName = sqlTableName+sqlTrainingProgramNR.toString()
        sqLiteDatabaseName = resources.getString(R.string.sqlite_db_name)//nazwa bazy danych w resources
        sqLiteDatabase = openOrCreateDatabase(sqLiteDatabaseName, MODE_PRIVATE,null)
        val i = Intent(this, TrainingService::class.java)
        bindService(i,trainingServiceConnection, Context.BIND_AUTO_CREATE)

        val exerciseDescription = trainingService.takeExerciseInfo(sqLiteDatabase,sqlTableName,exerciseName)
        val textExerciseName    = findViewById<View>(R.id.textExerciseName) as TextView
        val textDescription     = findViewById<View>(R.id.textDescription) as TextView
        val textMuscles         = findViewById<View>(R.id.textMuscles) as TextView
        textExerciseName.text   = exerciseDescription.name
        textDescription.text    = exerciseDescription.description
        textMuscles.text        = exerciseDescription.muscles
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
}
