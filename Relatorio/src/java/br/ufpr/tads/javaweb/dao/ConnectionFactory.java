/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpr.tads.javaweb.dao;

import br.ufpr.tads.javaweb.dao.expeptions.DAOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author razer
 */
public class ConnectionFactory implements AutoCloseable {
    private static String DRIVER = "org.postgresql.Driver";
    private static String URL    = "jdbc:postgresql://localhost:5432/razer";
    private static String LOGIN  = "postgres";
    private static String SENHA  = "postgres";
    
    private Connection con = null;
    public Connection getConnection() throws DAOException {  
       if (con == null) {
          try {
             Class.forName(DRIVER);  
             con = DriverManager.getConnection(URL, LOGIN, SENHA);
          }
          catch(ClassNotFoundException e) {
             throw new DAOException("Driver do banco não encontrado: " + DRIVER, e);
          }
          catch(SQLException e) {
             throw new DAOException("Erro conectando ao BD: " + URL + "/" + LOGIN + "/" + SENHA, e);
          }
      }
      return con;
   } 
    @Override
    public void close() {
        if (con!=null) {
            try { 
                con.close(); 
            }
            catch(Exception e) { 
                System.out.println("Erro fechando a conexão. IGNORADO");
                e.printStackTrace(); 
            }
            finally {
                con = null;
            }
        }
    }
    
}
