/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bezil
 */
public class Login implements Runnable {
    
    private Connection conn;
    private PreparedStatement preparedStatement = null;
    BlockingQueue loginqueue;
    
    Login(Connection conn,BlockingQueue loginqueue) {
     this.conn=conn; 
     this.loginqueue=loginqueue;
    }

    @Override
    public void run() {
        System.out.println("Thread Login started");
        while(!Thread.interrupted()) {
            try {
                if (!loginqueue.isEmpty()) {
                    loginqueue.clear();                    
                }                  
                Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                ResultSet rs = stmt.executeQuery("SELECT `acceskey` from `hosts` where active = 1");
                while (rs.next()) {
                    loginqueue.put(rs.getString(1));
                }
                conn.commit();
                rs.close();
                stmt.close();             
                Thread.sleep(1000*60);
            }
            catch (InterruptedException ex) {
                Logger.getLogger(Upstream.class.getName()).log(Level.SEVERE, null, ex);                
            }
            catch (SQLException ex) {
                Logger.getLogger(Upstream.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }
    
}
