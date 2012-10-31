/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

/**
 *
 * @author bezil
 */
public class Server {
    ChannelGroup channels = DiscardServerHandler.channels;
    ChannelFactory factory = new NioServerSocketChannelFactory(Executors.newCachedThreadPool(),Executors.newCachedThreadPool());
    ServerBootstrap bootstrap = new ServerBootstrap(factory);
    
    
    
    public void stop() {
        channels.close().awaitUninterruptibly();
        bootstrap.releaseExternalResources();
        System.out.println("Server stoped at 8080 port");
    }
    
    public Server() {
        bootstrap.setPipelineFactory(new ChatChannelPipelineFactory());
        bootstrap.setOption("child.tcpNoDelay", true);
        bootstrap.setOption("child.keepAlive", true); 
        bootstrap.bind(new InetSocketAddress(8080));
        System.out.println("Server Started at 8080 port");
    }
}
