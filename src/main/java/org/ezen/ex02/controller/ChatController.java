package org.ezen.ex02.controller;

import java.util.List;

import javax.print.attribute.standard.Media;
import javax.servlet.http.HttpSession;

import org.ezen.ex02.domain.ChatRoomVO;
import org.ezen.ex02.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import lombok.Setter;

@RequestMapping("/chat")
@RestController
public class ChatController {
	@Setter(onMethod_=@Autowired )
	private ChatService chatService;
	
	@GetMapping("/list")
	public ModelAndView getChatRoom(HttpSession session) {
		int id = (int)session.getAttribute("loginUser");
		
		List<ChatRoomVO> list = chatService.getMyChatRoom(id);
		ModelAndView mav = new ModelAndView();
		
		mav.addObject("list",list);
		mav.setViewName("chat/add");
		return mav;
	}
	
	@GetMapping("/get")
	public ModelAndView createNewChat(HttpSession session,int targetUser) {
		int id = (int)session.getAttribute("loginUser");
		
		ChatRoomVO chatRoom = chatService.findChatRoom(id,targetUser);
		if(chatRoom == null) {
			chatRoom = chatService.createChatRoom(id,targetUser);
		}
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("chat/get");
		mav.addObject("chatRoom", chatRoom);
		
		return mav;
	}
	//채팅목록에서 채팅방 들어가기
	@GetMapping("/geti")
	public ModelAndView joinChatRoom(HttpSession session, String roomId) {
		
		int chatUser = (int)session.getAttribute("loginUser");
		
		ChatRoomVO chatRoomVO = chatService.getChatRoomByRoomId(roomId,chatUser);
		ModelAndView mav = new ModelAndView();
		mav.setViewName("chat/get");
		mav.addObject("chatRoom", chatRoomVO);
		return mav;
	}
	
	@GetMapping(value="/room/{roomId}",produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ChatRoomVO> getChatRoom(@PathVariable("roomId") String roomId, HttpSession session){
		int chatUser = (int)session.getAttribute("loginUser");
		ChatRoomVO chatRoomVO = chatService.getChatRoomByRoomId(roomId, chatUser);
		
		return new ResponseEntity<>(chatRoomVO,HttpStatus.OK);
	}
	
	@GetMapping(value="/get/{roomId}",produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<ChatRoomVO>> getMyChatRoom(HttpSession session){
		int chatUser = (int)session.getAttribute("loginUser");
		List<ChatRoomVO> list = chatService.getMyChatRoom(chatUser);
		return new ResponseEntity<>(list,HttpStatus.OK);
	}
	
	@GetMapping(value="/new/{targetUser}",produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ChatRoomVO> createChatRoom(HttpSession session,@PathVariable("targetUser") int targetUser){
		int chatUser = (int)session.getAttribute("loginUser");
		
		ChatRoomVO chatRoom = chatService.findChatRoom(chatUser,targetUser);
		if(chatRoom == null) {
			chatRoom = chatService.createChatRoom(chatUser,targetUser);
		}
		
		return new ResponseEntity<>(chatRoom,HttpStatus.OK);
	}
	
	
}
