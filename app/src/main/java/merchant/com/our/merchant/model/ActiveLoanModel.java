
package merchant.com.our.merchant.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class ActiveLoanModel {

    @Expose
    private List<Activeloan> activeloan;
    @SerializedName("outstanding_loan")
    private Double outstandingLoan;
    @Expose
    private String status;
    @Expose
    private String msg;

    public List<Activeloan> getActiveloan() {
        return activeloan;
    }

    public void setActiveloan(List<Activeloan> activeloan) {
        this.activeloan = activeloan;
    }

    public Double getOutstandingLoan() {
        return outstandingLoan;
    }

    public void setOutstandingLoan(Double outstandingLoan) {
        this.outstandingLoan = outstandingLoan;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }
}
