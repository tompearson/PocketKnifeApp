package com.example.pocketknifeapplication

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
// PocketKnife Libraries
import com.example.library_getbluetoothstatus.getBlueToothStatus
import com.example.library_getlocationstatus.getLocationStatus
import com.example.library_getmacaddress.getMACAddress
import com.example.library_getnetworkstatus.getNetworkStatus
import com.example.library_isitrooted.isItRooted
import com.example.library_map.MapsActivity
import com.example.library_withpermissions.methodWithPermissions
// Appcenter Libraries
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.Flags
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes
import com.microsoft.appcenter.distribute.Distribute
import com.microsoft.appcenter.utils.async.AppCenterConsumer

class MainActivity : AppCompatActivity() {

    lateinit var toast: Toast
    private lateinit var textMessage: TextView
    lateinit var button: Button
    lateinit var crash_button: Button

    internal val builder = StringBuilder()
    internal val properties: Map<String, String> = HashMap()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        // Setup buttons
        button = findViewById(R.id.button_Map_id)
        button.setText(R.string.title_map_button)

        crash_button = findViewById(R.id.button_crash_id)
        crash_button.setText(R.string.button_crash_text)

        textMessage = findViewById(R.id.message)
        textMessage.setTextColor(Color.BLACK)

        AppCenter.start(
            application, "d22cdd64-9c51-46aa-97e4-2adf0758f0a6",
            Analytics::class.java, Crashes::class.java, Distribute::class.java
        )
        val future = Crashes.hasCrashedInLastSession()
        future.thenAccept(AppCenterConsumer {
            if (it) {
                Analytics.trackEvent(getString(R.string.crash_detected), properties, Flags.CRITICAL)
                Toast.makeText(this, getText(R.string.crash_oops), Toast.LENGTH_LONG).show()
            }
        })

        // PocketKnife Core Features
        methodWithPermissions(this)

        textMessage.setText(getNetworkStatus(this, builder))
        textMessage.setText(getLocationStatus(this, builder))
        textMessage.setText(getBlueToothStatus(this, builder))
        textMessage.setText(getMACAddress(this, builder))
        textMessage.setText(isItRooted(this, builder))

        // Test crash feature
        crash_button.setOnClickListener {
            Analytics.trackEvent(
                getString(R.string.button_crashing_clicked),
                properties,
                Flags.CRITICAL
            )
            Crashes.generateTestCrash()
        }

        // Map feature
        button.setOnClickListener {
            Analytics.trackEvent(getString(R.string.button_map_clicked), properties, Flags.NORMAL);
            val intent: Intent = Intent(this, MapsActivity::class.java).apply {}
            try {
                startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    //    Android Lifecycle overides
    override fun onStop() {
        super.onStop()
        Analytics.trackEvent(getString(R.string.onStop), properties, Flags.NORMAL)
        Toast.makeText(this, getString(R.string.onStop), Toast.LENGTH_LONG).show()
    }
    override fun onRestart() {
        super.onRestart()
        Analytics.trackEvent(getString(R.string.onRestart), properties, Flags.NORMAL)
        Toast.makeText(this, getString(R.string.onRestart), Toast.LENGTH_LONG).show()
    }
    override fun onResume() {
        super.onResume()
        Analytics.trackEvent(getString(R.string.onResume), properties, Flags.NORMAL)
        Toast.makeText(this, getString(R.string.onResume), Toast.LENGTH_LONG).show()
    }
    override fun onStart() {
        super.onStart()
        Analytics.trackEvent(getString(R.string.onStart), properties, Flags.NORMAL)
        Toast.makeText(this, getString(R.string.onStart), Toast.LENGTH_LONG).show()
    }
    override fun onPause() {
        super.onPause()
        Analytics.trackEvent(getString(R.string.onPause), properties, Flags.NORMAL)
        Toast.makeText(this, getString(R.string.onPause), Toast.LENGTH_LONG).show()
    }
    override fun onDestroy() {
        super.onDestroy()
        Analytics.trackEvent(getString(R.string.onDestroy), properties, Flags.NORMAL)
        Toast.makeText(this, getString(R.string.onDestroy), Toast.LENGTH_LONG).show()
    }
}
