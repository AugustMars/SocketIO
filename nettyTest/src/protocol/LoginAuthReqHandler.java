package protocol;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class LoginAuthReqHandler extends ChannelInboundHandlerAdapter{

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception{
		
		IOMessage mes = buildLoginReq();
		System.out.println("client send -- > " + mes);
		ctx.writeAndFlush(mes);
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg){
		
		IOMessage message = (IOMessage) msg;

		System.out.println("client receive --> " + msg);
		
		if(message.getHeader() != null && message.getHeader().getType() == MessageType.LOGIN_RESP.value()){
			
			Object loginResult = (Object)message.getBody();
			
			System.out.println("client receive body : " + loginResult);
			
			if(loginResult.toString() == "0"){
				ctx.close();
			}else{
				System.out.println("Login is Success!!");
				ctx.fireChannelRead(msg);
			}
		}else{
			ctx.fireChannelRead(msg);
		}
	} 
	
	private IOMessage buildLoginReq(){
		
		System.out.println("loginReq-->");
		
		IOMessage message = new IOMessage();
		Header header = new Header();
		header.setType(MessageType.LOGIN_REQ.value());
		message.setHeader(header);
		return message;
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		// TODO Auto-generated method stub
		ctx.fireExceptionCaught(cause);
	}
	
}
