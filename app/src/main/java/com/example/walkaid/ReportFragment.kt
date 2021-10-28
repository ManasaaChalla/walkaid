package com.example.walkaid

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.walkaid.adapter.ReportAdapter
import com.example.walkaid.databinding.FragmentReportBinding
import com.example.walkaid.databinding.SingleReportItemBinding
import com.example.walkaid.result.RunningResultActivity
import com.slt.lib.SLTSdk
import com.slt.lib.certify.SLTCertifyResult
import com.slt.lib.datamanager.DataManager
import com.slt.lib.running.RunningRecord

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ReportFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ReportFragment : Fragment() {
    lateinit var mContext: Context
    var mSLTSdk: SLTSdk? = null
    var mSDKLoadFailResult: SLTCertifyResult? = null
    var mDataManager: DataManager? = null
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var viewBinding: FragmentReportBinding

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
        viewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_report, container, false)
        viewBinding.setLifecycleOwner(this)
        mSLTSdk = SLTSdk.getInstance(this.requireContext())
        if (mSLTSdk == null) {
            mSDKLoadFailResult = SLTSdk.getCertifyResult()
            Toast.makeText(mContext, "SDK Load fail $mSDKLoadFailResult", Toast.LENGTH_SHORT).show()
        } else {
            mDataManager = mSLTSdk!!.DataManager()


        }
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val samplearrlist = arrayListOf("one","one","one","one","one","one")
       val reportadapter = ReportAdapter(activity ,samplearrlist)
        viewBinding.reportRecycler.layoutManager=LinearLayoutManager(activity)
        viewBinding.reportRecycler.adapter=reportadapter

    }
    override fun onResume() {
        super.onResume()
        loadData()
    }
    fun loadData() {
        mDataManager?.let {
            it.getRunningList {
                updateListData(it)
            }
        }
    }
    fun moveReport(id: Long) {
        var reportIntent = Intent(mContext, RunningResultActivity::class.java).apply {
            this.putExtra(RunningResultActivity.EXTRA_RUNNING_ID, id)
        }
       requireActivity().runOnUiThread {
            startActivity(reportIntent)
        }

    }
    fun updateListData(exercises: ArrayList<RunningRecord>) {
        requireActivity().runOnUiThread {
            viewBinding.lvExerciseList.adapter = RunningAdapter(exercises)
        }
    }
    fun deleteRunning(id: Long) {
        mDataManager?.let {
            it.removeRunning(id, {
                loadData()
            })
        }
    }
    inner class RunningAdapter(
        var exerciseList: List<RunningRecord>
    ) : BaseAdapter() {


        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var binding: SingleReportItemBinding?
            if (convertView == null) {

                binding =
                    SingleReportItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                binding.root.tag = binding
            } else {
                binding = convertView.tag as SingleReportItemBinding
            }
            binding.activity = mContext as ReportFragment
            binding.id = exerciseList!!.get(position).id
            binding.startDate = exerciseList!!.get(position).createDate


            return binding!!.root
        }

        override fun getItem(position: Int): Any {
            return exerciseList!!.get(position)
        }

        override fun getItemId(position: Int): Long {
            return 0
        }

        override fun getCount(): Int {
            return exerciseList!!.size
        }
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ReportFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ReportFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}