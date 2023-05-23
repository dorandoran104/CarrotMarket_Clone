<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<c:set var="pageTitle" value="채팅"></c:set>

<%@ include file="../include/header.jspf"%>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css">
<link rel="stylesheet" type="text/css" href="../css/chat.css">

<div id="container" style="margin-top: 150px; margin-bottom:70px;" data-id="${loginUser}">
  <aside>
    <ul>
      <li>
        <div>
          <h2>Prénom Nom</h2>
        </div>
      </li>
    
    </ul>
  </aside>
  
  <main>
    <header>
      <div id="chatroom" data-roomid="${chatRoom.roomId}" data-userid="${loginUser}">
      	<img src="../attach/thumbnail/${article.id}" style="width : 140px;border-radius: 10px;"/>
        <div>
        	<h2>${article.title}</h2>
        	<h3><fmt:formatNumber value="${article.cost}" pattern="#,###"></fmt:formatNumber>원</h3>
        	<div>
        		<button id="rebtn">예약하기</button>
        	</div>
       	</div>
      </div>
    </header>
    <ul id="chat">
    	<li class="you">
        <div class="entete">
          <span class="status green"></span>
          <h2>Vincent</h2>
          <h3>10:12AM, Today</h3>
        </div>
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
        <div class="message">
          Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor.
        </div>
      </li>
    </ul>
    <footer>
      <textarea placeholder="Type your message" id="chatting"></textarea>
      <a href="#" id="sendBtn"><i class="bi bi-send"></i></a>
    </footer>
  </main>
  
</div><script src="../js/chat.js"></script>
<%@ include file="../include/footer.jspf"%>