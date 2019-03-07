package merchant.com.our.merchant.api

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import merchant.com.our.merchant.helper.Const
import merchant.com.our.merchant.helper.AppUtils
import merchant.com.our.merchant.helper.HttpUtility
import merchant.com.our.merchant.listener.ActiveLoanListener
import merchant.com.our.merchant.model.ActiveLoanModel

class ActiveLoanAsync(private var context: Context): AsyncTask<Void, Int, String>(){
    companion object {
        var activeLoanListener: ActiveLoanListener?= null
    }
    override fun doInBackground(vararg p0: Void?): String {
        val userId = AppUtils.getMyUserId(context)
        val url = Const.FEWCHORE_URL+"activeloan/"+userId
        return HttpUtility.getRequest(url)
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        if (!result.isNullOrEmpty()){
            val gson = Gson()
            try{
                val type= object :TypeToken<ActiveLoanModel>(){}.type
                val activeLoan = gson.fromJson<ActiveLoanModel>(result, type)
                if (activeLoan.status =="true"){
                    val details=  activeLoan.activeloan
                    activeLoanListener!!.activeLoanListener(activeLoan, null, activeLoan.status!!)
                }else{
                    activeLoanListener!!.activeLoanListener(null,activeLoan.msg, activeLoan.status!!)
                }
            }catch (e: Exception){
                Log.i("ERROR MESSAGE", e.javaClass.simpleName)
            }
        }
    }
}