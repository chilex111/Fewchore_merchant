package merchant.com.our.merchant.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.View
import android.widget.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import merchant.com.our.merchant.helper.Const
import merchant.com.our.merchant.helper.HttpUtility
import merchant.com.our.merchant.helper.SharedPreferenceUtil
import merchant.com.our.merchant.model.ResponseStringModel
import merchant.com.our.merchant.R
import merchant.com.our.merchant.api.GetOTP
import merchant.com.our.merchant.helper.AppUtils
import merchant.com.our.merchant.helper.AppUtils.Companion.PREF_ADDRESS
import merchant.com.our.merchant.helper.AppUtils.Companion.PREF_BUISNESS_NAME
import merchant.com.our.merchant.helper.AppUtils.Companion.PREF_CODE
import merchant.com.our.merchant.helper.AppUtils.Companion.PREF_CONTACT_PERSON
import merchant.com.our.merchant.helper.AppUtils.Companion.PREF_EMAIL
import merchant.com.our.merchant.helper.AppUtils.Companion.PREF_FULLNAME
import merchant.com.our.merchant.helper.AppUtils.Companion.PREF_LIMIT
import merchant.com.our.merchant.helper.AppUtils.Companion.PREF_LOAN_STATUS
import merchant.com.our.merchant.helper.AppUtils.Companion.PREF_PHONENUMBER
import merchant.com.our.merchant.helper.AppUtils.Companion.PREF_PROFILE
import merchant.com.our.merchant.helper.AppUtils.Companion.PREF_TERRITORY
import merchant.com.our.merchant.helper.AppUtils.Companion.PREF_USERID
import merchant.com.our.merchant.helper.GMailSender
import merchant.com.our.merchant.listener.ResponseListener
import merchant.com.our.merchant.model.LoginModel
import java.lang.IllegalStateException

class LoginActivity : AppCompatActivity(),ResponseListener {

    private var editCode: EditText ?= null
    private var editPhone: EditText ?= null
    private var linearPhone: LinearLayout ?= null
    private var linearCode: LinearLayout ?= null
    private var buttonSubmit: Button ?= null
    private var appUtils: AppUtils ?= null
    private var relativeProgress : RelativeLayout ?= null
    private var progressDialog: RelativeLayout ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        appUtils = AppUtils(this)
        GetOTP.listener = this

        editCode = findViewById(R.id.editAgentCode)
        editPhone = findViewById(R.id.editAgentPhoneNo)
        linearPhone = findViewById(R.id.linearPhone)
        linearCode = findViewById(R.id.linearCode)
        buttonSubmit = findViewById(R.id.buttonLogin)
        relativeProgress = findViewById(R.id.relativeProgress)

