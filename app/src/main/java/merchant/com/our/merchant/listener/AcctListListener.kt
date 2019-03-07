package merchant.com.our.merchant.listener

import merchant.com.our.merchant.model.BankDetail


interface AcctListListener {
    fun acctDetailsListener(bankDetail: List<BankDetail>?, msg: String)
}
