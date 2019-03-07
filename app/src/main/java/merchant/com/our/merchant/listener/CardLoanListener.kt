package merchant.com.our.merchant.listener

import merchant.com.our.merchant.model.AccessCodeMsg

interface CardLoanListener {
    fun accessCodeListener(cardDetails: AccessCodeMsg?, status: Boolean)
}
