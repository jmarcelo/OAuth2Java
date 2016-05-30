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
    private String provider;
    
    public AuthenticationState() {
    }
    
    public AuthenticationState(String provider) {
        this.session = UUID.randomUUID().toString().replaceAll("-", "");
        this.provider = provider;
    }
    
    public AuthenticationState(String session, String provider) {
        this.session = session;
        this.provider = provider;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }
       
}
