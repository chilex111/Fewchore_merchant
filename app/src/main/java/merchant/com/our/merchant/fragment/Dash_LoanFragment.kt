package merchant.com.our.merchant.fragment

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.CardView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import merchant.com.our.merchant.R
import merchant.com.our.merchant.adapter.CardAdapter
import merchant.com.our.merchant.api.ProfileAsync
import merchant.com.our.merchant.enums.NavigationDirection
import merchant.com.our.merchant.helper.AppUtils
import merchant.com.our.merchant.helper.Const
import merchant.com.our.merchant.helper.HttpUtility
import merchant.com.our.merchant.listener.FragmentListener
import merchant.com.our.merchant.listener.IdListener
import merchant.com.our.merchant.listener.ProfileListener
import merchant.com.our.merchant.local_database.LocalDatabase
import merchant.com.our.merchant.model.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class DashLoanFragment : Fragment(), FragmentListener,ProfileListener {

    // TODO: Rename and change types of parameters
    private var page_from: String? = null
    private var param2: String? = null
    private var titleAdapter: ArrayAdapter<String>? = null
    private var localDB : LocalDatabase? = null
    private var cardAdapter : CardAdapter? = null
    private var loanForm: FormModel? = null
    private var bankModel: BankModel? = null
    private var passwordModel: PasswordModel? = null
    private var cardList : List<CardModel>? = null
    private var acctTypeSelected: String?= null
    private var banKName: String?= null
    private var acctTypeId: Int? = null
    private var appUtils: AppUtils? = null
    private var progressBar: ProgressBar? = null
    private var accType: String?= null
    private var cardModel:CardModel ? = null
    private var amountTaken: TextView ?= null
    private var interest: TextView ?= null
    private var dueDate: TextView ? = null
    private var extraCharge: TextView ?= null
    private var total: TextView ?= null
    private var loanCard: CardView ?= null
    private var approvalCard: CardView ?= null
    private var textAmount : TextView ?= null
    private var textInterest: TextView ?= null
    private var textCurrentlimit: String ?= null
    private var textTotal: TextView ?= null
    private var textApproval: TextView ?= null
    private var fragContainer: FrameLayout?= null
    private var relativeProgress: RelativeLayout ?= null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            page_from = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view =inflater.inflate(R.layout.fragment_dash_loan, container, false)

        localDB = LocalDatabase(activity!!) // initialise local DB
        appUtils = AppUtils(this.activity!!)

        /* AwaitApprovalAsync.statusListener = this*/
      //  ActiveLoanAsync.activeLoanListener = this
        ProfileAsync.profileListener = this
        childFragmentManager
                .beginTransaction()
                .add(R.id.container, LoanFragment())
                .commit()

        textAmount = view.findViewById(R.id.textAmount)
        textInterest = view.findViewById(R.id.textInterest)
        textTotal = view.findViewById(R.id.textTotal)
        textApproval = view.findViewById(R.id.textApproval)
        approvalCard = view.findViewById(R.id.cardApproval)
        approvalCard!!.visibility = View.GONE
        fragContainer = view.findViewById(R.id.container)
        relativeProgress = view.findViewById(R.id.relativeProgress)

        if (!AppUtils.getMyCurrentLimit(activity).isNullOrEmpty()){//check the set current limit
            textCurrentlimit = AppUtils.getMyCurrentLimit(activity)
            if (textCurrentlimit =="0"){
                fragContainer!! .visibility = View.GONE
                approvalCard!!.visibility  = View.VISIBLE
                textApproval!!.visibility = View.VISIBLE
                val text = "Dear customer, you have no credit left. Please payback the Loan you have to be able to take more loan"
                textApproval!!.text =  text
            }

        }else {
            relativeProgress!!.visibility = View.VISIBLE
            ProfileAsync(activity!!).execute()
        }

        loanForm = FormModel()
        bankModel = BankModel()
        passwordModel = PasswordModel()
        cardModel =  CardModel()

        val containerLayout = view.findViewById(R.id.loanContainer)as ConstraintLayout
        if (page_from.equals("1"))
            containerLayout.setBackgroundColor(ContextCompat.getColor(this.activity!!, R.color.green_light))

        amountTaken = view.findViewById(R.id.textAmount)
        interest = view.findViewById(R.id.textInterest)
        extraCharge = view.findViewById(R.id.extra)
        total = view.findViewById(R.id.textTotal)
        loanCard =view.findViewById(R.id.cardTakeLoan)
        loanCard!!.visibility = View.GONE
        // add an if statement so if there is loan display loan and balance else display loan form


        cardList = localDB!!.allCard //gets all the cards that is in local DB
       // ActiveLoanAsync(activity!!).execute()
        return view
    }
    override fun profileListener(profileModel: ProfileModel, status: String) {
        if (status =="true"){
            textCurrentlimit = profileModel.msg.currentLimit
            if (textCurrentlimit =="0"){
                fragContainer!! .visibility = View.GONE
                approvalCard!!.visibility  = View.VISIBLE
                textApproval!!.visibility = View.VISIBLE
                val text = "Dear customer, you have no credit left. Please payback the Loan you have to be able to take more loan"
                textApproval!!.text =  text
            }
        }else{

        }
    }
