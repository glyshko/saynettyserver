/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.BlockingQueue;

/**
 *
 * @author bezil
 */
class Listener implements Runnable {

	private Connection conn;
        protected BlockingQueue queue = null;

	Listener(Connection conn, BlockingQueue queue) {
		this.conn = conn;
                this.queue = queue;

	}

    @Override
	public void run() {
		while (!Thread.interrupted()) {
			try {
				Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
				ResultSet rs = stmt.executeQuery("SELECT * from events where status = 0");
                                while (rs.next()) {
                                    int id = rs.getInt(1);
                                    String channel = rs.getString(2);
                                    String cmd = rs.getString(3);
                                    System.out.println(Thread.currentThread().getName() + " Id: " + id + " Channel:" + channel + " Cmd: " + cmd);
                                    queue.put(cmd+";"+channel);
                                    rs.updateInt("status", 1);
                                    rs.updateRow();
                                } 
                                rs.close();
				stmt.close();
                                conn.commit();
				Thread.sleep(500);
			} 
                        catch (InterruptedException ex) {
                            System.out.println(ex.getMessage());
                        }
                        catch (Exception e) {
                              try {
                                conn.rollback();
                              } 
                        catch (SQLException error) {
                                  System.out.println(error.getSQLState());
                              }
                              System.out.println(e.getMessage());
                            }
		}
	}
}