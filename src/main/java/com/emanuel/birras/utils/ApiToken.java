package com.emanuel.birras.utils;

public class ApiToken {

    private String token;

    public ApiToken(String token) {
        this.setToken(token);
    }
    
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
