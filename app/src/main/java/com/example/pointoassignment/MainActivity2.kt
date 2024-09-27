package com.example.pointoassignment

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

class MainActivity2 : AppCompatActivity() {
    private val REQUEST_ENABLE_BT = 0
    private val REQUEST_DISCOVER_BT = 1
    private var devices: Set<BluetoothDevice>? = null
    private lateinit var mblueiv: ImageView
    private lateinit var mstatus: TextView
    private lateinit var pairedtv: TextView
    private lateinit var monbtn: Button
    private lateinit var moffbtn: Button
    private lateinit var mdisco: Button
    private lateinit var mpaired: Button
    private var mblueadapter: BluetoothAdapter? = null
    private var pairedDevices: Set<BluetoothDevice>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        if (mblueadapter == null) {
            mstatus.text = "BLUETOOTH NOT AVAILABLE"
        } else {
            mstatus.text = "BLUETOOTH AVAILABLE"
        }

        if (mblueadapter?.isEnabled == true) {
            mblueiv.setImageResource(R.drawable.bluetooth_on)
        } else {
            mblueiv.setImageResource(R.drawable.bluetooth_off)
        }

        monbtn.setOnClickListener {
            if (mblueadapter == null) {
                Toast.makeText(applicationContext, "Bluetooth Not Supported", Toast.LENGTH_SHORT).show()
            } else {
                if (mblueadapter?.isEnabled == false) {
                    val turnon = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                    if (ActivityCompat.checkSelfPermission(this@MainActivity2, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                        startActivityForResult(turnon, REQUEST_ENABLE_BT)
                        Toast.makeText(applicationContext, "Bluetooth Turned ON", Toast.LENGTH_SHORT).show()
                    } else {
                        ActivityCompat.requestPermissions(
                            this@MainActivity2,
                            arrayOf(Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN),
                            REQUEST_ENABLE_BT
                        )
                    }
                }
            }
        }

        moffbtn.setOnClickListener {
            if (mblueadapter?.isEnabled == true) {
                if (ActivityCompat.checkSelfPermission(this@MainActivity2, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    mblueadapter?.disable()
                    showToast("TURNING OFF BLUETOOTH")
                    mblueiv.setImageResource(R.drawable.bluetooth_off)
                } else {
                    ActivityCompat.requestPermissions(
                        this@MainActivity2,
                        arrayOf(Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN),
                        REQUEST_ENABLE_BT
                    )
                }
            } else {
                showToast("BLUETOOTH IS ALREADY OFF")
            }
        }

        mdisco.setOnClickListener {
            if (mblueadapter?.isEnabled == false) {
                showToast("Making your device discoverable")
                val intent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE)
                startActivityForResult(intent, REQUEST_DISCOVER_BT)
            }
        }

        mpaired.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(this@MainActivity2, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                val devices = mblueadapter?.bondedDevices
                devices?.forEach { device ->
                    pairedtv.append("\nDEVICE: ${device.name}, $device")
                }
            } else {
                ActivityCompat.requestPermissions(
                    this@MainActivity2,
                    arrayOf(Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN),
                    REQUEST_ENABLE_BT
                )
                showToast("TURNING ON BLUETOOTH TO GET PAIRED DEVICES")
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            // Bluetooth is on
            showToast("Bluetooth is on")
            mblueiv.setImageResource(R.drawable.bluetooth_on)
        } else {
            // User denied to turn on Bluetooth
            showToast("Cannot turn on Bluetooth")
        }
    }

    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}
