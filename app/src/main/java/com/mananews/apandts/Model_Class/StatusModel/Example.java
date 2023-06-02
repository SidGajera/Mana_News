
package com.mananews.apandts.Model_Class.StatusModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Example {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("request")
    @Expose
    private String request;
    @SerializedName("current_page")
    @Expose
    private String currentPage;
    @SerializedName("is_nextpage")
    @Expose
    private String isNextpage;
    @SerializedName("response")
    @Expose
    private StatusModelClass response;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(String currentPage) {
        this.currentPage = currentPage;
    }

    public String getIsNextpage() {
        return isNextpage;
    }

    public void setIsNextpage(String isNextpage) {
        this.isNextpage = isNextpage;
    }

    public StatusModelClass getResponse() {
        return response;
    }

    public void setResponse(StatusModelClass response) {
        this.response = response;
    }

}
