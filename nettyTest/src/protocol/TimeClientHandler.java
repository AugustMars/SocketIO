package protocol;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class TimeClientHandler extends ChannelInboundHandlerAdapter{
    
	private final ByteBuf buffer;
	
	public TimeClientHandler() {
		// TODO Auto-generated constructor stub
        byte[] req = "QUERY TIME ORDER".getBytes();
        buffer = Unpooled.buffer(req.length);
        buffer.writeBytes(req);
	}
	
	@Override
    public void channelActive(ChannelHandlerContext ctx) 
    	throws Exception {
    	// TODO Auto-generated method stub
		ctx.writeAndFlush(buffer);
    }
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		// TODO Auto-generated method stub
        ByteBuf buf = (ByteBuf) msg;
        byte[] resp  = new byte[buf.readableBytes()];
        buf.readBytes(resp);
        String body = new String(resp, "UTF-8");
        
        System.out.println("NOW IS: " + body);
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		// TODO Auto-generated method stub
        ctx.close();
	}
}
