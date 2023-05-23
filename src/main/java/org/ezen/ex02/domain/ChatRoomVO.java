package org.ezen.ex02.domain;

import java.util.List;

import lombok.Data;

@Data
public class ChatRoomVO {
	
	private String roomId;
	private int chatUser;
	private int targetUser;
	private int articleNo;
	
	private List<ChatVO> chatMessage;
}
