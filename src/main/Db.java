/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author bezil
 */
public final class Db {
    
    Connection conn;

    
//    @Override
//    public void finalize() {
//        disconnect();                    
//    }
    
    public Db() {
        
        connect();
        
    }
    
    public void connect() {
        
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://localhost/sbb";
        String username = "root";
        String password = "03.03.89";
        
        try {
            Class.forName(driver);
            System.out.println("Драйвер: " + driver + " загружен.");
        try {
            conn = DriverManager.getConnection(url, username, password);
            conn.setAutoCommit(false);
            //st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            System.out.println("Соиденение установлено: ОК.");
            
            } 
        catch (SQLException ex) {
            System.out.println("Ошибка: " + ex.getMessage());
            }
        } 
        catch (ClassNotFoundException ex) {
            System.out.println("Драйвер: " + driver + " не найден.");
            }
        
    }
    
   
    public void disconnect() {
        
        try {
            System.out.println("Пробуем закрить соиденение.");
            conn.close();
            System.out.println("Соиденение закрито: ОК.");
        }
        catch (SQLException ex) {
            System.out.println("Ошибка: " + ex.getMessage());
            }

    }
    
}
