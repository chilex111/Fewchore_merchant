package merchant.com.our.merchant.api

import android.annotation.SuppressLint
import android.content.Context
import android.os.AsyncTask
import android.util.Log
import com.google.gson.Gson
import merchant.com.our.merchant.helper.Const
import merchant.com.our.merchant.helper.HttpUtility
import merchant.com.our.merchant.helper.AppUtils
import merchant.com.our.merchant.listener.CardLoanListener
import merchant.com.our.merchant.model.AccessCodeModel

@SuppressLint("StaticFieldLeak")
class AddCardAsync( private val context: Context) : AsyncTask<Void, Int, String>() {
    companion object {
        var cardListener: CardLoanListener?= null
    }
    override fun doInBackground(vararg p0: Void?): String? {
        try {

            val url = Const.FEWCHORE_URL+"addcard"
            val userId = AppUtils.getMyUserId(context)
            val map = HashMap<String, Any?>()

            map["card_user"] = userId!!

            return HttpUtility.sendPostRequest(url,map)
        }catch (e: Exception){

        }
        return null
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        if (result != null){
            val gson = Gson()
            try{
                val codeModel = gson.fromJson<AccessCodeModel>(result, AccessCodeModel::class.java)
                if (codeModel != null){
                    if (codeModel.status!!){
                        cardListener!!.accessCodeListener(codeModel.msg,codeModel.status!!)

                    }else{
                        cardListener!!.accessCodeListener(null, codeModel.status!!)
                    }
                }
            } catch (e: Exception){
                Log.i("ERROR MESSAGE", e.javaClass.simpleName)
            }
        }
    }
}