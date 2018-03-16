package com.example.mateusz.testmodelkotlin

/**
 * Created by Mateusz on 3/1/2018.
 */
class Progress {
    constructor()
    var name: String =""
    var value: Int   =0
    var planned: Int =0
    var omited: Int  =0
    var unit: String =""
    constructor(name_in: String, value_in: Int, planned_in:Int, omited_in: Int, unit_in: String){
        name    = name_in
        value   = value_in
        planned = planned_in
        omited  = omited_in;
        unit    = unit_in;
    }
    fun add(progress: Progress){
        if(progress.name==name&&progress.unit==unit){
            value   += progress.value
            planned += progress.planned
            omited  += progress.omited
            return
        }
        throw error("ERROR! Incompatible units.")
    }
    fun add(progress: MutableList<Progress>, progress_to_add: Progress):MutableList<Progress>{
        val progressList: MutableList<Progress>  = mutableListOf()
        progressList.add(progress_to_add)
        return add(progress,progress_to_add)
    }
    fun add(progress: MutableList<Progress>,progress_to_add: MutableList<Progress>):MutableList<Progress> {
        val check_if_not_added = BooleanArray(progress_to_add.size)
        check_if_not_added.fill(true,0,check_if_not_added.size-1)

        for (i in 0..(progress.size-1))
            for (j in 0..(progress_to_add.size-1))
                if(progress[i].name==progress_to_add[j].name) {
                    progress[i].add(progress_to_add[j])
                    check_if_not_added[j] = false
                }
        for (i in 0..(progress_to_add.size-1))
            if(check_if_not_added[i])
                progress.add(progress_to_add[i])
        return progress
    }
}