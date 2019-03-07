package merchant.com.our.merchant.api

import android.annotation.SuppressLint
import android.content.Context
import android.os.AsyncTask
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import merchant.com.our.merchant.helper.AppUtils
import merchant.com.our.merchant.helper.Const
import merchant.com.our.merchant.helper.HttpUtility
import merchant.com.our.merchant.listener.ResponseListener
import merchant.com.our.merchant.model.ResponseStringModel


@SuppressLint("StaticFieldLeak")
class GetOTP(private var context: Context, private var phoneText: String?) : AsyncTask<Void, Int, String>(){
    companion object {
        var listener : ResponseListener ?= null
    }
    override fun doInBackground(vararg p0: Void?): String? {
        val userId = AppUtils.getMyUserId(context)

        val url = Const.FEWCHORE_URL+"getotp"
        val map = HashMap<String, Any?>()

        return if (!phoneText.isNullOrEmpty()) {
            map["identifier"] = phoneText
            map["type"] = 2
            HttpUtility.sendPostRequest(url, map)
        }
        else{
            map["identifier"] = userId
            map["type"] = 1
            HttpUtility.sendPostRequest(url, map)

        }
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        if (!result.isNullOrEmpty()){
            val gson = Gson()

            try{
                val type = object : TypeToken<ResponseStringModel>() {}.type
                val userModel = gson.fromJson<ResponseStringModel>(result, type)
                if (userModel != null) {

                    if (userModel.status == "true" &&(userModel.sms_status =="ok")) {
                        listener!!.idListener(userModel.status!!, userModel.msg)
                    }else{
                        listener!!.idListener(userModel.status!!, userModel.msg)

                    }
                }
            }catch (e:Exception){

            }
        }
    }

}
