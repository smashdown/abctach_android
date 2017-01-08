package com.abctech.blogtalking.event;

/**
 * Created by Jongyoung on 2016. 3. 26..
 */
public class BTEventEmpty {
    // to finish all the activities except me.
    private String mCallerActivityName;

    public BTEventEmpty(String caller) {
        mCallerActivityName = caller;
    }
}
