 package org.ezen.ex02.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.ezen.ex02.domain.ArticleVO;
import org.ezen.ex02.domain.ChatRoomVO;
import org.ezen.ex02.service.ArticlesService;
import org.ezen.ex02.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@RequestMapping("/chat")
@RestController
@Log4j
public class ChatController {
	@Setter(onMethod_=@Autowired )
	private ChatService chatService;
	
	@Setter(onMethod_=@Autowired )
	private ArticlesService articlesService;
	
	//내 채팅방 목록 불러오기
	@GetMapping("/list")
	public ModelAndView getChatRoom(HttpSession session) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("chat/get");
		return mav;
	}
	
	@GetMapping(value="/list/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<ChatRoomVO>> getChatRoomAjax(@PathVariable("id") int id){
		List<ChatRoomVO> list = chatService.getMyChatRoom(id);
		
		return list.size()>0? new ResponseEntity<>(list,HttpStatus.OK):new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	//게시글에서 채팅하기 누룰때
	@GetMapping("/new")
	public ModelAndView createNewChat(HttpSession session, int targetUser, int articleNo) {
		int id = (int)session.getAttribute("loginUser");
		
		ChatRoomVO chatRoom = chatService.findChatRoom(id,articleNo);
		//만약 전에 해당 게시글 물품대상으로 채팅한적이 없으면 채팅방 새로 만들기
		if(chatRoom == null) {
			chatRoom = chatService.createChatRoom(id,targetUser, articleNo);
		}
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("chat/get");
		mav.addObject("chatRoom", chatRoom);
		
		return mav;
	}
	//채팅목록에서 채팅방 들어가기
	@GetMapping("/get")
	public ModelAndView joinChatRoom(HttpSession session, String roomId) {
		
		int chatUser = (int)session.getAttribute("loginUser");
		ChatRoomVO chatRoomVO = chatService.getChatRoomByRoomId(roomId,chatUser);
		log.info(chatRoomVO);
		
		ArticleVO articleVO = articlesService.getArticle(chatRoomVO.getArticleNo());
		
		ModelAndView mav = new ModelAndView();
		
		mav.setViewName("chat/get");
		mav.addObject("article", articleVO);
		mav.addObject("chatRoom", chatRoomVO);
		return mav;
	}
}
