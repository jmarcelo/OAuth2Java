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
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.ResponseType;

/**
 *
 * @author jfascio
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

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
            if("1".equals(request.getParameter("t")))
                loginMicrosoft(request, response);
            else if("2".equals(request.getParameter("t")))
                loginGoogle(request, response);
            else if("3".equals(request.getParameter("t")))
                loginFacebook(request, response);
            else
                throw new ServletException("Parâmetro 't' não existente ou com valor não suportado.");
        } catch (OAuthSystemException ex) {
            Logger.getLogger(LoginServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void loginMicrosoft(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, OAuthSystemException {
        
        String stateUuid = UUID.randomUUID().toString().replaceAll("-", "");
        // TODO: implementar cache distribuído com TTL do state e checar no servlet de autorização.
        
        OAuthClientRequest oauthReq = OAuthClientRequest
                .authorizationLocation("https://login.microsoftonline.com/97ea4bf7-1360-4be4-9925-ae57d82346ef/oauth2/authorize")
                .setScope("openid")
                .setResponseType("code")
                .setClientId("da6a990e-fc37-485d-989a-229a19f80c47")
                .setRedirectURI("http://localhost:8080/webapp01/authorize")
                .setState(stateUuid)
                .buildQueryMessage();
        Logger.getLogger(LoginServlet.class.getName()).log(Level.INFO, oauthReq.getLocationUri());
        response.sendRedirect(oauthReq.getLocationUri());
    }
    
    private void loginGoogle(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, OAuthSystemException {
        
    }
    
    private void loginFacebook(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, OAuthSystemException {
        
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