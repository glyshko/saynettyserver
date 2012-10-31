/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.Server;

/**
 *
 * @author bezil
 */
public class Main {
    /**
     * @param args the command line arguments
     * 
     */
     public static BlockingQueue queue = new ArrayBlockingQueue(128);
     public static BlockingQueue nettydb = new ArrayBlockingQueue(128);
     public static BlockingQueue loginqueue = new ArrayBlockingQueue(10);
   
    
    public static void main(String[] args) throws Exception {
        final Db db = new Db();
        Listener listener = new Listener(db.conn,queue);
        Upstream upstream = new Upstream(db.conn,nettydb);
        Login login = new Login(db.conn,loginqueue);
        final Thread tl = new Thread(login,"LoginDB");
        final Thread tl1 = new Thread(listener,"DBListren");
        final Thread tu = new Thread(upstream,"UpstreamDB");
        tl.start();
        tl1.start();
        tu.start();       
        final Server srv = new Server();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() { 
                tl.interrupt();
                srv.stop();
                System.out.println("Waiting for closed queue.");
                while(!nettydb.isEmpty() || !queue.isEmpty()) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    }
                 }
                tl1.interrupt();
                System.out.println("DBListren closed.");
                tu.interrupt();
                System.out.println("UpstreamDB closed.");
                db.disconnect(); 
            }
        });
    }
}
