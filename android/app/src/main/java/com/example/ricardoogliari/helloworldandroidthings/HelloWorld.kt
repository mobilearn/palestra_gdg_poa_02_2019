package com.example.ricardoogliari.helloworldandroidthings

import android.app.Activity
import android.os.Bundle
import android.util.Log
import com.google.android.things.pio.Gpio
import com.google.android.things.pio.PeripheralManager
import java.io.IOException


/**
 * Skeleton of an Android Things activity.
 *
 * Android Things peripheral APIs are accessible through the class
 * PeripheralManagerService. For example, the snippet below will open a GPIO pin and
 * set it to HIGH:
 *
 * <pre>{@code
 * val service = PeripheralManagerService()
 * val mLedGpio = service.openGpio("BCM6")
 * mLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW)
 * mLedGpio.value = true
 * }</pre>
 * <p>
 * For more complex peripherals, look for an existing user-space driver, or implement one if none
 * is available.
 *
 * @see <a href="https://github.com/androidthings/contrib-drivers#readme">https://github.com/androidthings/contrib-drivers#readme</a>
 *
 */
class HelloWorld : Activity() {

    val TAG: String = "athings"

    var gpio: Gpio? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var manager = PeripheralManager.getInstance()

        try {
            gpio = try {
                manager.openGpio("BCM13")
            } catch (e: IOException) {
                Log.w(TAG, "Unable to access GPIO", e)
                null
            }
            gpio?.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
            gpio?.setActiveType(Gpio.ACTIVE_HIGH)

            while (true) {
                gpio?.setValue(true);
                Thread.sleep(500);
                gpio?.setValue(false);
                Thread.sleep(500);
            }
        } catch (e: IOException) {
            e.printStackTrace();
        } catch (e: InterruptedException){
            e.printStackTrace();
        }

    }

    override fun onDestroy() {
        super.onDestroy()

        try {
            gpio?.close()
            gpio = null
        } catch (e: IOException) {
            Log.w(TAG, "Unable to close GPIO", e)
        }
    }
}
