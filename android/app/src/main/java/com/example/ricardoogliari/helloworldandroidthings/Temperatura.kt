package com.example.ricardoogliari.helloworldandroidthings

import android.os.Bundle
import android.app.Activity

import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import com.google.android.things.pio.PeripheralManager
import com.google.android.things.pio.UartDevice
import com.google.android.things.pio.UartDeviceCallback
import java.util.*

class Temperatura : Activity(), UartDeviceCallback {
    override fun onUartDeviceDataAvailable(device: UartDevice?): Boolean {
        val buffer = ByteArray(8);
        while (device?.read(buffer, buffer.size)!! > 0) {
            val data = String(buffer);
            if (data.startsWith("$")){
                Log.e("falar", data.substring(1));
            }
        }
        Log.e("falar", "-------------");

        return true;

    }

    val TAG = "temp";
    lateinit var device: UartDevice;
    lateinit var ttsEngine: TextToSpeech;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_temperatura)

        ttsEngine = TextToSpeech(this, TextToSpeech.OnInitListener() {
            @Override fun onInit(status: Int) {
                if (status == TextToSpeech.SUCCESS) {
                    var localeBR = Locale("pt","br")
                    ttsEngine.setLanguage(localeBR)
                    ttsEngine.setPitch(1f)
                    ttsEngine.setSpeechRate(1f)
                }
            }
        });

        val manager = PeripheralManager.getInstance()

        device = manager.openUartDevice("UART0");

        device.setBaudrate(9600)
        device.setDataSize(8)
        device.setParity(UartDevice.PARITY_NONE)
        device.setStopBits(1)

        device.registerUartDeviceCallback(this);
    }

    fun temp(view: View) {
        sendToUartGPIO("T");
    }

    fun humi(view: View) {
        sendToUartGPIO("H");
    }

    fun sendToUartGPIO(send: String){
        device.write(send.toByteArray(), send.toByteArray().size);
    }


}
