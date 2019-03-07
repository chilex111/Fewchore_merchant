
package merchant.com.our.merchant.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Msg {

    @SerializedName("current_limit")
    private String currentLimit;
    @SerializedName("user_address")
    private String userAddress;
    @SerializedName("user_bvn_name")
    private String userBvnName;
    @SerializedName("user_code")
    private String userCode;
    @SerializedName("user_contactperson")
    private String userContactperson;
    @SerializedName("user_created")
    private String userCreated;
    @SerializedName("user_fullname")
    private String userFullname;
    @SerializedName("user_id")
    private String userId;
    @SerializedName("user_limit")
    private String userLimit;
    @SerializedName("user_phone")
    private String userPhone;
    @SerializedName("user_pin")
    private String userPin;
    @SerializedName("user_territory")
    private String userTerritory;

    public String getCurrentLimit() {
        return currentLimit;
    }

    public void setCurrentLimit(String currentLimit) {
        this.currentLimit = currentLimit;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getUserBvnName() {
        return userBvnName;
    }

    public void setUserBvnName(String userBvnName) {
        this.userBvnName = userBvnName;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getUserContactperson() {
        return userContactperson;
    }

    public void setUserContactperson(String userContactperson) {
        this.userContactperson = userContactperson;
    }

    public String getUserCreated() {
        return userCreated;
    }

    public void setUserCreated(String userCreated) {
        this.userCreated = userCreated;
    }

    public String getUserFullname() {
        return userFullname;
    }

    public void setUserFullname(String userFullname) {
        this.userFullname = userFullname;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserLimit() {
        return userLimit;
    }

    public void setUserLimit(String userLimit) {
        this.userLimit = userLimit;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserPin() {
        return userPin;
    }

    public void setUserPin(String userPin) {
        this.userPin = userPin;
    }

    public String getUserTerritory() {
        return userTerritory;
    }

    public void setUserTerritory(String userTerritory) {
        this.userTerritory = userTerritory;
    }

}
