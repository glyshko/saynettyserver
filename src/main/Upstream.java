/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bezil
 */
public class Upstream implements Runnable  {
    
    	private Connection conn;
        private PreparedStatement preparedStatement = null;
        BlockingQueue nettydb;

	Upstream(Connection conn, BlockingQueue nettydb) {
            this.conn = conn;
            this.nettydb = nettydb;
	}
        
        public static String datetime(long time) {
        String sqlDate = new java.sql.Date(time).toString();
        String sqlTime = new java.sql.Time(time).toString();       
        return sqlDate+" "+sqlTime;
        }
        
        @Override
	public void run() {
            System.out.println("Thread Upstream started");
            while (!Thread.interrupted()) {
                try {
                    
                    while (!nettydb.isEmpty()) {                        
                        String take = nettydb.take().toString();
                        System.out.println(take);
                        String result[]  = take.split(";");
                        String type = result[0];
                        String channelid = result[2];
                        String accesskey = result[3];

                        switch(type) {
                            
                            case "connect":
                                long javatime = new java.util.Date().getTime();
                                String regex = "\\/(.*)\\:(.*)";
                                String ip = result[1].replaceAll(regex, "$1");
                                String port = result[1].replaceAll(regex, "$2");

                                String state = "up";
                                
                                preparedStatement = conn.prepareStatement("INSERT INTO sbb.logins(`id`,`ipadress`,`port`,`channelid`,`acceskey`,`created_at`,`state`,`updated_at`) VALUES (?,?,?,?,?,?,?,?);");
                                preparedStatement.setString(1, null);
                                preparedStatement.setString(2, ip);
                                preparedStatement.setString(3, port);
                                preparedStatement.setString(4, channelid);
                                preparedStatement.setString(5, accesskey);
                                preparedStatement.setString(6,datetime(javatime));
                                preparedStatement.setString(7,state);
                                preparedStatement.setString(8,datetime(javatime));
                                preparedStatement.executeUpdate();
                                conn.commit();
                                System.out.println(Thread.currentThread().getName() + " Cmd: " + take);                                
                                break;
                            case "disconnect":
                                long djavatime = new java.util.Date().getTime();
                                String dregex = "\\/(.*)\\:(.*)";
                                String dip = result[1].replaceAll(dregex, "$1");
                                String dport = result[1].replaceAll(dregex, "$2");
                                String dstate = "down";
                                preparedStatement = conn.prepareStatement("INSERT INTO sbb.logins(`id`,`ipadress`,`port`,`channelid`,`acceskey`,`created_at`,`state`,`updated_at`) VALUES (?,?,?,?,?,?,?,?);");
                                preparedStatement.setString(1, null);
                                preparedStatement.setString(2, dip);
                                preparedStatement.setString(3, dport);
                                preparedStatement.setString(4, channelid);
                                 preparedStatement.setString(5, "down");
                                preparedStatement.setString(6,datetime(djavatime));
                                preparedStatement.setString(7,dstate);
                                preparedStatement.setString(8,datetime(djavatime));
                                preparedStatement.executeUpdate();
                                conn.commit();
                                System.out.println(Thread.currentThread().getName() + " Cmd: " + take);                                
                                break;                                
                            case "message":
                                long mjavatime = new java.util.Date().getTime();
                                String message = result[1];
                                preparedStatement = conn.prepareStatement("INSERT INTO sbb.messages(`id`,`msg`,`channelid`,`created_at`,`updated_at`) VALUES (?,?,?,?,?);");
                                preparedStatement.setString(1, null);
                                preparedStatement.setString(2, message);
                                preparedStatement.setString(3, channelid);
                                preparedStatement.setString(4,datetime(mjavatime));
                                preparedStatement.setString(5,datetime(mjavatime));                                
                                preparedStatement.executeUpdate();
                                conn.commit();
                                System.out.println(Thread.currentThread().getName() + " Cmd: " + take);
                                break;
                            case "sendcomplete":
                                String msg = result[1];
                                Statement stmt = conn.createStatement();
                                int rows = stmt.executeUpdate("UPDATE `events` SET `status`=3 WHERE command=\""+msg+"\" AND channelid=\""+channelid+"\"");
                                //preparedStatement = conn.prepareStatement("");
                                //preparedStatement.setString(1, msg);
                                //preparedStatement.setString(2, channelid);
                                //preparedStatement.executeUpdate();
                                stmt.close() ;
                                conn.commit();
                                System.out.println(Thread.currentThread().getName() + " Cmd: " + take);
                                break;                                
                            default:
                                    System.out.println("Not found");
                        }
                        
                    }
                   
                } catch (SQLException | InterruptedException ex) {
                    Logger.getLogger(Upstream.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Upstream.class.getName()).log(Level.SEVERE, null, ex);
                }
            }   
	}
    
}
