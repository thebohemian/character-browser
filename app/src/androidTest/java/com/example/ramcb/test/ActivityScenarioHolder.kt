package com.example.ramcb.test

import android.app.Activity
import android.content.Intent
import androidx.test.core.app.ActivityScenario
import io.cucumber.java.After

class ActivityScenarioHolder {

    var scenario: ActivityScenario<*>? = null

    fun launch(intent: Intent) {
        scenario = ActivityScenario.launch<Activity>(intent)
    }

    /**
     *  Close activity after scenario
     */
    @After
    fun close() {
        scenario?.close()
    }
}