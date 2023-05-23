package org.ezen.ex02.service;

import java.util.List;
import java.util.UUID;

import org.ezen.ex02.domain.ChatRoomVO;
import org.ezen.ex02.domain.ChatVO;
import org.ezen.ex02.mapper.ChatMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	public ChatRoomVO findChatRoom(int id, int articleNo) {
		ChatRoomVO chatRoomVO = chatMapper.findChatRoom(id,articleNo);
		return chatRoomVO;
	}
	
	//채팅방이 없으면 새롭게 만들기
	@Override
	public ChatRoomVO createChatRoom(int id, int targetUser, int articleNo) {
		String uuid = UUID.randomUUID().toString();
		
		ChatRoomVO chatRoomVO = new ChatRoomVO();
		chatRoomVO.setRoomId(uuid);
		chatRoomVO.setChatUser(id);
		chatRoomVO.setTargetUser(targetUser);
		chatRoomVO.setArticleNo(articleNo);
		
		chatMapper.createChatRoom(chatRoomVO);
		
		return chatRoomVO;
		
	}
	//내 채팅방에서 해당채팅방 들어갈때
	@Override
	public ChatRoomVO getChatRoomByRoomId(String roomId, int chatUser) {
		ChatRoomVO chatRoomVO = chatMapper.getChatRoomByRoomId(roomId, chatUser);
		return chatRoomVO;
	}

	//채팅메세지 db에 저장하기
	@Override
	public void insertMessage(ChatVO chatVO) {
		chatMapper.insertMessage(chatVO);
	}
	
	
	
	
	

}