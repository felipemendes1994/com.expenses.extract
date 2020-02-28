package com.expenses.extract.utility;

import org.springframework.http.HttpStatus;

public class HttpCommonResponse {

    private HttpStatus Status;
    private String body;

    public HttpStatus getStatus() {
        return Status;
    }

    public void setStatus(HttpStatus status) {
        Status = status;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
