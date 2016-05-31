/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.prodesp.webapp01;

import java.util.UUID;

/**
 *
 * @author 147692
 */
public class AuthenticationState {
    private String session;
    private int provider;
    
    public AuthenticationState() {
    }
    
    public AuthenticationState(int providerType) {
        this.session = UUID.randomUUID().toString().replaceAll("-", "");
        this.provider = providerType;
    }
    
    public AuthenticationState(String session, int providerType) {
        this.session = session;
        this.provider = providerType;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public int getProvider() {
        return provider;
    }

    public void setProvider(int providerType) {
        this.provider = providerType;
    }
       
}
