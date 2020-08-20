/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpr.tads.javaweb.servlets;

import br.ufpr.tads.javaweb.dao.ConnectionFactory;
import br.ufpr.tads.javaweb.dao.expeptions.DAOException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;

/**
 *
 * @author razer
 */
@WebServlet(name = "GeradorRelatorio", urlPatterns = {"/GeradorRelatorio"})
public class GeradorRelatorio extends HttpServlet {

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
        
        try(ConnectionFactory factory = new ConnectionFactory()) {

            // Caminho contextualizado do relatório compilado
            String jasper = request.getContextPath() + "/Teste.jasper";
            // Host onde o servlet esta executando 
            String host = "http://" + request.getServerName() + ":" + request.getServerPort(); 
            System.out.println(host+jasper);
            // URL para acesso ao relatório
            URL jasperURL = new URL(host + jasper);

            // Parâmetros do relatório
            HashMap params = new HashMap();
            // Geração do relatório

            byte[] bytes = JasperRunManager.runReportToPdf(jasperURL.openStream(), params, factory.getConnection());
            if (bytes != null) { 
               // A página será mostrada em PDF
               response.setContentType("application/pdf");
               // Envia o PDF para o Cliente
               OutputStream ops = response.getOutputStream();  
               ops.write(bytes);  
            } 
        } // Fechamento do try
        catch(DAOException e) {
           request.setAttribute("mensagem", "Erro de DAO : " + e.getMessage());
           request.getRequestDispatcher("erro.jsp").forward(request, response);
        }
        catch(JRException e) {
           request.setAttribute("mensagem", "Erro no Jasper : " + e.getMessage());
           request.getRequestDispatcher("erro.jsp").forward(request, response);
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
