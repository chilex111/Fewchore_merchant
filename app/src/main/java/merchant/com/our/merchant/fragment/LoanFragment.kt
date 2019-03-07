package merchant.com.our.merchant.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

import merchant.com.our.merchant.R
import merchant.com.our.merchant.enums.NavigationDirection
import merchant.com.our.merchant.helper.AppUtils
import merchant.com.our.merchant.listener.FragmentListener
import merchant.com.our.merchant.model.FormModel
import java.text.DecimalFormat

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val LOAN_DETAILS = "loan_details"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [LoanFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [LoanFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class LoanFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var listener: FragmentListener ?= null
    private var loanModel: FormModel? = null
    private var editAmount: EditText? = null
    private var totalAmount: TextView?= null
    private var maxAmount: String?= null
    private var appUtils: AppUtils?= null
    private var buttonSubmit: Button?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            loanModel = it.getParcelable(LOAN_DETAILS)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
       val v =inflater.inflate(R.layout.fragment_loan, container, false)

        appUtils = AppUtils(activity!!)
        editAmount = v.findViewById(R.id.editLoanAmt)
        totalAmount = v.findViewById(R.id.textTotal)
        buttonSubmit = v.findViewById(R.id.buttonProceed)
        maxAmount = AppUtils.getMyLimit(activity)
        val limitText = v.findViewById<TextView>(R.id.textLimit)
        val decimalFormat = DecimalFormat("#,###.00")
        val am = decimalFormat.format(java.lang.Double.parseDouble(maxAmount))

        limitText.text = am


        views()
        return  v
    }

    fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            internal var isManualChange = false

            override fun onTextChanged(s: CharSequence, start: Int, before: Int,
                                       count: Int) {
                if (isManualChange) {
                    isManualChange = false
                    return
                }

                try {
                    val value = s.toString().replace(",", "")
                    val reverseValue = StringBuilder(value).reverse()
                            .toString()
                    val finalValue = StringBuilder()
                    for (i in 1..reverseValue.length) {
                        val `val` = reverseValue[i - 1]
                        finalValue.append(`val`)
                        if (i % 3 == 0 && i != reverseValue.length && i > 0) {
                            finalValue.append(",")
                        }
                    }
                    isManualChange = true
                    editAmount!!.setText(finalValue.reverse())
                    editAmount!!.setSelection(finalValue.length)
                } catch (e: Exception) {
                    // Do nothing since not a number
                }

            }
            override fun afterTextChanged(s: Editable?) {


                afterTextChanged.invoke(s.toString())
            }
        })
    }

    fun views(){
        editAmount!!.afterTextChanged {
            try{
            if (!editAmount!!.text.toString().isEmpty()) {
                val textV = editAmount!!.text.toString().replace(",", "")
                val amount = Integer.valueOf(textV)
                val deci = 2 / 100.0f
                val percent = deci * amount
                val total = percent + amount
                val decimalFormat = DecimalFormat("#,###.00")
                val am = decimalFormat.format(java.lang.Double.parseDouble(total.toString()))

                totalAmount!!.text = am.toString()
                val max = AppUtils.getMyLimit(activity)!!.toInt()

                if (amount > max) {
                    val paramString = "Your loan plan is higher than your Maximum Loan Amount. Please request for a lower Amount "

                    val localBuilder = AlertDialog.Builder(activity!!)
                    localBuilder.setMessage(paramString)
                    localBuilder.setNeutralButton(R.string.ok) { _, _ ->
                        clearAmt()

                    }
                    localBuilder.create().show()
                }
            }

        }catch (e: Exception){
                Toast.makeText(activity!!, "Your input is the wrong format", Toast.LENGTH_SHORT).show()
            }
        }
        buttonSubmit!!.setOnClickListener {
            submitForm()

        }
    }

    private fun clearAmt() {
        editAmount!!.text.clear()
        editAmount!!.requestFocus()

    }

    private fun submitForm(){

        val amount =editAmount!!.text.toString().replace(",","")
        if(!amount.isEmpty()) {
            val floatAmt = amount.toDouble()
            val deci = (floatAmt*2 )/ 100.0f
           // val percent = (deci * floatAmt).toInt()
            val total = totalAmount!!.text.toString().replace(",", "")

            val max = AppUtils.getMyLimit(activity)!!.toInt()


            if (amount.isEmpty()) {
                editAmount!!.error = "This field i required"
                return
            }
            if (Integer.valueOf(amount) > max) {
                appUtils!!.showAlert("Your loan plan is higher than your Maximum Loan Amount. Please request for a lower Amount ")
            } else {
                loanModel = FormModel()

                loanModel!!.duration = "10 days"
                loanModel!!.loanAmount = amount
                loanModel!!.interest = deci.toString()
                loanModel!!.totalPayback = total
                listener!!.onFormDetailSubmit(loanModel!!)
                listener!!.onFragmentNavigation(NavigationDirection.FORM_FORWARD)
            }
        }else {
            appUtils!!.showAlert("Please enter a valid plan")
            return
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (parentFragment is FragmentListener) {
            listener = parentFragment as FragmentListener
        } else {
            throw RuntimeException(context.toString() + " must implement Loan")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener =null
    }

    companion object {


        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String?, loanFormModel: FormModel?) =
                LoanFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putParcelable(LOAN_DETAILS, loanFormModel)
                    }
                }
    }
}
