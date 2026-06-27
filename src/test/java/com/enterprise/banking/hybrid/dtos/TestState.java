package com.enterprise.banking.hybrid.dtos;

// Pure POJO: No static variables. Thread safety is handled by the Test class instantiating this object.
public class TestState {
    
    private String customerId;
    private String username;
    private String password;
    private String generatedAccountId;

    public String getCustomerId() { 
        return customerId; 
    }
    
    public void setCustomerId(String customerId) { 
        this.customerId = customerId; 
    }

    public String getUsername() { 
        return username; 
    }
    
    public void setUsername(String username) { 
        this.username = username; 
    }

    public String getPassword() { 
        return password; 
    }
    
    public void setPassword(String password) { 
        this.password = password; 
    }

    public String getGeneratedAccountId() { 
        return generatedAccountId; 
    }
    
    public void setGeneratedAccountId(String generatedAccountId) { 
        this.generatedAccountId = generatedAccountId; 
    }
}