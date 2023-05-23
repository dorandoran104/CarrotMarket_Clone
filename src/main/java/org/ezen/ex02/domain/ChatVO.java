package org.ezen.ex02.domain;

import lombok.Data;
@Data
public class ChatVO {
	
	private String roomId;
	private String message;
	private String sender;
	private String regDate;

}
