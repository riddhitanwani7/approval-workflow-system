package com.example.demo.dto;

public class JwtResponse {
    
    private String token;
    private String tokenType = "Bearer";
    private Long id;
    private String name;
    private String email;
    private String position;
    
    public JwtResponse(String token, Long id, String name, String email, String position) {
        this.token = token;
        this.id = id;
        this.name = name;
        this.email = email;
        this.position = position;
    }
    
    // Getters and setters
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    public String getTokenType() {
        return tokenType;
    }
    
    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPosition() {
        return position;
    }
    
    public void setPosition(String position) {
        this.position = position;
    }
} 