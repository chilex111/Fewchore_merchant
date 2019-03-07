package merchant.com.our.merchant.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_history.*
import kotlinx.android.synthetic.main.template_progress.*

import merchant.com.our.merchant.R
import merchant.com.our.merchant.api.ProfileAsync
import merchant.com.our.merchant.helper.AppUtils
import merchant.com.our.merchant.listener.IdListener
import merchant.com.our.merchant.listener.ProfileListener
import merchant.com.our.merchant.model.ProfileModel
import merchant.com.our.merchant.view.CircleImageView
import java.lang.Exception
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ProfileFragment : Fragment(), ProfileListener {


    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var textName:TextView ?= null
    private var textPhone: TextView ?= null
    private var textAddress: TextView ?= null
    private var textTerritory: TextView ?= null
    private var textLoan: TextView ?= null
    private var textMaxLoan: TextView ?= null
    private var textDueDate: TextView ?= null
    private var textAgent: TextView ?= null
    private var textOverDue: TextView ?= null
    private var textDisbursed: TextView ?= null
    private var textCode: TextView ?= null
    private var buttonRequest: Button?= null
    private var buttonPay: Button?= null
    private var Outstanding: LinearLayout ?= null
    private var textBalance: TextView ?= null
    private var loanId :  String ?= null
    private var imageProfile: CircleImageView ?= null
    private var appUtils : AppUtils ?= null
    private var linearExtra: LinearLayout ?= null
    private  var currentLimit: TextView ?= null
    private var outstandingLoan: Float ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_profile, container, false)


        ProfileAsync.profileListener = this
        appUtils = AppUtils(activity!!)

        textName = v.findViewById(R.id.merchantName)
        textCode = v.findViewById(R.id.merchantCode)
        textPhone = v.findViewById(R.id.merchantPhone)
        textAddress = v.findViewById(R.id.merchantAddress)
        textTerritory = v.findViewById(R.id.merchantTerritory)
        textLoan = v.findViewById(R.id.merchantLoanStatus)
        textMaxLoan = v.findViewById(R.id.merchantMaxLoan)
        textAgent = v.findViewById(R.id.merchantAgent)
        Outstanding = v.findViewById(R.id.linearBalance)
        textBalance = v.findViewById(R.id.merchantOutstanding)
        imageProfile = v.findViewById(R.id.profile)
        textDisbursed = v.findViewById(R.id.merchantDisbursed)
        textDueDate = v.findViewById(R.id.merchantDueDate)
        textOverDue = v.findViewById(R.id.merchantOverDue)
        linearExtra = v.findViewById(R.id.linearExtra)
        currentLimit = v.findViewById(R.id.merchantCurrentLimit)

        buttonRequest = v.findViewById(R.id.buttonNext)
        buttonPay =  v.findViewById(R.id.buttonPayNow)


        textAgent!!.text = AppUtils.getMyBuissnessName(activity)
        textName!!.text= AppUtils.getMyFullName(activity)
        textPhone!!.text = AppUtils.getMyPhoneNumber(activity)
        textAddress!!.text = AppUtils.getMyAddress(activity)
        textTerritory!!.text = AppUtils.getMyTerritory(activity)
        textLoan!!.visibility = View.GONE
        if (AppUtils.getMyLoanStatus(activity) =="1")
            textLoan!!.text = getString(R.string.return_customer)
        if (AppUtils.getMyLoanStatus(activity) =="0")
            textLoan!!.text = getString(R.string.first_customer)

        val img = AppUtils.getMyProfile(activity)
        Picasso.with(activity).load(img).into(imageProfile)

        textCode!!.text = AppUtils.getMyCode(activity)
        ProfileAsync(activity!!).execute()
        views()
        return  v
    }

    private fun paymentPlan() {
        val dialog = Dialog(activity)
        dialog.setContentView(R.layout.dialog_payment_option)
        dialog.findViewById<Button>(R.id.buttonInstalment).setOnClickListener {
            instalmentPayment()
            dialog.dismiss()

        }
        dialog.findViewById<Button>(R.id.buttonPayAll).setOnClickListener {


            val msg = "You wish to pay up your loan of \n balance = ₦ $outstandingLoan"

            val localBuilder = android.support.v7.app.AlertDialog.Builder(activity!!)
            localBuilder.setMessage(msg)
            localBuilder.setNeutralButton(R.string.ok) { _, _ ->
                dialog.dismiss()
                listener!!.idListener("pay_now", null,outstandingLoan)
            }
            localBuilder.create().show()
        }
        dialog.show()
    }

    private fun instalmentPayment() {
        val dialog = Dialog(activity!!)
        dialog.setContentView(R.layout.dialog_instalment_payment)
        dialog.findViewById<ImageButton>(R.id.buttonClose).setOnClickListener {
            dialog.dismiss()
        }
        val editAmt = dialog.findViewById<EditText>(R.id.editInstalmentAmt)
        editAmt.addTextChangedListener(object : TextWatcher {
            internal var isManualChange = false
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
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
                    editAmt!!.setText(finalValue.reverse())
                    editAmt.setSelection(finalValue.length)
                } catch (e: Exception) {
                    // Do nothing since not a number
                }            }

        })
        dialog.findViewById<Button>(R.id.buttonInstalment).setOnClickListener {
            val amt = editAmt.text.toString()
            if (amt.isEmpty())
                editAmt.error = "This field is required to complete your payment"
            else {
                listener!!.idListener("pay_now",null, amt.toFloat())
                dialog.dismiss()
            }


        }
        dialog.show()
    }


    private fun views() {
        var currentLimit: String ?= null
        buttonPay!!.setOnClickListener {
        paymentPlan()
    }
        if(!AppUtils.getMyCurrentLimit(activity).isNullOrEmpty()) {
             currentLimit = AppUtils.getMyCurrentLimit(activity)!!.replace(",","")

        }else{
            ProfileAsync(activity!!).execute()
        }
        buttonRequest!!.setOnClickListener {

            if (currentLimit == "0") {
                appUtils!!.showAlert("You have no credit left. Please payback the Loan you have to be able to take more loan")
            }else{
                listener!!.idListener("invest", null,null)
            }

        }
    }

    override fun profileListener(profileModel: ProfileModel, status: String) {
        if (status == "true") {
            outstandingLoan = profileModel.outstandingLoan.toFloat()
            val model = profileModel.msg
            textAgent!!.text = model.userFullname
            textName!!.text = model.userBvnName
            textPhone!!.text = model.userPhone
            textAddress!!.text = model.userAddress
            textTerritory!!.text = model.userTerritory
            textLoan!!.visibility = View.GONE
            textOverDue!!.text = profileModel.daysOverDue.toString()

            if(outstandingLoan!!.toInt() == 0){
                linearExtra!!.visibility = View.GONE

                buttonPay!!.isEnabled = false
                buttonPay!!.alpha =0.3f
            }else {
                textBalance!!.text = profileModel.outstandingLoan.toFloat().toString()

                if (!profileModel.loanDueDate.isNullOrEmpty() && (!profileModel.disbursementDate.isNullOrEmpty())) {
                    val dueDate = profileModel.loanDueDate
                    val disburseDate = profileModel.disbursementDate

                    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val displayDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    try {
                        textDueDate!!.text = displayDate.format(dateFormat.parse(dueDate))
                        textDisbursed!!.text = displayDate.format(dateFormat.parse(disburseDate))
                    } catch (e: ParseException) {
                        e.printStackTrace()
                    }
                } else {
                    linearExtra!!.visibility = View.GONE
                }
            }
            if (AppUtils.getMyLoanStatus(activity) == "1")
                textLoan!!.text = getString(R.string.return_customer)
            if (AppUtils.getMyLoanStatus(activity) == "0") {
                textLoan!!.text = getString(R.string.first_customer)
            }

            val decimalFormat = DecimalFormat("#,###.00")

            val currentLimitText = decimalFormat.format(java.lang.Double.parseDouble(profileModel.msg.currentLimit))
            currentLimit!!.text = currentLimitText

            val loanLimit = decimalFormat.format(java.lang.Double.parseDouble(profileModel.msg.userLimit))
            textMaxLoan!!.text = loanLimit

            textCode!!.text = AppUtils.getMyCode(activity)

            if (profileModel.daysOverDue > 0)
                appUtils!!.showAlert("Dear Agent, your Loan is over due for " + profileModel.daysOverDue + ", kindly repay to make a new request")
        }

    }

    companion object {
        var listener: IdListener ?= null
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                ProfileFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}
