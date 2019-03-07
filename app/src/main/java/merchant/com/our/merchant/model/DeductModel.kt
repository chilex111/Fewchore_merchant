package merchant.com.our.merchant.model

import com.google.gson.annotations.Expose

class DeductModel {

    @Expose
    var data: PaystackData? = null
    @Expose
    var message: String? = null
    @Expose
    var status: Boolean? = null

}
