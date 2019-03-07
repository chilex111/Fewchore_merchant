package merchant.com.our.merchant.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import merchant.com.our.merchant.helper.Const
import merchant.com.our.merchant.helper.HttpUtility
import merchant.com.our.merchant.model.ResponseStringModel
import merchant.com.our.merchant.R
import merchant.com.our.merchant.helper.AppUtils
import merchant.com.our.merchant.helper.GMailSender
import merchant.com.our.merchant.view.CircleImageView
import java.io.ByteArrayOutputStream

class CreatePinActivity : AppCompatActivity() {
    private var profile: CircleImageView? = null
    private var textPin: EditText? = null
    private var confirmPin: EditText? = null
    private var editOTP: EditText? = null

    private var TAKE_PHOTO_REQUEST = 14
    private var photo1: String? = null
    private var appUtils : AppUtils ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_pin)
        profile = findViewById(R.id.imageAgentPhoto)
        textPin = findViewById(R.id.editPin)
        confirmPin = findViewById(R.id.editRe_Pin)
        editOTP = findViewById(R.id.editOtp)
        appUtils = AppUtils(this)
        findViewById<TextView>(R.id.support).setOnClickListener {
            supportForm()
        }

        findViewById<Button>(R.id.buttonSubmit).setOnClickListener {
            submitForm()

        }

        findViewById<ImageButton>(R.id.photoButton).setOnClickListener {
            //showDialog()
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.CAMERA),
                        TAKE_PHOTO_REQUEST)
            }else{
                val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(takePictureIntent, TAKE_PHOTO_REQUEST)
            }
        }

    }

    private fun submitForm() {
        val pinText = textPin!!.text.toString()
        val confirmPinText = confirmPin!!.text.toString()
        val otpText = editOTP!!.text.toString()
        if (otpText.isEmpty()){
            editOTP!!.error = "This field is required"
        }
        if (pinText.isEmpty()){
            textPin!!.error = "This field is required"
        }
        if(pinText != confirmPinText){
            confirmPin!!.error = "Your pin is not a match"
        }
        if (photo1.isNullOrEmpty()){
            appUtils!!.showAlert("Please add a profile picture")
        }
        else{
            CreatePinAsync(pinText, otpText).execute()
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

    @SuppressLint("StaticFieldLeak")
    inner class CreatePinAsync(private var pinText: String, private var otpText: String): AsyncTask<Void, Int, String>() {
        override fun doInBackground(vararg p0: Void?): String {

            val map  = HashMap<String, Any?>()
            val userId = AppUtils.getMyUserId(this@CreatePinActivity)
            map["user_id"] = userId
            map["user_pin"] = pinText
            map["user_image"] = photo1
            map["otp"] =otpText
            val url  = Const.FEWCHORE_URL+"addpin"
            return HttpUtility.sendPostRequest(url, map)
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if (result != null){
                val gson = Gson()

                try{
                    val type = object : TypeToken<ResponseStringModel>() {}.type
                    val userModel = gson.fromJson<ResponseStringModel>(result, type)
                    if (userModel != null) {
                        if (userModel.status =="true") {
                            startActivity(Intent(this@CreatePinActivity, HomeActivity::class.java))
                        }else{
                            appUtils!!.showAlert(userModel.msg!!)
                        }
                    }

                }catch (e: Exception){
                    appUtils!!.showAlert("Please check your network connection")
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            TAKE_PHOTO_REQUEST -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(takePictureIntent, TAKE_PHOTO_REQUEST)

                } else {
                    ActivityCompat.requestPermissions(this,
                            arrayOf(Manifest.permission.CAMERA),
                            TAKE_PHOTO_REQUEST) }
                return
            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)

        try {
            val profileImg = data.extras.get("data") as Bitmap

            profile!!.setImageBitmap(profileImg)

            val bao = ByteArrayOutputStream()
            profileImg.compress(Bitmap.CompressFormat.PNG, 90, bao)
            val ba = bao.toByteArray()

            photo1 = Base64.encodeToString(ba, Base64.DEFAULT)
        } catch (e: Exception) {
            e.printStackTrace()
        }


    }

}