/*

    override fun activeLoanListener(activeLoan: Activeloan?, msg: String?, status: String, disbursed: String?) {
        //do a check to see if the user have an active loan if yes display the form else open a form that allow user to request for a form
        relativeProgress!!.visibility =View.GONE
        if (status =="true") {
            if (activeLoan != null) {
                loanCard!!.visibility = View.VISIBLE
                fragContainer!! .visibility = View.GONE
                if (activeLoan.loanPaybackdate != null)
                    dueDate!!.text = activeLoan.loanPaybackdate

                val decimalFormat = DecimalFormat("#,###.00")

                //      val loanTotal = decimalFormat.format(java.lang.Double.parseDouble(activeLoan.loanTotalpayback))
                val loanInt = decimalFormat.format(java.lang.Double.parseDouble(activeLoan.loanInterest))
                val loanAmt = decimalFormat.format(java.lang.Double.parseDouble(activeLoan.loanAmount))

                textInterest!!.text = loanInt
                textAmount!!.text = loanAmt
                total !!.text =   activeLoan.loanTotalpayback
            }
        }else{
            */
/* if (isAdded)
                 childFragmentManager
                         .beginTransaction()
                         .add(R.id.container, LoanFragment())
                         .commit()*//*

            if (disbursed=="true") {
                fragContainer!! .visibility = View.GONE
                approvalCard!!.visibility  = View.VISIBLE
                textApproval!!.visibility = View.VISIBLE
                val text = "Dear customer, you currently have a running facility. Kindly pay your loan before you can make a new request."
                textApproval!!.text =  text
            }

        }

        */
