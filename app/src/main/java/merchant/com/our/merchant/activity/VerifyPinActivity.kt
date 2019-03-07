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
import merchant.com.our.merchant.model.ResponseStringModel
import merchant.com.our.merchant.R
import merchant.com.our.merchant.api.GetOTP
import merchant.com.our.merchant.helper.*
import merchant.com.our.merchant.helper.AppUtils.Companion.PREF_PIN
import merchant.com.our.merchant.listener.PinTextListener
import merchant.com.our.merchant.listener.ResponseListener
import java.io.IOException

class VerifyPinActivity : AppCompatActivity(), PinTextListener,ResponseListener {

    private var sharedPreference: SharedPreferenceUtil? = null
    private var pinChar1: EditText?= null
    private var pinChar2: EditText?= null
    private var pinChar3: EditText?= null
    private var pinChar4: EditText?= null
    private var code: String ?= null
    private var appUtils : AppUtils ?= null
    private var relativeProgress: RelativeLayout?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_pin)

        sharedPreference = SharedPreferenceUtil
        appUtils = AppUtils(this)
        GetOTP.listener = this

        pinChar1 = findViewById(R.id.pinChar1)
        pinChar2 = findViewById(R.id.pinChar2)
        pinChar3 = findViewById(R.id.pinChar3)
        pinChar4 = findViewById(R.id.pinChar4)
        relativeProgress = findViewById(R.id.relativeProgress)

        pinChar1!!.addTextChangedListener(PinFormatter(pinChar2, this))
        pinChar2!!.addTextChangedListener(PinFormatter(pinChar3, this))
        pinChar3!!.addTextChangedListener(PinFormatter(pinChar4, this))
        pinChar4!!.addTextChangedListener(PinFormatter(pinChar1, this))

        findViewById<TextView>(R.id.forgotten).setOnClickListener {
            if (!AppUtils.getMyUserId(this@VerifyPinActivity).isNullOrEmpty()) {
                relativeProgress!!.visibility = View.VISIBLE
                GetOTP(this,null).execute()
            }
            else{
                val dialog = Dialog(this)
                dialog.setContentView(R.layout.dialog_phone)
                val phone  = dialog.findViewById<EditText>(R.id.ediPhone)

                dialog.findViewById<Button>(R.id.buttonSubmit).setOnClickListener {
                    val phoneText = phone.text.toString()
                    dialog.dismiss()
                    GetOTP(this,phoneText).execute()

                }
                dialog.show()
            }
          //  startActivity(Intent(this@VerifyPinActivity, CreatePinActivity::class.java))
        }
        findViewById<TextView>(R.id.support).setOnClickListener {
            supportForm()
        }
    }
    override fun idListener(id: String, msg: String?) {
        if (id == "true") {
            relativeProgress!!.visibility = View.GONE
            val message = "An OTP has been sent to your registered Phone number. Insert OTP in the space provided to complete your Mobile PIN setup."
            val localBuilder = AlertDialog.Builder(this)
            localBuilder.setMessage(message)
            localBuilder.setNeutralButton(R.string.ok) { _, _ ->
                startActivity(Intent(this@VerifyPinActivity, CreatePinActivity::class.java))
            }
            localBuilder.create().show()
        }else
            appUtils!!.showAlert(msg!!)

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

    override fun onTextChanged() {
        val str1 = pinChar1!!.text.toString()
        val str2 = pinChar2!!.text.toString()
        val str3 = pinChar3!!.text.toString()
        val str4 = pinChar4!!.text.toString()

        if (str1.isNotEmpty() && str2.isNotEmpty() && str3.isNotEmpty() && str4.isNotEmpty() ) {
            code = str1 + str2 + str3 + str4


            if (code.isNullOrEmpty()) {
                pinChar1!!.error = "This field is required"
            } else {
                VerifyPinAsync(code!!).execute()
            }
        }    }

    @SuppressLint("StaticFieldLeak")
    inner class VerifyPinAsync (private var pinText: String) : AsyncTask<Void, Int, String>() {
        internal var userId = AppUtils.getMyUserId(this@VerifyPinActivity)

        override fun onPreExecute() {
            super.onPreExecute()
            relativeProgress!!.visibility = View.VISIBLE
        }

        override fun doInBackground(vararg voids: Void): String? {
            val url = Const.FEWCHORE_URL + "verifypin"
            val userId = AppUtils.getMyUserId(this@VerifyPinActivity)
            try {
                val map = HashMap<String, Any?>()
                map["user_pin"] = pinText
                map["user_id"] = userId
                return HttpUtility.sendPostRequest(url, map)
            } catch (e: IOException) {
                e.printStackTrace()
            }

            return null
        }

        override fun onPostExecute(s: String?) {

            super.onPostExecute(s)
            if (s != null) {
                appUtils = AppUtils(this@VerifyPinActivity)
                try {
                    val gson = Gson()
                    val responseMessage = gson.fromJson<ResponseStringModel>(s, ResponseStringModel::class.java)

                    if (responseMessage.status =="true") {
                        relativeProgress!!.visibility = View.GONE
                        sharedPreference!!.save(this@VerifyPinActivity, "1", AppUtils.PREF_IS_LOGGEDIN)
                        sharedPreference!!.save(this@VerifyPinActivity, pinText, PREF_PIN)
                        startActivity(Intent(this@VerifyPinActivity, HomeActivity::class.java))
                        pinChar1!!.setText("")
                        pinChar2!!.setText("")
                        pinChar3!!.setText("")
                        pinChar4!!.setText("")

                    } else {
                        val msg = responseMessage.msg
                        val builder = AlertDialog.Builder(this@VerifyPinActivity)
                        builder.setMessage(msg)
                        builder.setNeutralButton(R.string.ok) { _, _ ->
                            relativeProgress!!.visibility = View.GONE }

                        builder.create().show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@VerifyPinActivity, "Please check your network connection", Toast.LENGTH_LONG).show()

                    Log.i("ERROR MESSAGE", e.javaClass.simpleName)
                }

            }
        }
    }

}
