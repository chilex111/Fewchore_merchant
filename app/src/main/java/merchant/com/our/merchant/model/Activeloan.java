
package merchant.com.our.merchant.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Activeloan {

    @SerializedName("loan_amount")
    private String loanAmount;
    @SerializedName("loan_id")
    private String loanId;
    @SerializedName("loan_interest")
    private String loanInterest;
    @SerializedName("loan_totalpayback")
    private String loanTotalpayback;
    @SerializedName("status_title")
    private String statusTitle;

    public String getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(String loanAmount) {
        this.loanAmount = loanAmount;
    }

    public String getLoanId() {
        return loanId;
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }

    public String getLoanInterest() {
        return loanInterest;
    }

    public void setLoanInterest(String loanInterest) {
        this.loanInterest = loanInterest;
    }

    public String getLoanTotalpayback() {
        return loanTotalpayback;
    }

    public void setLoanTotalpayback(String loanTotalpayback) {
        this.loanTotalpayback = loanTotalpayback;
    }

    public String getStatusTitle() {
        return statusTitle;
    }

    public void setStatusTitle(String statusTitle) {
        this.statusTitle = statusTitle;
    }

}
