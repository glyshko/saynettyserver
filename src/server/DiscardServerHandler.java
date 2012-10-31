/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;
import main.Main;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.DefaultChannelGroup;


/**
 *
 * @author bezil
 * 
 * 
 * 
 */




public class DiscardServerHandler extends SimpleChannelHandler {
    
    private static final Logger logger = Logger.getLogger(DiscardServerHandler.class.getName());
    public BlockingQueue nettydb = Main.nettydb;
    public BlockingQueue queue = Main.queue;
    static final ChannelGroup channels = new DefaultChannelGroup();    
    
    private final AtomicLong transferredMessages = new AtomicLong();
    
      public long getTransferredMessages() {
          return transferredMessages.get();
      }
        
      @Override
      public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws InterruptedException {          
          e.getFuture().addListener(new ChannelFutureListener() {

             
              @Override
              public void operationComplete(ChannelFuture cf) throws Exception {
                  Channel channel = cf.getChannel();
                  String id = channel.getId().toString();
                  Object[] array = queue.toArray();
                    for (int i=0; i<array.length;i++) {
                        String qtake = array[i].toString();
                        String ch = qtake.split(";")[1];
                        String cmd = qtake.split(";")[0];
                        System.out.println("Sending to host " + qtake);
                        if (ch.contains(id)) {
                            queue.remove(qtake);
                            System.out.println("Send: "+channel + ";" + cmd);
                            channel.write(cmd+"\n");
                            String type = "sendcomplete";
                            nettydb.put(type+";"+cmd+";"+id+"; ");
                        }
                    }                  
                  }        
          });
          Channel ch = e.getChannel();
          String message = (String) e.getMessage();
          String chid = ch.getId().toString();
          String type = "message";
          nettydb.put(type+";"+message+";"+chid+"; ");
          if (message.equals("bye")) {
              ch.close();
          }
        }
        
    @Override
    public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        
        Channel ch = e.getChannel();
        String request = ch.getRemoteAddress().toString();
        String chid = ch.getId().toString();
        String type = "disconnect";
        nettydb.put(type+";"+request+";"+chid+"; ");
        channels.remove(ch);
        System.out.println(Thread.currentThread().getName() + "disconnected id: " + e.getChannel().getId());
    }
   
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
          logger.log(Level.WARNING,"Unexpected exception from downstream." + e.getCause());
          e.getChannel().close();
      }
        
        
    }