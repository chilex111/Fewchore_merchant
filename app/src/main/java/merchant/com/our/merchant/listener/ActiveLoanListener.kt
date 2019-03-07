package merchant.com.our.merchant.listener

import merchant.com.our.merchant.model.ActiveLoanModel

interface ActiveLoanListener {
    fun activeLoanListener(activeLoan: ActiveLoanModel?, msg: String?, status: String)
}
