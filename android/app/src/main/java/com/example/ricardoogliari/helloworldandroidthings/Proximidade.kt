package com.example.ricardoogliari.helloworldandroidthings

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.google.android.things.pio.Gpio
import com.google.android.things.pio.GpioCallback
import com.google.android.things.pio.PeripheralManager


class Proximidade : Activity(), GpioCallback {

    val ECHO_PIN_NAME = "BCM20"
    val TRIGGER_PIN_NAME = "BCM21"

    lateinit var mEcho: Gpio
    lateinit var mTrigger: Gpio

    var time1: Long = 0
    var time2: Long = 0

    lateinit var ultrasonicTriggerHandler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var service = PeripheralManager.getInstance()

        mEcho = service.openGpio(ECHO_PIN_NAME)
        mEcho.setDirection(Gpio.DIRECTION_IN)
        mEcho.setEdgeTriggerType(Gpio.EDGE_BOTH)
        mEcho.setActiveType(Gpio.ACTIVE_HIGH)

        mTrigger = service.openGpio(TRIGGER_PIN_NAME)
        mTrigger.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW)

        ultrasonicTriggerHandler = Handler();
        ultrasonicTriggerHandler.post(triggerRunnable);

        mEcho.registerGpioCallback(null, this);
    }

    val triggerRunnable: Runnable = Runnable() {
        readDistanceAsnyc();
    }

    fun readDistanceAsnyc(){
        mTrigger?.setValue(false)
        Thread.sleep(0,2000)

        mTrigger?.setValue(true)
        Thread.sleep(0,10000) //10 microsec

        mTrigger?.setValue(false)

        ultrasonicTriggerHandler.postDelayed(triggerRunnable, 1000);
    }

    override fun onGpioEdge(gpio: Gpio?): Boolean {
        if (gpio?.getValue() == false){
            time2 = System.nanoTime();

            var pulseWidth = time2 - time1;

            var distance = (pulseWidth / 1000000000.0) * 340.0 / 2.0 * 100.0;

            Log.e("distance", "distance: $distance")

        } else {
            time1 = System.nanoTime();
        }

        return true;
    }

}