        buttonSubmit!!.text = getString(R.string.code_auth)
        findViewById<Button>(R.id.buttonLogin).setOnClickListener {
            signIn()
        }
        findViewById<TextView>(R.id.support).setOnClickListener {
            supportForm()
        }
    }

    override fun idListener(id: String, msg: String?) {
        if (id == "true") {
            progressDialog!!.visibility = View.GONE
            val message = "An OTP has been sent to your registered Phone number. Insert OTP in the space provided to complete your Mobile PIN setup."
            val localBuilder = AlertDialog.Builder(this@LoginActivity)
            localBuilder.setMessage(message)
            localBuilder.setNeutralButton(R.string.ok) { _, _ ->
                startActivity(Intent(this@LoginActivity, CreatePinActivity::class.java))
            }
            localBuilder.create().show()
        }else{
            appUtils!!.showAlert(msg!!)

        }
    }

    private fun supportForm() {

        val dialog = Dialog(this, R.style.Dialog)
        dialog.setContentView(R.layout.dialog_contact_support)

        val editTitle = dialog.findViewById<EditText>(R.id.storyTitle)
        dialog.findViewById<ImageButton>(R.id.contactLine).setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:09058699681")
            startActivity(intent)
        }

        val editBody = dialog.findViewById<EditText>(R.id.editStory)
        val sendButton = dialog.findViewById<ImageButton>(R.id.buttonSend)
        val phone = dialog.findViewById<EditText>(R.id.ediPhone)
        val name= dialog.findViewById<EditText>(R.id.editFullname)
        dialog.show()
        dialog.findViewById<ImageButton>(R.id.buttonBack).setOnClickListener {
            dialog.dismiss()
        }
        sendButton.setOnClickListener {
            val bodyText = editBody.text.toString()
            val titleText = editTitle.text.toString()
            val phoneText = phone.text.toString()
            val nameText = name.text.toString()

            if (bodyText.isEmpty())
                editBody.error = "This field is required"

            if (nameText.isEmpty())
                name.error = "This field is required"
            if (phoneText.isEmpty())
                phone.error ="This field is required"
            if (titleText.isEmpty())
                editTitle.error ="This field is required"
            else
                sendEmail(titleText, bodyText, nameText, phoneText)
            dialog.dismiss()
        }
    }
    private fun sendEmail( title: String?, body: String?, name: String?,phone: String?) {

        /*
            if (!email.contains("@") && !email.contains(".")) {
                appUtils.showAlert("Please contact Radix Pension Customer Care to change Email")
            } else {*/
        val email = "fewchoremfb@gmail.com"
        Thread(Runnable {
            try {
                val sender = GMailSender(email,
                        "fewchoremfb@123")


                sender.sendMail(title, "Hi, \n My name is$name. \n$body and my phone number: $phone \n Thanks.",
                        email, "merchant@fewchorefinance.com,$email")

            } catch (e: Exception) {
                Log.e("SendMail", e.message, e)
            }
        }).start()

        val msg = "Request successful Submitted."
        val builder = AlertDialog.Builder(this)
        builder.setMessage(msg)
        builder.setNeutralButton(R.string.ok) { _, _ -> onBackPressed() }

        builder.create().show()
        //  }
    }

    private fun signIn() {
        val codeText = editCode!!.text.toString()

        if (codeText.isEmpty()){
            editCode!!.error = "This field is required"
        }

        else{
            relativeProgress!!.visibility = View.VISIBLE
            AuthenticateAsync(codeText).execute()
        }

    }

    private fun submitForm(codeText: String) {
        val phoneText = editPhone!!.text.toString()

        if (phoneText.isEmpty()){
            editPhone!!.error = "This field is required"
        }
        else{
            relativeProgress!!.visibility = View.VISIBLE

            LoginAsync(phoneText,codeText).execute()
        }

    }

    private fun pinSetDialog() {
        val dialog = Dialog(this@LoginActivity, R.style.Dialog)
        dialog.setContentView(R.layout.custom_set_up_pin)
        dialog.setTitle("Set Up PIN...")
        dialog.setCanceledOnTouchOutside(false)
        dialog.findViewById<Button>(R.id.buttonVerifyPin).setOnClickListener {
            startActivity(Intent(this@LoginActivity, VerifyPinActivity::class.java))

        }
        progressDialog = dialog.findViewById(R.id.relativeProgress)

        dialog.findViewById<Button>(R.id.buttonSetupPin).setOnClickListener {
            progressDialog!!.visibility = View.VISIBLE
            GetOTP(this, null).execute()
        }
        dialog.show()

        return
    }

    @SuppressLint("StaticFieldLeak")
    inner class AuthenticateAsync(private var codeText: String): AsyncTask<Void, Int, String>() {
        override fun onPreExecute() {
            super.onPreExecute()
            buttonSubmit!!.isEnabled = false
        }
        override fun doInBackground(vararg p0: Void?): String? {
            val map = HashMap<String, Any?>()
            map["user_code"] =codeText
            val url = Const.FEWCHORE_URL+"checkcode"
            try {
                return HttpUtility.sendPostRequest(url, map)
            }catch (e: Exception){}
            return null
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if (result != null){
                relativeProgress!!.visibility = View.GONE
                buttonSubmit!!.isEnabled = true
                val gson = Gson()

                try{
                    val type = object : TypeToken<ResponseStringModel>() {}.type
                    val userModel = gson.fromJson<ResponseStringModel>(result, type)
                    if (userModel.status =="true") {
                        linearPhone!!.visibility = View.VISIBLE
                        buttonSubmit!!.text = getString(R.string.login)
                        linearCode!!.visibility = View.GONE
                        buttonSubmit!!.setOnClickListener {
                            submitForm(codeText)
                        }
                    }
                    else{
                        appUtils!!.showAlert(userModel.msg!!)
                    }

                }catch (e: Exception){
                    if (e is IllegalStateException)
                        Toast.makeText(this@LoginActivity, "Please check your Internet connection",Toast.LENGTH_SHORT).show()
                }
            }
        }

    }


    @SuppressLint("StaticFieldLeak")
    inner class LoginAsync(private var phoneText: String, private var codeText: String):
            AsyncTask<Void, Int, String>() {
        override fun doInBackground(vararg p0: Void?): String? {
            val url = Const.FEWCHORE_URL+"login"
            val map = HashMap<String, Any?>()
            map["user_code"] =codeText
            map["user_phone"] = phoneText
            try {
                return HttpUtility.sendPostRequest(url, map)
            }catch (e: Exception){}
            return null
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if (result != null){
                relativeProgress!!.visibility = View.GONE

                val gson = Gson()

                try{
                    val type = object : TypeToken<LoginModel>() {}.type
                    val userModel = gson.fromJson<LoginModel>(result, type)
                    if (userModel != null) {
                        if (userModel.status) {
                            val sharedPreferences = SharedPreferenceUtil
                            sharedPreferences.save(this@LoginActivity, userModel.userDetails.userId, PREF_USERID)
                            sharedPreferences.save(this@LoginActivity, userModel.userDetails.userAddress, PREF_ADDRESS)
                            if (userModel.userDetails.userBvnName != null)
                                sharedPreferences.save(this@LoginActivity, userModel.userDetails.userBvnName, PREF_FULLNAME)
                            sharedPreferences.save(this@LoginActivity, userModel.userDetails.userCode, PREF_CODE)
                            sharedPreferences.save(this@LoginActivity, userModel.userDetails.userContactperson, PREF_CONTACT_PERSON)
                            sharedPreferences.save(this@LoginActivity, userModel.userDetails.userLimit, PREF_LIMIT)
                            sharedPreferences.save(this@LoginActivity, userModel.userDetails.userPhone, PREF_PHONENUMBER)
                            sharedPreferences.save(this@LoginActivity, userModel.userDetails.userLoanstatus, PREF_LOAN_STATUS)
                            sharedPreferences.save(this@LoginActivity, userModel.userDetails.userTerritory, PREF_TERRITORY)


                            if (userModel.image != null)
                                sharedPreferences.save(this@LoginActivity, userModel.image, PREF_PROFILE)
                            sharedPreferences.save(this@LoginActivity, userModel.userDetails.userFullname, PREF_BUISNESS_NAME)
                            if(userModel.userDetails.userEmail != null)
                                sharedPreferences.save(this@LoginActivity, userModel.userDetails.userEmail ,PREF_EMAIL)
                            else {
                                val emailText = AppUtils.getMyCode(this@LoginActivity)+"@fewchore.com"
                                sharedPreferences.save(this@LoginActivity, emailText, PREF_EMAIL)
                            }
                            pinSetDialog()
                        }else{
                            appUtils!!.showAlert(userModel.msg)
                        }
                    }
                }catch (e:Exception) {
                    if (e is IllegalStateException) {
                        Log.i("LOGIN", "Please check your network connection")
                    }
                }
            }
        }

    }


}






