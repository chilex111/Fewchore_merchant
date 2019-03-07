package merchant.com.our.merchant.model

import com.google.gson.annotations.Expose

class ResponseStringModel {
    @Expose
    var msg: String? = null
    @Expose
    var sms_status: String? = null
    @Expose
    var error_msg: String? = null
    @Expose
    var status: String? = null
    @Expose
    var loan_id: String? = null
    @Expose
    var name: String? = null

}