/*else{
            if (isAdded)
                childFragmentManager
                        .beginTransaction()
                        .add(R.id.container, LoanFragment())
                        .commit()
        }*//*

    }
*/

    override fun onFragmentNavigation(navigationDirection: NavigationDirection) {
        when(navigationDirection){
            NavigationDirection.FORM_FORWARD ->{
                relativeProgress!!.visibility =View.VISIBLE
                CheckUserLoanStatus().execute()

                return
            }
            /*  NavigationDirection.BANK_DETAILS_BACKWARD ->{
                  childFragmentManager
                          .beginTransaction()
                          .replace(R.id.container, LoanFragment.newInstance(null, loanForm))
                          .commit()
                  return
              }
              NavigationDirection.BANK_DETAILS_FORWARD ->{
                  childFragmentManager
                          .beginTransaction()
                          .replace(R.id.container, CardLoanListFragment.newInstance(null, null,cardModel))
                          .commit()
                  return
              }*/
            NavigationDirection.CARD_DETAILS_BACKWARD ->{
                childFragmentManager
                        .beginTransaction()
                        .replace(R.id.container, LoanFragment.newInstance(null, loanForm))
                        .commit()
                return
            }
            NavigationDirection.CARD_DETAILS_FORWARD ->{
                childFragmentManager
                        .beginTransaction()
                        .replace(R.id.container, PasswordFragment.newInstance(null, passwordModel))
                        .commit()
                return
            }
            NavigationDirection.PASSWORD_BACKWARD ->{
                childFragmentManager
                        .beginTransaction()
                        .replace(R.id.container, CardLoanListFragment.newInstance(null,null, cardModel))
                        .commit()
                return
            }

            NavigationDirection.PASSWORD_FORWARD ->{

                val accNo = bankModel!!.acctNo
                val acctype = bankModel!!.acctType
                val bcName= bankModel!!.bankName
                val dur= loanForm!!.duration
                val amt = loanForm!!.loanAmount
                val pass = passwordModel!!.password
                val cardNo = cardModel!!.cardNo
                val expr= cardModel!!.cardExpiry
                val cvv = cardModel!!.cardId

                val i = "$accNo $acctype $bcName $dur $amt $pass $cardNo $expr $cvv"
                Log.i("VALUES", i)
                appUtils!!.showAlert("Your request has been sent. One of our Agent will be in touch soon.")
            }
        }
    }

    override fun onFormDetailSubmit(formModel: FormModel) {
        this.loanForm = formModel
        //CHECK THE PERSONS LOAN STATUS

        /*  childFragmentManager
                  .beginTransaction()
                  .replace(R.id.container, BankListFragment.newInstance(null, bankModel))
                  .commit()*/

        /*  childFragmentManager
                  .beginTransaction()
                  .replace(R.id.container, CardLoanListFragment.newInstance(null, null,cardModel))
                  .commit()*/
    }


    override fun onCardDetailSubmit(cardModel: CardModel) {
        this.cardModel = cardModel
        childFragmentManager
                .beginTransaction()
                .replace(R.id.container, PasswordFragment.newInstance(null, passwordModel))
                .commit()       }

    override fun onPasswordDetailSubmit(passwordModel: PasswordModel) {
        this.passwordModel = passwordModel
        val msg = "₦100 will be charged to you card. This fee is not refundable."
        val localBuilder = AlertDialog.Builder(this.activity!!)
        localBuilder.setMessage(msg)
        localBuilder.setNeutralButton(R.string.ok) { _, _ ->
            relativeProgress!!.visibility = View.VISIBLE
            Pay100Async().execute()
            //LoanRequestAsync().execute()


        }
        localBuilder.create().show()

/*relativeProgress!!.visibility = View.VISIBLE
LoanRequestAsync().execute()*/

    }

    @SuppressLint("StaticFieldLeak")
    inner class Pay100Async: AsyncTask<Void, Int, String>() {
        override fun doInBackground(vararg p0: Void?): String {
            val url = "https://api.paystack.co/transaction/charge_authorization"
            val map = HashMap<String, Any?>()
            val auth_code = cardModel!!.authCode
            val email = AppUtils.getMyEmail(activity!!)

            map["authorization_code"] = auth_code
            map["email"] = email
            map["plan"]= 10000
            map["amount"]= 10000

            return HttpUtility.sendPaystackPostRequest(url, map)
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if (!result.isNullOrEmpty()){
                relativeProgress!!.visibility = View.GONE
                val gson = Gson()
                try{
                    val type = object :TypeToken<DeductModel>(){}.type
                    val response = gson.fromJson<DeductModel>(result, type)
                    if(response.status!!){
                        if (response.data!!.status =="success"){
                            LoanRequestAsync().execute()
                        }else{
                            appUtils!!.showAlert("The ₦100 charge could not be completed on this card."+response.data!!.gatewayResponse)
                        }
                    }else{


                        appUtils!!.showAlert("For this charge, an "+response.message!!)
                    }

                }catch (e: Exception){
                    Log.i("ERROR MESSAGE", e.javaClass.simpleName)
                }
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    inner class LoanRequestAsync: AsyncTask<Void, Int, String>() {
        override fun onPreExecute() {
            super.onPreExecute()
            relativeProgress!!.visibility = View.VISIBLE
        }
        override fun doInBackground(vararg p0: Void?): String {

            val url = Const.FEWCHORE_URL + "loanrequest"
            val map = HashMap<String, Any?>()
            val amt = loanForm!!.loanAmount
            val userId = AppUtils.getMyUserId(activity)
            val interest = loanForm!!.interest
            val total = loanForm!!.totalPayback

            map["loan_amount"] = amt
            map["loan_interest"] = interest
            map["loan_totalpayback"] =total
            map["loan_userid"] = userId

            return HttpUtility.sendPostRequest(url, map)
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if (!result.isNullOrEmpty()){
                relativeProgress!!.visibility = View.GONE
                val gson = Gson()
                try{
                    val type = object :TypeToken<ResponseStringModel>(){}.type
                    val response = gson.fromJson<ResponseStringModel>(result, type)
                    if (response.status == "true"){
                        appUtils!!.showAlert("Request has been submitted successfully" )
                        idListener!!.idListener("request",null,null)
                    }else{
                        appUtils!!.showAlert(response.msg!!)
                        idListener!!.idListener("request",null,null)
                    }
                }catch (e: Exception){
                    if (e is IllegalStateException)
                        appUtils!!.showAlert("Please check your network connection")
                }
            }else{
                appUtils!!.showAlert("Your request have been made")
                idListener!!.idListener("request",null, null)
            }
        }
    }


    companion object {

        // TODO: Rename and change types and number of parameters
        var idListener: IdListener ?= null
        @JvmStatic
        fun newInstance(param1: String, param2: String?) =
                DashLoanFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }


    @SuppressLint("StaticFieldLeak")
    inner class CheckUserLoanStatus :AsyncTask<Void, Int, String>(){
        override fun doInBackground(vararg p0: Void?): String? {
            try {
                val url = Const.FEWCHORE_URL + "userstatus"
                val map = HashMap<String, Any?>()
                val id = AppUtils.getMyUserId(activity)
                map["user_id"] = id
                return HttpUtility.sendPostRequest(url, map)
            }catch (e:Exception){

            }
            return null
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            relativeProgress!!.visibility =View.GONE
            if (result != null){
                val gson = Gson()

                try{
                    val type = object : TypeToken<ResponseStringModel>() {}.type
                    val userModel = gson.fromJson<ResponseStringModel>(result, type)
                    if (userModel != null) {
                        if (userModel.status == "true") {
                            childFragmentManager
                                    .beginTransaction()
                                    .replace(R.id.container, CardLoanListFragment.newInstance(null, null, cardModel))
                                    .commit()
                        } else {
                            //submit loan request for returning customer
                            val localBuilder = AlertDialog.Builder(activity!!)
                            localBuilder.setMessage("Are you sure you want to make this Loan Request?")
                            localBuilder.setNeutralButton(R.string.yes) { _, _ ->
                                relativeProgress!!.visibility=View.VISIBLE
                                LoanRequestAsync().execute()
                            }
                            localBuilder.setNegativeButton(R.string.no){_,_->
                                localBuilder.create().dismiss()
                            }
                            localBuilder.create().show()

                        }
                    }
                }catch (e:Exception){
                    appUtils!!.showAlert("Please check your internet connection")
                }
            }
        }
    }

}

