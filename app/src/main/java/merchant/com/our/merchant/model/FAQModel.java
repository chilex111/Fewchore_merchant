
package merchant.com.our.merchant.model;

import com.google.gson.annotations.Expose;

import java.util.List;

@SuppressWarnings("unused")
public class FAQModel {

    @Expose
    private List<Faq> faq;
    @Expose
    private String status;

    public List<Faq> getFaq() {
        return faq;
    }

    public void setFaq(List<Faq> faq) {
        this.faq = faq;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
