package com.example.mateusz.testmodelkotlin

import android.content.ServiceConnection
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.ComponentName
import android.content.Context
import android.os.IBinder
import com.example.mateusz.testmodelkotlin.TrainingService.TrainingServiceLocalBinder
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.AdapterView




class BrowseTrainingsActivity : AppCompatActivity() {
    private val sqlTableName: String = "TRAININGPROGRAMS"
    lateinit var sqLiteDatabaseName:String
    lateinit var sqLiteDatabase:SQLiteDatabase
    var trainingService: TrainingService = TrainingService()
    var trainingNames: MutableList<String> = mutableListOf()
    var trainingPrograms: MutableList<TrainingProgramSimple> = mutableListOf()
    var isBound: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_browse_trainings)
        sqLiteDatabaseName = resources.getString(R.string.sqlite_db_name)//nazwa bazy danych w resources
        sqLiteDatabase = openOrCreateDatabase(sqLiteDatabaseName, MODE_PRIVATE,null)
        val i = Intent(this, TrainingService::class.java)
        bindService(i,trainingServiceConnection, Context.BIND_AUTO_CREATE)

        trainingNames = trainingService.takeListOfNames(sqLiteDatabase,sqlTableName)
        trainingPrograms = trainingService.takeListOfTrainingPrograms(sqLiteDatabase,sqlTableName)
        val myList        = findViewById<View>(R.id.listTrainings) as ListView
        val myAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, trainingNames)
        myList.adapter = myAdapter
        myList.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val intent = Intent(this@BrowseTrainingsActivity, BrowseDaysActivity::class.java)
            val nr:Int = parent.getPositionForView(view) //listTrainings.selectedItemPosition
            intent.putExtra("sqlTableName",sqlTableName + nr.toString() + "TRAININGDAYS")
            intent.putExtra("sqlTrainingProgramNR",trainingPrograms[position].sqlTrainingProgramNR)
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
            val binder = service as TrainingServiceLocalBinder //referencja do klasy TrainingServiceLocalBinder (inner class of TrainingService)
            trainingService = binder.getService() //uzyskanie dostępu do TrainingService (uzyskanie odpowiedniej referencji do tej klasy)
            isBound = true
        }

        override fun onServiceDisconnected(name: ComponentName) {
            isBound = false
        }
    }
}
