/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.codec.frame.DelimiterBasedFrameDecoder;
import org.jboss.netty.handler.codec.frame.Delimiters;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;

/**
 *
 * @author bezil
 */
public class ChatChannelPipelineFactory implements ChannelPipelineFactory  {
    
    //private static Timer timer = new HashedWheelTimer();
    //private final ChannelHandler idleStateHandler = new IdleStateHandler(timer, 1, 20, 0);
    @Override
    public ChannelPipeline getPipeline() throws Exception {
          ChannelPipeline pipeline = Channels.pipeline();
          pipeline.addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
          pipeline.addLast("decoder", new StringDecoder());
          pipeline.addLast("encoder", new StringEncoder());
          // and then business logic.
          pipeline.addLast("AuthHandler", new AuthHandlerHandler());
          pipeline.addLast("handler", new DiscardServerHandler()); 
          return pipeline;
      }
    
}
