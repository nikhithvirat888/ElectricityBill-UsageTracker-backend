package com.anurag.cse.dto;

import jakarta.validation.constraints.*;

public class ProfileRequest {
    @NotBlank(message = "Name is required") private String name;
    @NotBlank(message = "Consumer number is required") private String consumerNumber;
    private String address;
    private String newPassword; // optional

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getConsumerNumber() { return consumerNumber; }
    public void setConsumerNumber(String c) { this.consumerNumber = c; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getNewPassword() { return newPassword; }
    public void setNewPassword(String p) { this.newPassword = p; }
}