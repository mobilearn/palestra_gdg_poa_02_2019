package com.example.ricardoogliari.helloworldandroidthings

import android.os.Bundle
import android.app.Activity
import android.util.Log
import com.google.android.things.pio.Gpio
import com.google.android.things.pio.PeripheralManager
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging

import kotlinx.android.synthetic.main.activity_rele.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.IOException
import org.greenrobot.eventbus.EventBus


class Rele : Activity() {

    val TAG: String = "athings"

    var gpio: Gpio? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rele)

        FirebaseMessaging.getInstance().isAutoInitEnabled = true

        var manager = PeripheralManager.getInstance()

        try {
            gpio = try {
                manager.openGpio("BCM2")
            } catch (e: IOException) {
                Log.w(TAG, "Unable to access GPIO", e)
                null
            }
            gpio?.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
            gpio?.setActiveType(Gpio.ACTIVE_HIGH)

            /*while (true) {
                gpio?.setValue(true);
                Thread.sleep(5000);
                gpio?.setValue(false);
                Thread.sleep(5000);
            }*/
        } catch (e: IOException) {
            e.printStackTrace();
        } catch (e: InterruptedException){
            e.printStackTrace();
        }

        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener(this) { instanceIdResult ->
            // Just use this call
            val newToken = instanceIdResult.token
            Log.i("token", newToken)
        }

        EventBus.getDefault().register(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: MessageEvent) {/* Do something */
        if (event.message.equals("ligar")){
            gpio?.setValue(true);
        } else if (event.message.equals("desligar")){
            gpio?.setValue(false);
        }
    };

    public override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

}
