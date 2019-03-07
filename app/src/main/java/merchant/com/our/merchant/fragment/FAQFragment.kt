package merchant.com.our.merchant.fragment

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import merchant.com.our.merchant.helper.Const
import merchant.com.our.merchant.helper.HttpUtility

import merchant.com.our.merchant.R
import merchant.com.our.merchant.adapter.FAQAdapter
import merchant.com.our.merchant.model.FAQModel
import java.io.IOException

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class FAQFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var recyclerView: RecyclerView? = null
    private var relativeLayout: RelativeLayout? = null
    private var empty: TextView? = null
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
        val v = inflater.inflate(R.layout.fragment_faq, container, false)

        recyclerView = v.findViewById(R.id.recyclerView)
        relativeLayout = v.findViewById(R.id.relativeProgress)
        relativeLayout!!.visibility = View.VISIBLE
        empty = v.findViewById(R.id.textViewEmpty)
        empty!!.visibility = View.GONE
        FAQAsync().execute()

        return v
    }

    @SuppressLint("StaticFieldLeak")
     inner class FAQAsync internal constructor() : AsyncTask<Void, Int, String>() {

        override fun doInBackground(vararg voids: Void): String? {
            return try {
                HttpUtility.getRequest(Const.FEWCHORE_URL + "faq")
            } catch (e: IOException) {
                e.printStackTrace()
                null
            }

        }

        override fun onPostExecute(s: String?) {
            super.onPostExecute(s)
            if (s != null) {
                if (!s.isEmpty()) {
                    relativeLayout!!.setVisibility(View.GONE)
                    try {
                        val gson = Gson()
                        val type = object : TypeToken<FAQModel>() {

                        }.type
                        val faqModel = gson.fromJson<FAQModel>(s, type)
                        if (faqModel != null) {
                            val faqModelList = faqModel.faq

                            val faqAdapter = FAQAdapter(activity, faqModelList)
                            recyclerView!!.layoutManager = LinearLayoutManager(activity)

                            recyclerView!!.adapter = faqAdapter
                            recyclerView!!.requestFocus()
                        }
                    } catch (e: Exception) {
                        Log.i("TAG", e.message)
                    }

                } else
                    empty!!.visibility = View.VISIBLE
            }
        }
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FAQFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                FAQFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}
