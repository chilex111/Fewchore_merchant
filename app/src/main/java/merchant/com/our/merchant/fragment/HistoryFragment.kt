package merchant.com.our.merchant.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.fragment_history.*

import merchant.com.our.merchant.R
import merchant.com.our.merchant.adapter.LoanHistoryAdapter
import merchant.com.our.merchant.api.ActiveLoanAsync
import merchant.com.our.merchant.listener.ActiveLoanListener
import merchant.com.our.merchant.model.ActiveLoanModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [HistoryFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [HistoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class HistoryFragment : Fragment(), ActiveLoanListener {

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var recyclerView: RecyclerView ?= null
    private var relativeProgress: RelativeLayout ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_history, container, false)
        ActiveLoanAsync.activeLoanListener = this

        recyclerView = v.findViewById(R.id.recyclerView)
        relativeProgress = v.findViewById(R.id.relativeProgress)

        ActiveLoanAsync(activity!!).execute()
        return  v
    }

    override fun activeLoanListener(activeLoan: ActiveLoanModel?, msg: String?, status: String) {
        relativeProgress!!.visibility = View.GONE
        if (status =="true") {
            if (activeLoan != null) {
                val totalLoan = "₦ "+ activeLoan.outstandingLoan.toFloat().toString()
                textOutstanding.text =totalLoan
                if (activeLoan.activeloan.size >0) {
                    val historyAdapter = LoanHistoryAdapter(activeLoan.activeloan, activity!!)
                    recyclerView!!.layoutManager = LinearLayoutManager(activity)
                    recyclerView!!.adapter = historyAdapter
                    recyclerView!!.requestFocus()
                }else{
                    val msgEmpty = "Dear customer you have no existing loan. You can take a loan within your Credit limit"

                    textEmpty.text = msgEmpty
                }
            }
        }else{
            textOutstanding.text = "0"
            textEmpty.text = msg
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HistoryFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                HistoryFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}
