/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.prodesp.webapp01;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Marcelo
 */
public class LoginProvider {
    public static final int MICROSOFT = 1;
    public static final int GOOGLE = 2;
    public static final int FACEBOOK = 3;
    public static final int WSO2 = 4;
    public static final int SP_GOV = 5;
    
    public static boolean verifyRequestType(HttpServletRequest request, int type) {
        String par = request.getParameter("t");
        if(par == null || par.trim().isEmpty())
            return false;
        try {
            return Integer.parseInt(par) == type;
        } catch(NumberFormatException e) {
            return false;
        }
    }
}