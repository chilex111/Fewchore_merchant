
package merchant.com.our.merchant.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Faq {

    @SerializedName("faq_a")
    private String faqA;
    @SerializedName("faq_created")
    private String faqCreated;
    @SerializedName("faq_id")
    private String faqId;
    @SerializedName("faq_modified")
    private String faqModified;
    @SerializedName("faq_q")
    private String faqQ;

    public String getFaqA() {
        return faqA;
    }

    public void setFaqA(String faqA) {
        this.faqA = faqA;
    }

    public String getFaqCreated() {
        return faqCreated;
    }

    public void setFaqCreated(String faqCreated) {
        this.faqCreated = faqCreated;
    }

    public String getFaqId() {
        return faqId;
    }

    public void setFaqId(String faqId) {
        this.faqId = faqId;
    }

    public String getFaqModified() {
        return faqModified;
    }

    public void setFaqModified(String faqModified) {
        this.faqModified = faqModified;
    }

    public String getFaqQ() {
        return faqQ;
    }

    public void setFaqQ(String faqQ) {
        this.faqQ = faqQ;
    }

}
