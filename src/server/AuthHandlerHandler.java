/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.util.concurrent.BlockingQueue;
import main.Main;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.group.ChannelGroup;

/**
 *
 * @author bezil
 */
 public class AuthHandlerHandler extends SimpleChannelHandler {
     
     public BlockingQueue loginqueue = Main.loginqueue;
     public BlockingQueue nettydb = Main.nettydb;
     ChannelGroup channels = DiscardServerHandler.channels;
     
    
     
         @Override
    public void messageReceived( ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        if (auth(e)) {
            channels.add(ctx.getChannel());
            ctx.getPipeline().remove(this);
        }
        else {e.getChannel().close();}
    }

    // Returns true if auth was successfully
    private boolean auth(MessageEvent e) throws InterruptedException {
        Object[] array = loginqueue.toArray();
        if (loginqueue.contains(e.getMessage())) {
            Channel ch = e.getChannel();
            String request = ch.getRemoteAddress().toString();
            String chid = ch.getId().toString();
            String accesskey = e.getMessage().toString();
            String type = "connect";
            nettydb.put(type+";"+request+";"+chid+";"+accesskey);
            ch.write(ch.getId().toString()+ "\n");
            System.out.println("Id: " + ch.getId().toString());          
            return true;
        }
        return false;
    }  
    
    
        public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {        
        Channel ch = e.getChannel();
        String request = ch.getRemoteAddress().toString();
        System.out.println("Non auth channel disconnected id: " + e.getChannel().getId() + "; ip: "+request);
    }
    
}
