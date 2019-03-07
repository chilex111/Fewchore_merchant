
package merchant.com.our.merchant.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class ProfileModel {

    @SerializedName("days_over_due")
    private Long daysOverDue;
    @SerializedName("disbursement_date")
    private String disbursementDate;
    @Expose
    private String image;
    @SerializedName("loan_due_date")
    private String loanDueDate;
    @SerializedName("outstanding_loan")
    private Double outstandingLoan;
    @Expose
    private Msg msg;
    @Expose
    private String status;

    public Long getDaysOverDue() {
        return daysOverDue;
    }

    public void setDaysOverDue(Long daysOverDue) {
        this.daysOverDue = daysOverDue;
    }

    public String getDisbursementDate() {
        return disbursementDate;
    }

    public void setDisbursementDate(String disbursementDate) {
        this.disbursementDate = disbursementDate;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLoanDueDate() {
        return loanDueDate;
    }

    public void setLoanDueDate(String loanDueDate) {
        this.loanDueDate = loanDueDate;
    }

    public Msg getMsg() {
        return msg;
    }

    public void setMsg(Msg msg) {
        this.msg = msg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getOutstandingLoan() {
        return outstandingLoan;
    }
}
