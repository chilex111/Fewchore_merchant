package merchant.com.our.merchant.model

import com.google.gson.annotations.SerializedName

class AccessCodeMsg {

    @SerializedName("access_code")
    var accessCode: String? = null
    @SerializedName("user_id")
    var userId: String? = null

}
