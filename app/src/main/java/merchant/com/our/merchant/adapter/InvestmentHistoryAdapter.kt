package merchant.com.our.merchant.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.Adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import merchant.com.our.merchant.R
import merchant.com.our.merchant.model.Activeloan
import java.text.NumberFormat
import java.util.*


class LoanHistoryAdapter(private val historyModelList: List<Activeloan>?, private val context: Context)
    : Adapter<LoanHistoryAdapter.HomeViewHolder>() {
    override fun getItemCount(): Int {
        return  historyModelList!!.size
    }

    companion object {
        //  var idlistener: IdListener? = null
    }
    inner class HomeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var cardView : CardView = itemView.findViewById(R.id.cardHolder)
        val textTotalPayBack: TextView = itemView.findViewById(R.id.textTotalPayBack)
        var textLoan : TextView = itemView.findViewById(R.id.textLoanAmt)
        var textInterest: TextView = itemView.findViewById(R.id.textInterest)
        // var seemore : Button = itemView.findViewById(R.id.buttonDetails)


    }

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(paramViewGroup: ViewGroup, paramInt: Int): HomeViewHolder {
        return HomeViewHolder(LayoutInflater.from(paramViewGroup.context)
                .inflate(R.layout.custome_loan_history, paramViewGroup, false))
    }
    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {


        val valie  = historyModelList!![position].loanAmount.toDouble()
        val convertedVal = NumberFormat.getNumberInstance(Locale.US).format(valie)
        val loanText  ="₦$convertedVal"
        holder.textLoan.text=loanText


        val i =  historyModelList[position].loanInterest

        if (historyModelList[position].loanInterest.contains("0.")){
            val interest =i.replace("0.","")
            val newInterest  = "$interest kobo"
            holder.textInterest.text =newInterest
        }else{
            val newInterest  ="₦"+historyModelList[position].loanInterest
            holder.textInterest.text =newInterest
        }

        val pay_back = "₦"+historyModelList[position].loanTotalpayback
        holder.textTotalPayBack.text=pay_back

        when {
            historyModelList[position].statusTitle =="disbursed" -> {
                holder.cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.green))
            }
        }

    }

}
