package com.example.mateusz.testmodelkotlin

import android.database.sqlite.SQLiteDatabase

/**
 * Created by Mateusz on 3/1/2018.
 */
class ExerciseDescription(name_in: String, description_in: String, muscles_in: String) {
    var name: String = name_in
    var description: String = description_in
    var muscles: String = muscles_in
}