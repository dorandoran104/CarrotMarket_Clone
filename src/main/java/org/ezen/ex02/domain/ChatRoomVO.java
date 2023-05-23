package org.ezen.ex02.domain;

import lombok.Data;

@Data
public class ChatRoomVO {
	
	private String roomId;
	private int chatUser;
	private int targetUser;
}
