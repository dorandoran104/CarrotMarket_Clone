package org.ezen.ex02.service;

import java.util.List;
import java.util.UUID;

import org.ezen.ex02.domain.ChatRoomVO;
import org.ezen.ex02.mapper.ChatMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.Setter;

@Service
public class ChatServiceImpl implements ChatService{
	
	@Setter(onMethod_=@Autowired)
	private ChatMapper chatMapper;
	//내 채팅방 불러오기
	@Override
	public List<ChatRoomVO> getMyChatRoom(int id) {
		List<ChatRoomVO> list = chatMapper.getMyChatRoom(id);
		return list;
	}
	
	//채팅방이 존재하는지
	@Override
	public ChatRoomVO findChatRoom(int id, int targetUser) {
		ChatRoomVO chatRoomVO = chatMapper.findChatRoom(id,targetUser);
		return chatRoomVO;
	}
	//채팅방이 없으면 새롭게 만들기
	@Override
	@Transactional
	public ChatRoomVO createChatRoom(int id, int targetUser) {
		String uuid = UUID.randomUUID().toString();
		
		ChatRoomVO chatRoomVO = new ChatRoomVO();
		chatRoomVO.setRoomId(uuid);
		
		//상대방한테도 리스트 뿌려주기 위해
		chatRoomVO.setChatUser(targetUser);
		chatRoomVO.setTargetUser(id);
		
		chatMapper.createChatRoom(chatRoomVO);
		
		chatRoomVO.setChatUser(id);
		chatRoomVO.setTargetUser(targetUser);
		
		chatMapper.createChatRoom(chatRoomVO);
		
		return chatRoomVO;
		
	}
	//내 채팅방에서 해당채팅방 들어갈때
	@Override
	public ChatRoomVO getChatRoomByRoomId(String roomId, int chatUser) {
		ChatRoomVO chatRoomVO = chatMapper.getChatRoomByRoomId(roomId, chatUser);
		return chatRoomVO;
	}
	
	

}
