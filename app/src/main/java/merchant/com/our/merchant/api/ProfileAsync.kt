package merchant.com.our.merchant.api

import android.annotation.SuppressLint
import android.content.Context
import android.os.AsyncTask
import android.support.v4.app.FragmentActivity
import android.util.Log
import android.view.View
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import merchant.com.our.merchant.R
import merchant.com.our.merchant.R.id.textName
import merchant.com.our.merchant.helper.AppUtils
import merchant.com.our.merchant.helper.Const
import merchant.com.our.merchant.helper.HttpUtility
import merchant.com.our.merchant.helper.SharedPreferenceUtil
import merchant.com.our.merchant.listener.ProfileListener
import merchant.com.our.merchant.model.ProfileModel
import java.lang.Exception
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class ProfileAsync(@SuppressLint("StaticFieldLeak") private var activity: Context) : AsyncTask<Void, Int, String>(){

    companion object {
        var profileListener: ProfileListener ?= null
    }
    override fun doInBackground(vararg p0: Void?): String {
        val userId= AppUtils.getMyUserId(activity)
        val url = Const.FEWCHORE_URL +"profile/"+userId
        return HttpUtility.getRequest(url)
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        if (result != null){
            val gson = Gson()
            val sharedPreferences = SharedPreferenceUtil
            try{
                val type = object : TypeToken<ProfileModel>() {}.type
                val userModel = gson.fromJson<ProfileModel>(result, type)
                if (userModel!= null) {
                    val maxLoan = AppUtils.getMyLimit(activity)
                    val decimalFormat = DecimalFormat("#,###.00")
                    val am = decimalFormat.format(java.lang.Double.parseDouble(maxLoan))

                    if (maxLoan != userModel.msg.userLimit)
                        sharedPreferences.save(activity, am, AppUtils.PREF_LIMIT)

                    val currentLimitValue = decimalFormat.format(java.lang.Double.parseDouble(userModel.msg.currentLimit))
                    sharedPreferences.save(activity, currentLimitValue, AppUtils.PREF_CURRENT_LIMIT)

                    profileListener!!.profileListener(userModel, userModel.status)
                }
            }catch (e: Exception){
                Log.i("PROFILE_ERror", e.message)
            }
        }
    }

}
