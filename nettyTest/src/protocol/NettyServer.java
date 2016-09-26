package protocol;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;

public class NettyServer {

	public void bind() throws Exception{
		
		EventLoopGroup bossGroup = new NioEventLoopGroup();
	    EventLoopGroup workerGroup = new NioEventLoopGroup();
	    ServerBootstrap b = new ServerBootstrap();
	    
	    b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
	     .option(ChannelOption.SO_BACKLOG, 100)
	     .childHandler(new ChannelInitializer<SocketChannel>() {
	    	 
	    	 @Override
	    	protected void initChannel(SocketChannel ch) throws Exception {
	    		// TODO Auto-generated method stub
	    		
	    		ch.pipeline().addLast(new IOMessageDecoder(1024*1024, 4, 4));
	    		ch.pipeline().addLast(new IOMessageEncoder());
	    		ch.pipeline().addLast("readTimeOutHandler", new ReadTimeoutHandler(50));
	    		ch.pipeline().addLast(new LoginAuthRespHandler());
	    		ch.pipeline().addLast("HeartBeatHandler", new HeartBeatRespHandler());

	    	 }
		});
	    
	    b.bind(NettyConstant.REMOTEIP, NettyConstant.PORT);
	    System.out.println("Netty server start ok : " + NettyConstant.REMOTEIP + " : " + NettyConstant.PORT);
	}
	
	public static void main(String[] args)throws Exception{
		System.out.println("start");
		new NettyServer().bind();
	}
}
