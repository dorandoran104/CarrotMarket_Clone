package org.ezen.ex02.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.ezen.ex02.domain.ChatRoomVO;

public interface ChatMapper {

	List<ChatRoomVO> getMyChatRoom(int id);

	ChatRoomVO findChatRoom(@Param("id") int id,@Param("targetUser") int targetUser);

	void createChatRoom(ChatRoomVO chatRoomVO);

	ChatRoomVO getChatRoomByRoomId(@Param("roomId") String roomId,@Param("chatUser") int chatUser);

}
