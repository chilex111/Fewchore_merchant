
package merchant.com.our.merchant.model;

import com.google.gson.annotations.Expose;

@SuppressWarnings("unused")
public class UserLoanStatusModel {

    @Expose
    private Boolean status;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

}
