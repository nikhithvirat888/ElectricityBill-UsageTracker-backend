package com.anurag.cse.dto;

import jakarta.validation.constraints.*;

public class RegisterRequest {
    @NotBlank(message = "Name is required") private String name;
    @NotBlank(message = "Email is required") @Email(message = "Enter a valid email address") private String email;
    @NotBlank(message = "Password is required") @Size(min = 6, message = "Password must be at least 6 characters") private String password;
    @NotBlank(message = "Consumer number is required") private String consumerNumber;
    private String address;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getConsumerNumber() { return consumerNumber; }
    public void setConsumerNumber(String c) { this.consumerNumber = c; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
}