/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.zeus.app;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLXML;
import java.sql.ResultSet;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.Part;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;
import org.w3c.dom.Document;
/**
 *
 * @author wolf
 */
@MultipartConfig(maxFileSize = 16177215) 
@WebServlet(name = "MainServlet", urlPatterns = {"/xmlservlet"})
public class MainServlet extends HttpServlet {

   
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        Connection c = DatabaseConnection.getDb();
        PreparedStatement stmt;
        
        try{
            Part file = request.getPart("file");
            InputStream inputStream = file.getInputStream();
            
            DocumentBuilderFactory factory= DocumentBuilderFactory.newInstance();
            DocumentBuilder parser = factory.newDocumentBuilder();
            Document document= parser.parse(inputStream);
                
            SQLXML sqlxml = c.createSQLXML();
            SAXResult sax = sqlxml.setResult(SAXResult.class);

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.transform(new DOMSource(document), sax);

            String sql = "INSERT INTO xml_files (xml) VALUES (?)";
            stmt = c.prepareStatement(sql);
            
            stmt.setSQLXML(1, sqlxml);
            int row = stmt.executeUpdate();
            
             stmt.executeUpdate(sql);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        
        
        
    }
    @Override
    protected  void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/xml;charset=UTF-8");
        PrintWriter writer = response.getWriter();

        int id = Integer.parseInt(request.getParameter("id"));
        Connection c = DatabaseConnection.getDb();
        PreparedStatement stmt;
        
        try{
            DocumentBuilderFactory factory= DocumentBuilderFactory.newInstance();
            DocumentBuilder parser = factory.newDocumentBuilder();
            //Document document= parser.parse(inputStream);
                

            String sql = "SELECT xml FROM xml_files WHERE id=?";
            stmt = c.prepareStatement(sql);
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            while(rs.next()){
                SQLXML xml = rs.getSQLXML("xml");
                
                writer.print(xml.getString());
            }
            
             stmt.executeUpdate(sql);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        
        
    }
  
}
