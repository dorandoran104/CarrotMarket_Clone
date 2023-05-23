package org.ezen.ex02.service;

import java.util.List;

import org.ezen.ex02.domain.ChatRoomVO;
import org.ezen.ex02.domain.ChatVO;

public interface ChatService {

	List<ChatRoomVO> getMyChatRoom(int id);

	ChatRoomVO findChatRoom(int id, int articleNo);

	ChatRoomVO createChatRoom(int id, int targetUser, int articleNo);

	ChatRoomVO getChatRoomByRoomId(String roomId, int chatUser);

	void insertMessage(ChatVO chatVO);
}
