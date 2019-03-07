package merchant.com.our.merchant.listener

import merchant.com.our.merchant.model.CardDetails

interface CardListListener {
    fun cardDetailsListener(cardDetails: MutableList<CardDetails>?, msg: String)
}
