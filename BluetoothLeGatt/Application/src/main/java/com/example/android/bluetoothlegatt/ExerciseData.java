package com.example.android.bluetoothlegatt;

import java.util.EventObject;

/**
 * Created by romanfilippov on 04.05.15.
 */
public class ExerciseData extends EventObject {

    private Exercise[] _exercises;

    public ExerciseData( Object source, Exercise[] exercises ) {
        super( source );
        _exercises = exercises;
    }

    public ExerciseData( Object source ) {
        super( source );
    }

    public Exercise[] exercises() {
        return _exercises;
    }
}
