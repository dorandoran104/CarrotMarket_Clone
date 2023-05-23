package org.ezen.ex02.service;

import java.util.List;

import org.ezen.ex02.domain.ChatRoomVO;

public interface ChatService {

	List<ChatRoomVO> getMyChatRoom(int id);

	ChatRoomVO findChatRoom(int id, int targetUser);

	ChatRoomVO createChatRoom(int id, int targetUser);

	ChatRoomVO getChatRoomByRoomId(String roomId, int chatUser);
}
