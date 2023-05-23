package org.ezen.ex02.handler;

import java.util.HashMap;
import java.util.Map.Entry;

import org.ezen.ex02.domain.ChatVO;
import org.ezen.ex02.service.ChatService;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;

import lombok.extern.log4j.Log4j;

@Log4j
@Component
public class SocketHandler extends TextWebSocketHandler{
	
	private HashMap<String,WebSocketSession> sessionMap = new HashMap<>();
	//private List<Map<String,Object>> sessionList = new ArrayList<>();
	
	private ChatService chatService;
	
	@Override
	//websocket 연결 성공 시 실행되는 메서드
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		super.afterConnectionEstablished(session);
		sessionMap.put(session.getId(), session);
		
	}
	
	@Override
	//websocket 연걸 종료 시 실행되는 메서드
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		sessionMap.remove(session.getId());
		
		super.afterConnectionClosed(session, status);
	}
	
	@Override
	//메세지 수신 및 송신
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		String id = session.getId();
		log.info("message payload" + message.getPayload());
		log.info("session : " + id + "message : " + message);
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		ChatVO chatVO = objectMapper.readValue(message.getPayload(), ChatVO.class);
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("roomId", chatVO.getRoomId());
		jsonObject.addProperty("message", chatVO.getMessage());
		jsonObject.addProperty("sender", chatVO.getSender());
		jsonObject.addProperty("regDate", chatVO.getRegDate());
		
		for(Entry<String, WebSocketSession> args : sessionMap.entrySet()) {
			if(!args.getKey().equals(id)) {
				args.getValue().sendMessage(new TextMessage(jsonObject.toString()));
			}
		}
	}
}
