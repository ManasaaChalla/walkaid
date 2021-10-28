package com.example.walkaid

import android.annotation.SuppressLint
import android.content.Context
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import com.example.walkaid.data.Accelerometer
import com.example.walkaid.data.Balance
import com.example.walkaid.data.Sessions
import com.example.walkaid.data.WalkAidDB
import com.example.walkaid.databinding.FragmentNewHomeBinding
import com.example.walkaid.view.activity.GlobalContext
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
import com.bumptech.glide.Glide


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [NewHomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewHomeFragment : Fragment() , RunningCallBack, DeviceCallBack {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var viewBinding: FragmentNewHomeBinding
    lateinit var mediaPlayer: MediaPlayer
    lateinit var database: WalkAidDB

    var currentSessionStart: Long = 0
    var currentSessionStop: Long = 0

    private lateinit var gifView: ImageView;
    private var gifChangedTime: Long = System.currentTimeMillis();

    var mSLTSdk: SLTSdk? = null
    lateinit var mContext: Context
    var mSDKLoadFailResult: SLTCertifyResult? = null
    var mDataManager: DataManager? = null
    var mDevice: Device? = null
    lateinit var appUtil: AppUtil;
    var playbackSpeed: Float = 1f;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_new_home, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mContext= requireContext()
        database = GlobalContext.getDatabaseInstance(mContext)

        mSLTSdk = SLTSdk.getInstance(mContext)
        if (mSLTSdk == null) {
            mSDKLoadFailResult = SLTSdk.getCertifyResult()
            Toast.makeText(mContext, "SDK Load fail $mSDKLoadFailResult", Toast.LENGTH_SHORT).show()
        } else {
            mDataManager = mSLTSdk!!.DataManager()
            mDevice = mSLTSdk!!.Device(this)
        }
        mediaPlayer = MediaPlayer.create(activity, R.raw.feel_right);
        appUtil = AppUtil(20);

        gifView = view.findViewById(R.id.gif_img)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment NewHomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NewHomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    fun playMusic() {
        mediaPlayer.start();

    }

    fun pauseMusic() {
        mediaPlayer.pause()
    }

    fun onClickStart() {
        if (viewBinding.startStopButton.text.equals("Start")) {

            // Set GIF image
            gifChangedTime = System.currentTimeMillis();
            startGif();

            currentSessionStart = System.currentTimeMillis()
            var className = this.javaClass.name
            //com.slt.testapp.view.activity.MainActivity
            mSLTSdk?.let {
                var running = it.Running(this)
                running?.let {
                    it.setServiceProperty(RunningServiceProperty().apply {
                        this.notiMoveActivityName = className
                        this.channelName = "Running"
                        this.notiBigText = "notiBigText"
                        this.notiBigContentText = "notiBigContentText"
                        this.notiSurmaryText = "notiSurmaryText"
                        this.notiIconDrawable = com.example.walkaid.R.drawable.ic_heel_report
                    })
                    it.startRunning(mContext)
                }
            }
            mediaPlayer.start();
            viewBinding.startStopButton.text = "Stop"
        } else {
            // Set GIF
            stopGif()

            currentSessionStop = System.currentTimeMillis()
            val session = Sessions(currentSessionStart, currentSessionStop)
            mSLTSdk?.let {
                var running = it.Running(this)
                running?.let {
                    it.stopRunning()
                }
            }
            viewBinding.startStopButton.text = "Start"
            mediaPlayer.stop();
            database.sessionsDAO.insert(session)
            val count = database.sessionsDAO.getSessionsCount()
            Log.v("TOTAL SESSIONS", "$count")
        }

    }

    private fun startGif() {
        var change = System.currentTimeMillis() - gifChangedTime
        Log.v("Gif elapsed", change.toString());
        if(change < 3000) {
            Log.w("GIF change",change.toString())
            return;
        }
        activity?.runOnUiThread {
            Log.v("Gif elapsed", "Changing!!!!!!!!!!!!!**********************")
            Glide.with(mContext)
                .load(R.drawable.walking_gif)
                .into(gifView);
        }
        gifChangedTime = System.currentTimeMillis();
    }

    private fun stopGif() {
        var change = System.currentTimeMillis() - gifChangedTime
        Log.v("Gif elapsed", change.toString());
        if(change < 3000) {
            Log.w("GIF change",change.toString())
            return;
        }
        activity?.runOnUiThread {
            Log.v("Gif elapsed", "Changing!!!!!!!!!!!!!**********************")
            Glide.with(mContext)
                .load(R.drawable.stop_gif)
                .into(gifView);
        }
//        Glide.with(mContext).load(R.drawable.stop_gif).into(gifView);
        gifChangedTime = System.currentTimeMillis();
    }


    override fun onChangeState(state: RunningState) {
        TODO("Not yet implemented")
        Log.v("UB-Sensor", "State Name: ${state.name}}")
    }

    override fun onRunningAnalyzed(runningRealTimeData: RunningRealTimeData) {
        TODO("Not yet implemented")
        Log.v("UB-Sensor", "Real time data: ${runningRealTimeData.toString()}}")
        updateRunningData(runningRealTimeData)
    }

    override fun onRunningStop(recordID: Long) {
        TODO("Not yet implemented")
        Log.v("UB-Sensor", "Recorded ID: ${recordID}}")
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onSensorReceived(sensorData: SensorData) {
        val timeConverter = TimeConverter(sensorData.timeCount)
        viewBinding.duration.text = timeConverter.fullTime

        val timestamp = System.currentTimeMillis()
        val accelerometer = Accelerometer(
            timestamp,
            sensorData.accelerometerValues[0],
            sensorData.accelerometerValues[1],
            sensorData.accelerometerValues[2],
            sensorData.accelerometerValues[3],
            sensorData.accelerometerValues[4],
            sensorData.accelerometerValues[5],
        )
        val balance = Balance(
            timestamp,
            sensorData.balanceValues[0],
            sensorData.balanceValues[1],
            sensorData.balanceValues[2],
            sensorData.balanceValues[3],
            sensorData.balanceValues[4],
            sensorData.balanceValues[5],
            sensorData.balanceValues[6],
            sensorData.balanceValues[7],
        )
        database.accelerometerDAO.insert(accelerometer)
        database.balanceDAO.insert(balance)


        // Log the balance

        Log.v("balance", Arrays.toString(sensorData.balanceValues))

        // Check stability

        checkStability(sensorData);

    }
    private fun checkStability(sensorData: SensorData) {

//        var vals = sensorData.balanceValues;
//        var unstable: Boolean = false;
//        var thresholdVal = 50;
//        for (i in 1..5 step 2) {
//            if(vals[i]-vals[i-1] > thresholdVal) {
//                unstable = true;
//                break;
//            }
//        }

        var hun = 0;
        var zer = 0;
        sensorData.balanceValues.forEach {
            if (it == 100) {
                hun++
            } else if(it == 0) {
                zer++
            }
        }
        var unstable = (hun == zer) && (hun == 3);
        Log.v("Unstable", unstable.toString())
        if (unstable) {
            stopGif()
        } else {
            startGif()
        }
    }


    override fun onBatteryLevelChanged(left: Int, right: Int) {
        TODO("Not yet implemented")
        viewBinding.leftBattery.text = left.toString()+"%"
//        viewBinding.leftBattery.text = getLeftBatteryLevel().toString()
        viewBinding.rightBattery.text = right.toString()+"%"
    }

    override fun onConnectionStateChanged(state: DeviceConnectionState) {
        TODO("Not yet implemented")
        Log.v("Device Disconnected????", state.toString())
//        val s1 = DeviceConnectionState.CONNECT_STATE_DISCONNECT
//        DeviceConnectionState.
        if(state.equals(DeviceConnectionState.CONNECT_STATE_DISCONNECT)){
            Log.v("Device Disconnected:::", state.equals(DeviceConnectionState.CONNECT_STATE_DISCONNECT).toString())
            viewBinding.connectionStatus.text = "Disconnected"
            viewBinding.statusLayout.setBackgroundColor(getResources().getColor(R.color.disconnectedColor))
        }
        else if(state.equals(DeviceConnectionState.CONNECT_STATE_BOTH_CONNECT)){
            Log.v("Device both connected::", state.equals(DeviceConnectionState.CONNECT_STATE_BOTH_CONNECT).toString())
            viewBinding.connectionStatus.text = "Connected"
            viewBinding.statusLayout.setBackgroundColor(getResources().getColor(R.color.connectedColor))
        }
    }



    @SuppressLint("NewApi")
    fun updateRunningData(data: RunningRealTimeData) {
        activity?.runOnUiThread {

            viewBinding.pace.text = StrUtils.getPaceStr(data.pace)
            viewBinding.cadence.text = data.cadence.toString()
            var duration = data.duration
            var hour = duration / 3600
            var min = (duration % 3600) / 60
            var sec = duration % 60
            viewBinding.distance.text = ((data.distance.toInt().toDouble())).toString()
            viewBinding.steps.text = data.steps.toString();


            // Change the playback speed
            var currPace: Int = data.cadence;
            if(currPace != 0) {
                if(currPace > playbackSpeed) {
                    playbackSpeed += (0.01f*playbackSpeed);
                } else {
                    playbackSpeed -= (0.01f*playbackSpeed);
                }
            } else {
                playbackSpeed = 1f;
            }

            Log.v("Playback speed: ", playbackSpeed.toString())
            Log.v("Pace: ", data.pace.toString())
            Log.v("Cadence: ", data.cadence.toString());
            var params = mediaPlayer.playbackParams;
            params.setSpeed(playbackSpeed)
            mediaPlayer.playbackParams = params;
            viewBinding.playbackSpeed.text = playbackSpeed.toString();

    //            mediaPlayer.playbackParams.speed = playbackSpeed;
    //            playbackSpeed = appUtil.next(currPace).toFloat();
    //
    //            viewBinding.playbackSpeed.text = playbackSpeed.toString();

    //            when (data.landingMethod) {
    //                LandingMethod.FORE -> viewBinding.tvLandingMethodSubtitle.text = "FORE"
    //                LandingMethod.MID -> viewBinding.tvLandingMethodSubtitle.text = "MID"
    //                LandingMethod.HILL -> viewBinding.tvLandingMethodSubtitle.text = "HELL"
    //            }

        }
    }
}