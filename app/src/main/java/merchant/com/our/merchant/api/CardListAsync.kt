package merchant.com.our.merchant.api

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import merchant.com.our.merchant.helper.Const
import merchant.com.our.merchant.helper.HttpUtility
import merchant.com.our.merchant.helper.AppUtils
import merchant.com.our.merchant.listener.CardListListener
import merchant.com.our.merchant.model.CardListModel

class CardListAsync(var context: Context): AsyncTask<Void, Int, String>(){
    companion object {
        var cardListener: CardListListener?= null
    }
    override fun doInBackground(vararg p0: Void?): String {
        val userId = AppUtils.getMyUserId(context)
        val url = Const.FEWCHORE_URL+"usercards/"+userId
        return HttpUtility.getRequest(url)
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        if (!result.isNullOrEmpty()){
            val gson = Gson()
            try{
                val type= object :TypeToken<CardListModel>(){}.type
                val activeLoan = gson.fromJson<CardListModel>(result, type)
                if (activeLoan.status =="success"){
                    val details=  activeLoan.cardDetails

                    cardListener!!.cardDetailsListener(details, activeLoan.status!!)
                }else{
                    cardListener!!.cardDetailsListener(null, activeLoan.status!!)
                }
            } catch (e: Exception){
                Log.i("ERROR MESSAGE", e.javaClass.simpleName)
                cardListener!!.cardDetailsListener(null, "failed")

            }
        }
    }
}