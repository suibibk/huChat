package cn.forever.webSocket;

import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;
import javax.websocket.server.ServerEndpointConfig.Configurator;
/**
 * 获取HttpSession
 * @author forever
 *
 */
public class GetHttpSessionConfigurator extends Configurator {
	@Override
    public void modifyHandshake(ServerEndpointConfig sec,
            HandshakeRequest request, HandshakeResponse response) {
        // TODO Auto-generated method stub
        HttpSession httpSession=(HttpSession) request.getHttpSession();
        System.out.println("httpSession:"+httpSession);
        if(httpSession!=null){
        	sec.getUserProperties().put(HttpSession.class.getName(),httpSession);
        }else{
        	System.out.println("session为空，非法访问");
        }
    }
}
