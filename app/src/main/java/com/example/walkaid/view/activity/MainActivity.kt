package com.example.walkaid.view.activity

import android.app.Instrumentation
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.room.Room
import com.slt.lib.SLTSdk
import com.slt.lib.bledevice.DeviceConnectionState
import com.slt.lib.certify.SLTCertifyResult
import com.slt.lib.device.Device
import com.slt.lib.device.DeviceCallBack
import com.slt.lib.model.SensorData
import com.slt.lib.running.*
import com.example.walkaid.R
import com.example.walkaid.ReportFragment
import com.example.walkaid.StrUtils
import com.example.walkaid.TimeConverter
import com.example.walkaid.data.Accelerometer
import com.example.walkaid.data.AccelerometerDAO
import com.example.walkaid.data.WalkAidDB
import com.example.walkaid.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*

class MainActivity : AppCompatActivity(), DeviceCallBack, RunningCallBack {
    lateinit var mContext: Context
    var mSLTSdk: SLTSdk? = null
    var mSDKLoadFailResult: SLTCertifyResult? = null
    var mRunning: Running? = null
    var mDevice: Device? = null
    lateinit var viewBinding: ActivityMainBinding

    var mIsRegist = false
    var mRunningState: RunningState? = null

    var database: WalkAidDB? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        database = WalkAidDB.getInstance(this)

        mContext = this

        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewBinding.setLifecycleOwner(this)
        viewBinding.activity = this

        mSLTSdk = SLTSdk.getInstance(this)
        if (mSLTSdk == null) {
            mSDKLoadFailResult = SLTSdk.getCertifyResult()
            Toast.makeText(mContext, "SDK Load fail $mSDKLoadFailResult", Toast.LENGTH_SHORT).show()
        } else {
            mDevice = mSLTSdk!!.Device(this)
        }

//        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
//        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
//        val navController = navHostFragment.navController
//        val navController = findNavController(R.id.fragmentContainerView)
//        bottomNavigationView.setupWithNavController(navController)
    }

    fun startConnection() {
        if (viewBinding.connectText.equals("Disconnect")) {
            mSLTSdk?.let {
                mDevice?.let {
                    if (it.isConnected()) {
                        it.disConnect()
                        viewBinding.connectText.text = "Connect"
                    }
                }
            }
        } else {
            mSLTSdk?.let {
                it.startDeviceInfo(mContext)
            }
        }
    }

    //DeviceCallback
    override fun onBatteryLevelChanged(left: Int, right: Int) {

    }

    fun showHome() {
        startActivity(Intent(this, HomeActivity::class.java));
    }

    fun showReport() {
        startActivity(Intent(this, ReportActivity::class.java));
    }

    override fun onConnectionStateChanged(state: DeviceConnectionState) {
//        if(state.equals(DeviceCo))
        if(state.equals(DeviceConnectionState.CONNECT_STATE_DISCONNECT)){
//            Log.v("Device Disconnected:::", state.equals(DeviceConnectionState.CONNECT_STATE_DISCONNECT).toString())
            viewBinding.connectText.text = "Connect"
//            viewBinding.statusLayout.setBackgroundColor(getResources().getColor(R.color.disconnectedColor))
        }
        else if(state.equals(DeviceConnectionState.CONNECT_STATE_BOTH_CONNECT)){
//            Log.v("Device both connected::", state.equals(DeviceConnectionState.CONNECT_STATE_BOTH_CONNECT).toString())
            viewBinding.connectText.text = "Disconnect"
//            viewBinding.statusLayout.setBackgroundColor(getResources().getColor(R.color.connectedColor))
        }
    }

    override fun onChangeState(state: RunningState) {
        Log.v("UB-Sensor", "State Name: ${state.name}}")
    }

    override fun onRunningAnalyzed(runningRealTimeData: RunningRealTimeData) {
        Log.v("UB-Sensor", "Real time data: ${runningRealTimeData.toString()}}")
    }

    override fun onRunningStop(recordID: Long) {
        Log.v("UB-Sensor", "Recorded ID: ${recordID}}")
    }

    override fun onSensorReceived(sensorData: SensorData) {

        var timeConverter = TimeConverter(sensorData.timeCount)
//        viewBinding.time.text = timeConverter.fullTime
//        viewBinding.accValue.text = sensorData.accelerometerValues.joinToString()
//        viewBinding.balanceValue.text = sensorData.balanceValues.joinToString()
//        viewBinding.latitudeVal.text = "${sensorData.latitude}"
//        viewBinding.longitudeValue.text = "${sensorData.longitude}"

    }

}
