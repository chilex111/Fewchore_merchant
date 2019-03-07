package merchant.com.our.merchant.api

import android.content.Context
import android.os.AsyncTask
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import merchant.com.our.merchant.helper.Const
import merchant.com.our.merchant.helper.HttpUtility
import merchant.com.our.merchant.helper.SharedPreferenceUtil
import merchant.com.our.merchant.listener.AcctListListener
import merchant.com.our.merchant.helper.AppUtils
import merchant.com.our.merchant.model.AcctListModel

class AcctListAsync(private var context: Context): AsyncTask<Void, Int, String>(){
companion object {
    var listener
            : AcctListListener ?= null
    var sharedPrefernce : SharedPreferenceUtil?= null
}
    override fun doInBackground(vararg p0: Void?): String {
        val userId = AppUtils.getMyUserId(context)
        val url = Const.FEWCHORE_URL+"userbanks/"+userId
        return HttpUtility.getRequest(url)
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        if (!result.isNullOrEmpty()){
            val gson = Gson()
            sharedPrefernce = SharedPreferenceUtil
            try{
            val type= object :TypeToken<AcctListModel>(){}.type
            val activeLoan = gson.fromJson<AcctListModel>(result, type)
            if (activeLoan.status =="success"){
              val details=  activeLoan.bankDetails
                sharedPrefernce!!.save(context,"verified", AppUtils.PREF_BVN)
                listener!!.acctDetailsListener(details, activeLoan.status!!)
            }else{
                listener!!.acctDetailsListener(null, activeLoan.msg!!)
            }
        }catch (e: Exception){
                listener!!.acctDetailsListener(null, "Please check your internet connection")

            }
        }
    }
}