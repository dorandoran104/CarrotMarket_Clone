package org.ezen.ex02.controller;

import org.ezen.ex02.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.Setter;

@Controller
@RequestMapping("/chat")
public class ChatController {
	
	@Setter(onMethod_=@Autowired)
	private ChatService chatService;
	
	@GetMapping("/add")
	public String chatAdd() {
		return "chat/add";
	}
	
	@GetMapping("/doAdd")
	public String chatAddAction() {
		return null;
	}
	
}
