package merchant.com.our.merchant.listener

import merchant.com.our.merchant.model.ProfileModel

interface ProfileListener {
    fun profileListener(profileModel: ProfileModel, status: String )
}