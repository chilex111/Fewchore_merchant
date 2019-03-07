package merchant.com.our.merchant.api

import android.annotation.SuppressLint
import android.content.Context
import android.os.AsyncTask
import android.util.Log
import com.google.gson.Gson
import merchant.com.our.merchant.helper.Const
import merchant.com.our.merchant.helper.HttpUtility
import merchant.com.our.merchant.local_database.LocalDatabase
import merchant.com.our.merchant.model.BankListModel

@SuppressLint("StaticFieldLeak")
class BankListAsync(private val context: Context) : AsyncTask<Void, Int, String>() {

    override fun doInBackground(vararg p0: Void?): String? {
        val url = Const.FEWCHORE_URL+"allbanks"
        try {
            return HttpUtility.getRequest(url)
        }catch (e:Exception){

        }
                return null
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        if (result != null){
            try {
                val gson = Gson()
                val bankList = gson.fromJson<BankListModel>( result, BankListModel::class.java)
                if (bankList!= null){
                    val banks = bankList.result
                    val localDatabase = LocalDatabase(context)
                    localDatabase.insertBank(banks)
                }
            }catch (e: Exception){

                Log.i("ERROR MESSAGE", e.javaClass.simpleName)
            }


        }
    }
}