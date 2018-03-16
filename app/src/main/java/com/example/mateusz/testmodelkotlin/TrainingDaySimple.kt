package com.example.mateusz.testmodelkotlin

class TrainingDaySimple {
    var currentWorkout: Int = 0;    //or currentWorkoutID
    var is_finished: Boolean = false;
    var progress: MutableList<Progress> = mutableListOf()
    var workouts: MutableList<WorkoutSimple> = mutableListOf()
    var date: String = ""
    var sqlTrainingProgramNR:Int = 0
    fun add(workout: WorkoutSimple) {
        for (i in 0..(workouts.size-1))
            if(workout.workoutid == workouts[i].workoutid)
                return
        if(workouts.size==0) {
            date = workout.workoutdate
            sqlTrainingProgramNR = workout.sqlTrainingProgramNR
        }
        workouts.add(workout)
        progress = Progress().add(progress,workout.progress)
    }
}
