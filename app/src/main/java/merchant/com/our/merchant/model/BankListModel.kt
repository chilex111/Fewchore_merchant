package merchant.com.our.merchant.model

import com.google.gson.annotations.Expose

class BankListModel {

    @Expose
    var result: List<Banks>? = null
    @Expose
    var status: String? = null

}
