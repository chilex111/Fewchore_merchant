
package merchant.com.our.merchant.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class LoginModel {

    @Expose
    private String msg;
    @Expose
    private Boolean status;

    @SerializedName("image")
    private String image;
    @SerializedName("user_details")
    private UserDetails userDetails;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public UserDetails getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserDetails userDetails) {
        this.userDetails = userDetails;
    }

    public String getImage() {
        return image;
    }
}
