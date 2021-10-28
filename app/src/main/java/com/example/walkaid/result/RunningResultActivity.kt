package com.example.walkaid.result

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.databinding.DataBindingUtil
import com.example.walkaid.R
import com.example.walkaid.databinding.ActivityRunningResultBinding
import com.example.walkaid.utils.RunningResultListener
import com.example.walkaid.utils.RunningResultPresenter
import com.slt.lib.SLTSdk
import java.util.*

class RunningResultActivity : AppCompatActivity()  , RunningResultListener {
    private val TAG = RunningResultActivity::class.java.simpleName
   lateinit var binding: ActivityRunningResultBinding
    companion object{
        val EXTRA_RUNNING_ID = "runId"
    }


    // 0 -> km, 1 -> mile
    var runningDisplayUnit = 0
    var mRunningID: Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_running_result)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_running_result)
        binding.setActivity(this)
        mRunningID = intent.getLongExtra(RunningResultActivity.EXTRA_RUNNING_ID, 0)
        if (mRunningID != 0L) {
            binding.presenter = RunningResultPresenter(
                mRunningID,
                SLTSdk.getInstance(this)!!.DataManager(),
                this,
                resources.configuration.locale,
                runningDisplayUnit
            )
        }

    }
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_share, menu)
        //        menuShare = menu.findItem(R.id.new_device);
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }

    override fun onClickDetailAndSplits(runningId: Int) {
        val intent = Intent(this, RunningResultActivity::class.java)
        intent.putExtra("id", runningId)
        startActivity(intent)
    }

    override fun setDateTitle(timeStamp: String) {
        runOnUiThread {
            title = ""
            binding.myTitle.text = timeStamp
        }
    }

    override fun setDisableButton() {}

    override fun setDuration(value: String) {
        runOnUiThread { binding.tvDuration.text = value }
    }

    override fun setDistance(value: String) {
        runOnUiThread { binding.tvDistance.text = value }
    }


    override fun setCalorie(value: String) {
        runOnUiThread { binding.tvCalorie.text = value }
    }

    override fun setSteps(value: String) {
        runOnUiThread { binding.tvStep.text = value }
    }

    override fun setAnalysisFootStrikeMain(strike: Int, percent: String) {
        runOnUiThread {
            binding.tvAnalysisFootStrike.setText(getFootStepStrike(strike))
            binding.tvAnalysisFootStrikePercent.text = String.format(
                Locale.US,
                resources.getString(R.string.format_percent),
                percent.toInt()
            )
        }
    }

    private fun getFootStepStrike(strike: Int): String? {

        /*
           type = 1 <- 포어풋
           type = 2 <- 미드풋
           type = 3 <- 힐풋
         */
        val strikeStr: String
        strikeStr = if (strike == 1) {
            resources.getString(R.string.fore_foot)
        } else if (strike == 2) {
            resources.getString(R.string.mid_foot)
        } else {
            resources.getString(R.string.heel_foot)
        }
        return strikeStr
    }


    override fun setAnalysisForeFootStrikeSub(percent: String) {
        runOnUiThread {
            binding.layoutAnalysisForeFoot.visibility = View.VISIBLE
            binding.tvAnalysisForeFoot.text = String.format(
                Locale.US,
                resources.getString(R.string.format_percent),
                percent.toInt()
            )
        }
    }
    override fun setAnalysisMidFootStrikeSub(percent: String) {
        runOnUiThread {
            binding.layoutAnalysisMidFoot.visibility = View.VISIBLE
            binding.tvAnalysisMidFoot.text = String.format(
                Locale.US,
                resources.getString(R.string.format_percent),
                percent.toInt()
            )
        }
    }

    override fun setAnalysisHeelFootStrikeSub(percent: String) {
        runOnUiThread {
            binding.layoutAnalysisHeelFoot.visibility = View.VISIBLE
            binding.tvAnalysisHeelFoot.text = String.format(
                Locale.US,
                resources.getString(R.string.format_percent),
                percent.toInt()
            )
        }
    }
    override fun setForeFootIndicator() {
        runOnUiThread {
            binding.imgLeftFore.visibility = View.VISIBLE
            binding.imgRightFore.visibility = View.VISIBLE
        }
    }


    override fun setMidFootIndicator() {
        runOnUiThread {
            binding.imgLeftMid.visibility = View.VISIBLE
            binding.imgRightMid.visibility = View.VISIBLE
        }
    }

    override fun setHeelFootIndicator() {
        runOnUiThread {
            binding.imgLeftHeel.visibility = View.VISIBLE
            binding.imgRightHeel.visibility = View.VISIBLE
        }
    }

    override fun setPace(value_km: String, value_mile: String) {
        runOnUiThread {
            val pace = if (value_mile != "") getPaceStr(value_mile) else "--"
            binding.tvPaceStr.text = pace
            binding.tvPaceValue.text = pace
        }
    }

    private fun getPaceStr(_pace: String): String? {
        val tempPace = _pace.toDouble()
        val pace = tempPace.toInt()
        val paceStr: String
        val min = pace / 60
        val sec = pace - min * 60
        paceStr = if (sec < 10) {
            "$min\'0$sec\""
        } else {
            min.toString() + "\'" + sec + "\""
        }
        return paceStr
    }

    override fun setCadence(value: Int) {
        runOnUiThread {
            val cadence: String = if (value > 0) value.toString() else "--"
            binding.tvCadenceStr.text = cadence
            binding.tvCadenceValue.text = cadence
        }
    }

    override fun setGroundContactTime(value: Double) {
        runOnUiThread {
            val gctValue = if (value > 0) value.toString() else "--"
            binding.tvGroundContactTimeStr.text = gctValue
            binding.tvGroundContactTimeValue.text = gctValue
        }
    }

    override fun onClickRoute(runningId: Int) {}

    fun onClickSection() {
//        val sectionIntent = Intent(this, RunningSectionInfoActivity::class.java)
//        sectionIntent.putExtra(RunningSectionInfoActivity.EXTRA_RUNNING_ID, mRunningID)
//        startActivity(sectionIntent)
    }
}