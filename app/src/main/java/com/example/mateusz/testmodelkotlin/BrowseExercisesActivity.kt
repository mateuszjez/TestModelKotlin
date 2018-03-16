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
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView

class BrowseExercisesActivity : AppCompatActivity() {
    private var sqlTableName: String = ""
    lateinit var sqLiteDatabaseName:String
    lateinit var sqLiteDatabase: SQLiteDatabase
    var trainingService: TrainingService = TrainingService()
    var isBound: Boolean = false
    var progress: MutableList<Progress> = mutableListOf()
    var sqlTrainingProgramNR:Int = 0

    var is_finished: Boolean = false;

    var exercises: MutableList<Exercise> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_browse_exercises)
        val bundle = intent.extras
        sqlTableName = bundle.getString("sqlTableName")
        sqlTrainingProgramNR = bundle.getInt("sqlTrainingProgramNR")
        sqLiteDatabaseName = resources.getString(R.string.sqlite_db_name)//nazwa bazy danych w resources
        sqLiteDatabase = openOrCreateDatabase(sqLiteDatabaseName, MODE_PRIVATE,null)
        val i = Intent(this, TrainingService::class.java)
        bindService(i,trainingServiceConnection, Context.BIND_AUTO_CREATE)

        exercises = trainingService.takeExercises(sqLiteDatabase,sqlTableName)
        val exercisesNames: MutableList<String> = mutableListOf()
        for(ix in 0..(exercises.size-1))
            exercisesNames.add(exercises[ix].name)
        val myList = findViewById<View>(R.id.listExercises) as ListView
        val myAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, exercisesNames)
        myList.adapter = myAdapter
        myList.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val intent = Intent(this@BrowseExercisesActivity, ExerciseActivity::class.java)
            intent.putExtra("sqlTableName",sqlTableName)
            intent.putExtra("exercisenr",position)
            intent.putExtra("sqlTrainingProgramNR",sqlTrainingProgramNR)
            startActivity(intent)
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
}
