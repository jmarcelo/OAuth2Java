/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.prodesp.webapp01;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
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
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.apache.oltu.oauth2.common.utils.JSONUtils;

/**
 *
 * @author jfascio
 */
@WebServlet(name = "AuthorizeServlet", urlPatterns = {"/authorize"})
public class AuthorizeServlet extends HttpServlet {

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
        
        String tokenEndpoint = "";
        String clientId = "";
        String clientSecret = "";

        try
        {
            // Obtém o code
            OAuthAuthzResponse oar = OAuthAuthzResponse.oauthCodeAuthzResponse(request);
            
            String code = oar.getCode();
            
            // Obtém do cache o 
            Cache cache = Cache.getInstance();
            AuthenticationState state = (AuthenticationState)cache.get(oar.getState());
            
            if(state.getProvider() == LoginProvider.MICROSOFT) {
                tokenEndpoint = "https://login.microsoftonline.com/97ea4bf7-1360-4be4-9925-ae57d82346ef/oauth2/token";
                clientId = "da6a990e-fc37-485d-989a-229a19f80c47";
                clientSecret = "";
            } else if(state.getProvider() == LoginProvider.GOOGLE) {
                tokenEndpoint = "https://www.googleapis.com/oauth2/v4/token";
                clientId = "321056030583-c26ieiq50cs96vvrlql52ptjc640qip9.apps.googleusercontent.com";
                clientSecret = "";
            } else if(state.getProvider() == LoginProvider.FACEBOOK) {
                tokenEndpoint = "https://graph.facebook.com/v2.3/oauth/access_token";
                clientId = "298440033821421";
                clientSecret = "";
            } else if(state.getProvider() == LoginProvider.WSO2) {
                tokenEndpoint = "https://10.199.101.201:9443/oauth2/token";
                clientId = "4569zP7OWmasQzYlrEqmjHZ_aVca";
                clientSecret = "aBQLfBOQfSOTjG94MJgf91nzV0sa";
            } else {
                throw new Exception("O login provider não pode ser verificado através do parâmetro 'state'.");
            }
            
            String referer = request.getHeader("referer");
            Logger.getLogger(LoginServlet.class.getName()).log(Level.INFO, "Request HTTP Referer:" + referer);
            Enumeration headers = request.getHeaderNames();
            
            // Busca o token
            OAuthClientRequest oauthReq = OAuthClientRequest
                .tokenLocation(tokenEndpoint)
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .setRedirectURI("http://localhost:8080/webapp01/authorize")
                .setCode(code)
                .setGrantType(GrantType.AUTHORIZATION_CODE)
                .buildBodyMessage();
            
            Logger.getLogger(LoginServlet.class.getName()).log(Level.INFO, "Token request (POST) to URL:" + oauthReq.getLocationUri());
            
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
            
        } catch(OAuthProblemException ex) {
            
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
            
            Logger.getLogger(AuthorizeServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch(OAuthSystemException ex) {
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
            
            Logger.getLogger(AuthorizeServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch(ClassCastException ex) {
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
            
            Logger.getLogger(AuthorizeServlet.class.getName()).log(Level.SEVERE, null, ex);            
        } catch (Exception ex) {
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
            Logger.getLogger(AuthorizeServlet.class.getName()).log(Level.SEVERE, null, ex);
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
