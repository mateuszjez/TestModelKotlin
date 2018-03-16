package com.example.mateusz.testmodelkotlin

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import java.lang.reflect.Array.getDouble
import android.widget.EditText





class MainActivity : AppCompatActivity() {
    lateinit var _db: SQLiteDatabase;
    var trainingService: TrainingService = TrainingService()
    var isBound: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        _db = openOrCreateDatabase("testMyDatabase", MODE_PRIVATE,null)
        Log.d("DBS","DataBase opened or created")
        _db.execSQL("CREATE TABLE IF NOT EXISTS OSOBY (" + //CREATE INDEX - creates an index (search key)
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "NAZWISKO CHAR (20) NOT NULL," +
                "PESEL CHAR (9) NOT NULL," +
                "DATAUR DATE," +
                "WZROST REAL)");
        //_db.execSQL("UPDATE OSOBY SET WZROST = 57 WHERE ID = 2");*/
        val cursor = _db.rawQuery("SELECT * FROM OSOBY", null);
        SendToLog(cursor);
        val i = Intent(this, TrainingService::class.java)
        bindService(i,trainingServiceConnection, Context.BIND_AUTO_CREATE)//metoda wbudowana
        //training_programs = ProgramListLoader().loadFromDatabase(_db)


    }

    override fun onDestroy() {
        super.onDestroy()
        _db.close()
    }
    fun onClickCLEAR(view: View){
        //CreateSampleDatabase().clearDatabase(_db)
        CreateSampleDatabase().clearAndCreateTRAININGPROGRAMS(_db)
        _db.execSQL("DROP TABLE IF EXISTS OSOBY;")//DROP TABLE - deletes a table
        Log.d("DBS", "Tabela usunieta")
        _db.execSQL("CREATE TABLE IF NOT EXISTS OSOBY (" + //CREATE INDEX - creates an index (search key)
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "NAZWISKO CHAR (20) NOT NULL," +
                "PESEL CHAR (9) NOT NULL," +
                "DATAUR DATE," +
                "WZROST REAL)");
        Log.d("DBS", "Tabela utworzona")
        _db.execSQL("CREATE TABLE IF NOT EXISTS TRAININGDESCRIPTION(" + //CREATE INDEX - creates an index (search key)
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "NAME CHAR (20) NOT NULL," +
                "DESCRIPTION CHAR (1024) NOT NULL," +
                "DATAUR DATE," +
                "WZROST REAL)");
        Log.d("DBS", "Tabela utworzona")
    }
    fun onClickADD(view: View){
        val editTextNAZWISKO = findViewById<View>(R.id.editTextNAZWISKO) as EditText
        val editTextPESEL = findViewById<View>(R.id.editTextPESEL) as EditText
        val editTextDATAUR = findViewById<View>(R.id.editTextDATAUR) as EditText
        val editTextWZROST = findViewById<View>(R.id.editTextWZROST) as EditText
        _db.execSQL("INSERT INTO OSOBY (NAZWISKO, PESEL, DATAUR, WZROST) VALUES (" +//INSERT INTO - inserts new data into a database
                "'" + editTextNAZWISKO.text.toString() + "'," +
                "'" + editTextPESEL.text.toString() + "'," +
                "'" + editTextDATAUR.text.toString() + "'," +//"'2016-01-09'"
                editTextWZROST.text.toString() + ")");
        val cursor: Cursor = _db.rawQuery("SELECT * FROM OSOBY", null);
        SendToLog(cursor);
    }
    fun onClickBrowse(view: View){
        val i = Intent(this, BrowseTrainingsActivity::class.java)
        i.putExtra("DataBaseName","testMyDatabase")
        startActivity(i)//wbudowana metoda
    }
    fun onClickCheckDate(view: View){
        Log.d("CHD","MainActivity - onClickCheckDate from 25-02-2018 for 30 days")
        CreateSampleDatabase().checkDate(2018,2,25,800)
    }
    private val trainingServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            Log.d("TRS","3: MainActivity - onServiceConnected before val binder = service as TrainingServiceLocalBinder")
            val binder = service as TrainingService.TrainingServiceLocalBinder //referencja do klasy TrainingServiceLocalBinder (inner class of TrainingService)
            Log.d("TRS","4: MainActivity - onServiceConnected before trainingService = binder.getService()")
            trainingService = binder.getService() //uzyskanie dostępu do TrainingService (uzyskanie odpowiedniej referencji do tej klasy)
            Log.d("TRS","6: MainActivity - onServiceConnected after trainingService = binder.getService()")
            isBound = true
        }

        override fun onServiceDisconnected(name: ComponentName) {
            Log.d("TRS","x: MainActivity - fun onServiceDisconnected(name: ComponentName)")
            isBound = false
        }
    }
    private fun SendToLog(cursor: Cursor) {
        while (cursor.moveToNext()) {
            val id: Int = cursor.getInt(0)
            val nazwisko: String = cursor.getString(1)
            val pesel: String = cursor.getString(cursor.getColumnIndex("PESEL"))
            val dataUr: String = cursor.getString(cursor.getColumnIndex("DATAUR"))
            val wzrost: Double = cursor.getDouble(4)
            Log.d("DBS", id.toString() + "\t" + nazwisko + "\t" + pesel + "\t" + dataUr + "\t" + wzrost)
        }
    }
}
