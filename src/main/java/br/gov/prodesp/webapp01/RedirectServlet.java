/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.prodesp.webapp01;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.GitHubTokenResponse;
import org.apache.oltu.oauth2.client.response.OAuthAuthzResponse;
import org.apache.oltu.oauth2.client.response.OAuthJSONAccessTokenResponse;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.apache.oltu.oauth2.common.utils.JSONUtils;

/**
 *
 * @author jfascio
 */
@WebServlet(name = "RedirectServlet", urlPatterns = {"/Redirect"})
public class RedirectServlet extends HttpServlet {

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

        try
        {
            // Obtém o code
            OAuthAuthzResponse oar = OAuthAuthzResponse.oauthCodeAuthzResponse(request);
            String code = oar.getCode();
            
            // Busca o token
            OAuthClientRequest oauthReq = OAuthClientRequest
                .tokenLocation("https://login.microsoftonline.com/97ea4bf7-1360-4be4-9925-ae57d82346ef/oauth2/token")
                .setClientId("da6a990e-fc37-485d-989a-229a19f80c47")
                .setClientSecret("Gk38t6ESu9WZx2rKCRcCNC/JI4HpTfBXOJmzocWWsYc=")
                .setRedirectURI("http://localhost:8080/webapp01-1.0-SNAPSHOT/Redirect")
                .setCode(code)
                .setGrantType(GrantType.AUTHORIZATION_CODE)
                .buildBodyMessage();
            
            Logger.getLogger(Teste1.class.getName()).log(Level.INFO, "Token request URL:" + oauthReq.getLocationUri());
            
            OAuthClient client = new OAuthClient(new URLConnectionClient());
            OAuthJSONAccessTokenResponse resp = client.accessToken(oauthReq);
                                    
            // ----------------------------------------------------------------
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();
            
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet RedirectServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet RedirectServlet at " + request.getContextPath() + "</h1>");
            out.println("<h2>Code: " + code + "</h2>");
            out.println("<h2>resp: " + resp + "</h2>");
            
//            out.println("<h2>Token : " + accessToken + "</h2>");
//            out.println("<h2>Expires: " + expiresIn + "</h2>");
            out.println("</body>");
            out.println("</html>");
            
        } catch(Exception ex) {
            
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();
            
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet RedirectServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Erro de execução</h1>");
            out.println("<h2>Exception: " + ex.getMessage() + "</h2>");
            out.println("<h3>Stack Trace</h3><pre>");
            ex.printStackTrace(out);
            out.println("</pre></body>");
            out.println("</html>");
            
            Logger.getLogger(Teste1.class.getName()).log(Level.SEVERE, null, ex);
        }
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
