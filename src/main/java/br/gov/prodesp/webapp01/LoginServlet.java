/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.prodesp.webapp01;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.common.OAuthProviderType;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.ResponseType;

/**
 *
 * @author jfascio
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {
    
    private static final int DEFAULT_LOGIN_TIMEOUT = 10; // em minutos

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            if(LoginProvider.verifyRequestType(request, LoginProvider.MICROSOFT))
                loginMicrosoft(request, response);
            else if(LoginProvider.verifyRequestType(request, LoginProvider.GOOGLE))
                loginGoogle(request, response);
            else if(LoginProvider.verifyRequestType(request, LoginProvider.FACEBOOK))
                loginFacebook(request, response);
            else if(LoginProvider.verifyRequestType(request, LoginProvider.WSO2))
                loginWSO2(request, response);
            else if(LoginProvider.verifyRequestType(request, LoginProvider.SP_GOV))
                loginSpGov(request, response);
            else
                throw new ServletException("Parâmetro 't' não existente ou com valor não suportado.");
        } catch (OAuthSystemException ex) {
            Logger.getLogger(LoginServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void loginMicrosoft(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, OAuthSystemException {
        
        AuthenticationState state = new AuthenticationState(LoginProvider.MICROSOFT);
        Cache cache = Cache.getInstance();
        cache.put(state.getSession(), state, DEFAULT_LOGIN_TIMEOUT);
        // TODO: implementar cache distribuído com TTL do state e checar no servlet de autorização.
        OAuthClientRequest oauthReq = OAuthClientRequest
                .authorizationLocation("https://login.microsoftonline.com/97ea4bf7-1360-4be4-9925-ae57d82346ef/oauth2/authorize")
                .setScope("openid")
                .setResponseType("code")
                .setClientId("da6a990e-fc37-485d-989a-229a19f80c47")
                .setRedirectURI("http://localhost:8080/webapp01/authorize")
                .setState(state.getSession())
                .buildQueryMessage();
        
        Logger.getLogger(LoginServlet.class.getName()).log(Level.INFO, "Redirect to MICROSOFT authorization URL:" + oauthReq.getLocationUri());
        response.sendRedirect(oauthReq.getLocationUri());
    }
    
    private void loginGoogle(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, OAuthSystemException {
            
        AuthenticationState state = new AuthenticationState(LoginProvider.GOOGLE);
        Cache cache = Cache.getInstance();
        cache.put(state.getSession(), state, DEFAULT_LOGIN_TIMEOUT);
        // TODO: implementar cache distribuído com TTL do state e checar no servlet de autorização.
        OAuthClientRequest oauthReq = OAuthClientRequest
                .authorizationProvider(OAuthProviderType.GOOGLE)
                .setScope("openid")
                .setResponseType("code")
                .setClientId("321056030583-c26ieiq50cs96vvrlql52ptjc640qip9.apps.googleusercontent.com")
                .setRedirectURI("http://localhost:8080/webapp01/authorize")
                .setState(state.getSession())
                .buildQueryMessage();
        
        Logger.getLogger(LoginServlet.class.getName()).log(Level.INFO, "Redirect to GOOGLE authorization URL:" + oauthReq.getLocationUri());
        response.sendRedirect(oauthReq.getLocationUri());
    }
    
    private void loginFacebook(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, OAuthSystemException {
    
        AuthenticationState state = new AuthenticationState(LoginProvider.FACEBOOK);
        Cache cache = Cache.getInstance();
        cache.put(state.getSession(), state, DEFAULT_LOGIN_TIMEOUT);
        // TODO: implementar cache distribuído com TTL do state e checar no servlet de autorização.
        OAuthClientRequest oauthReq = OAuthClientRequest
                .authorizationProvider(OAuthProviderType.FACEBOOK)
                //.setScope("openid")  // Facebook não suporta OpenID Connect
                .setScope("email")
                .setResponseType("code")
                .setClientId("298440033821421")
                .setRedirectURI("http://localhost:8080/webapp01/authorize")
                .setState(state.getSession())
                .buildQueryMessage();
        
        Logger.getLogger(LoginServlet.class.getName()).log(Level.INFO, "Redirect to FACEBOOK authorization URL:" + oauthReq.getLocationUri());
        response.sendRedirect(oauthReq.getLocationUri());
    }

    private void loginWSO2(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, OAuthSystemException {
    
        AuthenticationState state = new AuthenticationState(LoginProvider.WSO2);
        Cache cache = Cache.getInstance();
        cache.put(state.getSession(), state, DEFAULT_LOGIN_TIMEOUT);
        // TODO: implementar cache distribuído com TTL do state e checar no servlet de autorização.
        OAuthClientRequest oauthReq = OAuthClientRequest
                .authorizationLocation("https://10.199.101.201:9443/oauth2/authorize")
                .setScope("openid")
                .setResponseType("code")
                .setClientId("tYmcIZHnRfuXCtvDB6rUcn9AfgMa")
                .setRedirectURI("http://localhost:8080/webapp01/authorize")
                .setState(state.getSession())
                .buildQueryMessage();
        
        Logger.getLogger(LoginServlet.class.getName()).log(Level.INFO, "Redirect to WSO2 authorization URL:" + oauthReq.getLocationUri());
        response.sendRedirect(oauthReq.getLocationUri());
    }

    private void loginSpGov(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, OAuthSystemException {
    
        AuthenticationState state = new AuthenticationState(LoginProvider.SP_GOV);
        Cache cache = Cache.getInstance();
        cache.put(state.getSession(), state, DEFAULT_LOGIN_TIMEOUT);
        // TODO: implementar cache distribuído com TTL do state e checar no servlet de autorização.
        OAuthClientRequest oauthReq = OAuthClientRequest
                .authorizationLocation("http://www.govspauth.com.br/connect/authorize")
                .setScope("openid")
                .setResponseType("id_token")
                .setClientId("WebAppZe")
                .setRedirectURI("http://localhost:8080/webapp01/authorize")
                .setState(state.getSession())
                .buildQueryMessage();
        
        Logger.getLogger(LoginServlet.class.getName()).log(Level.INFO, "Redirect to WSO2 authorization URL:" + oauthReq.getLocationUri());
        response.sendRedirect(oauthReq.getLocationUri());
    }
        
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
