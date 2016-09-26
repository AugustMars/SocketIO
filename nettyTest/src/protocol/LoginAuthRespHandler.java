package protocol;


import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class LoginAuthRespHandler extends ChannelInboundHandlerAdapter{

	private Map<String, Boolean> nodecheck = new ConcurrentHashMap<String, Boolean>();
	
	private String[] whitekList = {"127.0.0.1" , "192.168.1.104"};
	
	public LoginAuthRespHandler() {
		// TODO Auto-generated constructor stub
	}
	
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg){
		IOMessage message = (IOMessage) msg;

		System.out.println("server receive --> " + msg);
		
		// 如果是握手请求消息，处理，其它消息透传
		if (message.getHeader() != null
			&& message.getHeader().getType() == MessageType.LOGIN_REQ.value()) {
			
		    String nodeIndex = ctx.channel().remoteAddress().toString();
		    
		    System.out.println("remote IP --> " + nodeIndex);
		    
		    IOMessage loginResp = null;
		    // 重复登陆，拒绝
		    if (nodecheck.containsKey(nodeIndex)) {
			
		    	loginResp = buildResponse((byte) -1);
		    } else {
			
		    	InetSocketAddress address = (InetSocketAddress) ctx.channel()
				                            .remoteAddress();
			    String ip = address.getAddress().getHostAddress();
			
			boolean isOK = false;
			for (String WIP : whitekList) {
			    if (WIP.equals(ip)) {
				isOK = true;
				break;
			    }
			}
			
			loginResp = isOK ? buildResponse((byte) 0)
				: buildResponse((byte) -1);
			if (isOK)
			    nodecheck.put(nodeIndex, true);
		    }
		    
		    System.out.println("The login response is : " + loginResp
			    + " body [" + loginResp.getBody() + "]");
		    ctx.writeAndFlush(loginResp);
		} else {
		    ctx.fireChannelRead(msg);
		}
	}
	
	private IOMessage buildResponse(byte result) {
		IOMessage message = new IOMessage();
		Header header = new Header();
		header.setType(MessageType.LOGIN_RESP.value());
		message.setHeader(header);
		message.setBody(result);
		return message;
	  }
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		// TODO Auto-generated method stub
		super.exceptionCaught(ctx, cause);
	}
}
