<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<c:set var="pageTitle" value="회원정보 수정"></c:set>

<%@ include file="../include/header.jspf"%>
<link rel="stylesheet" type="text/css" href="../css/chat.css">

<div id="container" style="margin-top: 150px; margin-bottom:150px;">
  <aside>
    <header>
      <input type="text" placeholder="search">
    </header>
    <ul>
      <li>
        <div>
          <h2>Prénom Nom</h2>
          <h3>
            <span class="status orange"></span>
            offline
          </h3>
        </div>
      </li>
    
    </ul>
  </aside>
  
  <main>
    <header>
      <div id="chatroom" data-roomid="${chatRoom.roomId}" data-userid="${loginUser}">
        <h2>Chat with Vincent Porter</h2>
        <h3>already 1902 messages</h3>
      </div>
      <img src="https://s3-us-west-2.amazonaws.com/s.cdpn.io/1940306/ico_star.png" alt="">
    </header>
    <ul id="chat">
    
      <li class="you">
        <div class="entete">
          <span class="status green"></span>
          <h2>Vincent</h2>
          <h3>10:12AM, Today</h3>
        </div>
        <div class="triangle"></div>
        <div class="message">
          Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor.
        </div>
      </li>
      
      <li class="me">
        <div class="entete">
          <h3>10:12AM, Today</h3>
          <h2>Vincent</h2>
          <span class="status blue"></span>
        </div>
        <div class="triangle"></div>
        <div class="message">
          Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor.
        </div>
      </li>
      
      
    </ul>
    <footer>
      <textarea placeholder="Type your message" id="chatting"></textarea>
      <img src="https://s3-us-west-2.amazonaws.com/s.cdpn.io/1940306/ico_picture.png" alt="">
      <img src="https://s3-us-west-2.amazonaws.com/s.cdpn.io/1940306/ico_file.png" alt="">
      <a href="#" id="sendBtn">Send</a>
    </footer>
  </main>
  
</div><script src="../js/chat.js"></script>
<%@ include file="../include/footer.jspf"%>