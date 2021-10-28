package com.example.walkaid.view.activity

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.walkaid.*
import com.example.walkaid.data.Accelerometer
import com.example.walkaid.data.Balance
import com.example.walkaid.data.Sessions
import com.example.walkaid.data.WalkAidDB
import com.example.walkaid.databinding.ActivityHomeBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.slt.lib.SLTSdk
import com.slt.lib.bledevice.DeviceConnectionState
import com.slt.lib.certify.SLTCertifyResult
import com.slt.lib.datamanager.DataManager
import com.slt.lib.device.Device
import com.slt.lib.device.DeviceCallBack
import com.slt.lib.model.SensorData
import com.slt.lib.running.RunningCallBack
import com.slt.lib.running.RunningRealTimeData
import com.slt.lib.running.RunningServiceProperty
import com.slt.lib.running.RunningState
import java.util.*

class HomeActivity : AppCompatActivity(){

    lateinit var mContext: Context
    var mSLTSdk: SLTSdk? = null
    var mSDKLoadFailResult: SLTCertifyResult? = null
    var mDataManager: DataManager? = null
    lateinit var viewBinding: ActivityHomeBinding
    var mDevice: Device? = null
    lateinit var mediaPlayer: MediaPlayer;
    lateinit var appUtil: AppUtil



    lateinit var database: WalkAidDB

    var currentSessionStart: Long = 0
    var currentSessionStop: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this

        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        viewBinding.setLifecycleOwner(this)
        viewBinding.activity = this


        database = GlobalContext.getDatabaseInstance(this)

        mSLTSdk = SLTSdk.getInstance(this)
//        if (mSLTSdk == null) {
//            mSDKLoadFailResult = SLTSdk.getCertifyResult()
//            Toast.makeText(mContext, "SDK Load fail $mSDKLoadFailResult", Toast.LENGTH_SHORT).show()
//        } else {
//            mDataManager = mSLTSdk!!.DataManager()
//            mDevice = mSLTSdk!!.Device(this)
//        }
        mediaPlayer = MediaPlayer.create(this, R.raw.feel_right)
        appUtil = AppUtil(20)

        val fragment = NewHomeFragment()
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment, fragment.javaClass.getSimpleName())
            .commit()

         val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.homeFragment2 -> {
                    val fragment = NewHomeFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment, fragment.javaClass.getSimpleName())
                        .commit()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.reportFragment -> {
                    val fragment = ReportFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment, fragment.javaClass.getSimpleName())
                        .commit()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.userFragment -> {
                    val fragment = UserFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment, fragment.javaClass.getSimpleName())
                        .commit()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.menuFragment -> {
                    val fragment = MenuFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment, fragment.javaClass.getSimpleName())
                        .commit()
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

        viewBinding.bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

       /* mSLTSdk?.let {
            runOnUiThread {
                mDevice?.let {
                    Log.v("OnCreate", "Working")
                    viewBinding.leftBattery.text = it.getLeftBatteryLevel().toString()+"%"
                    viewBinding.rightBattery.text = it.getRightBatteryLevel().toString()+"%"
                    if (it.isConnected()) {
                        viewBinding.connectionStatus.text = "Connected"
                        viewBinding.statusLayout.setBackgroundColor(getResources().getColor(R.color.connectedColor))
                    } else {
                        viewBinding.connectionStatus.text = "Disconnected"
                        viewBinding.statusLayout.setBackgroundColor(getResources().getColor(R.color.disconnectedColor))
                    }
                }
            }
        }*/
//        onConnectionStateChanged()
    }


}