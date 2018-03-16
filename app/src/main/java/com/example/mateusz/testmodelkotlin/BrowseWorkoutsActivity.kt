package com.example.mateusz.testmodelkotlin

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.database.sqlite.SQLiteDatabase
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView

class BrowseWorkoutsActivity : AppCompatActivity() {
    private var sqlTableName: String = ""
    lateinit var sqLiteDatabaseName:String
    lateinit var sqLiteDatabase: SQLiteDatabase
    var trainingService: TrainingService = TrainingService()
    var isBound: Boolean = false
    var progress: MutableList<Progress> = mutableListOf()
    var sqlTrainingProgramNR:Int = 0

    var is_finished: Boolean = false;

    var workouts: MutableList<WorkoutSimple> = mutableListOf()
    var currentDay: Int = 0
    var currentWorkout: Int = 0;    //or currentWorkoutID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_browse_workouts)
        val bundle = intent.extras
        sqlTableName = bundle.getString("sqlTableName")
        sqlTrainingProgramNR = bundle.getInt("sqlTrainingProgramNR")
        sqLiteDatabaseName = resources.getString(R.string.sqlite_db_name)//nazwa bazy danych w resources
        sqLiteDatabase = openOrCreateDatabase(sqLiteDatabaseName, MODE_PRIVATE,null)
        val i = Intent(this, TrainingService::class.java)
        bindService(i,trainingServiceConnection, Context.BIND_AUTO_CREATE)

        currentDay = bundle.getInt("daynr")
        workouts = trainingService.takeWorkoutsInDay(sqLiteDatabase,sqlTableName,currentDay)
        val workoutsNames: MutableList<String> = mutableListOf()
        for(ix in 0..(workouts.size-1))
            workoutsNames.add(workouts[ix].workoutname + " for day " + currentDay.toString())
        val myList = findViewById<View>(R.id.listWorkouts) as ListView
        val myAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, workoutsNames)
        myList.adapter = myAdapter

        myList.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val intent = Intent(this@BrowseWorkoutsActivity, BrowseExercisesActivity::class.java)
            val nr:Int = parent.getPositionForView(view)
            intent.putExtra("sqlTableName",sqlTableName + workouts[nr].workoutid.toString() + "EXERCISES")
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
            Log.d("TRS","3: BrowseDaysActivity - onServiceConnected before val binder = service as TrainingServiceLocalBinder")
            val binder = service as TrainingService.TrainingServiceLocalBinder //referencja do klasy TrainingServiceLocalBinder (inner class of TrainingService)
            Log.d("TRS","4: BrowseDaysActivity - onServiceConnected before trainingService = binder.getService()")
            trainingService = binder.getService() //uzyskanie dostępu do TrainingService (uzyskanie odpowiedniej referencji do tej klasy)
            Log.d("TRS","6: BrowseDaysActivity - onServiceConnected after trainingService = binder.getService()")
            isBound = true
        }

        override fun onServiceDisconnected(name: ComponentName) {
            Log.d("TRS","x: BrowseDaysActivity - fun onServiceDisconnected(name: ComponentName)")
            isBound = false
        }
    }
}
