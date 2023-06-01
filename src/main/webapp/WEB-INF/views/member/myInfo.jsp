<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="pageTitle" value="마이 페이지"></c:set>

<%@ include file="../include/header.jspf"%>

<link rel="stylesheet" type="text/css" href="../css/member.css">
<link rel="stylesheet" media="all" href="https://d1unjqcospf8gs.cloudfront.net/assets/home/users/show-b2c7250e62f851bf2967ba56a6ee71752f170e773072e6c74ab741ff9572737b.css" />

<section id="content">
    <section id="user-profile">
    	<div class="d-flex justify-content-evenly mb-3" style="border-bottom: 1px #e9ecef solid;">
    		<button type="button" class="myInfo_Btn" onclick="location.href='myInfo'" style="color : #ff6f0f;">내 정보</button>
			<button type="button" class="myInfo_Btn" onclick="location.href='modify'">내 정보 수정</button>
			<button type="button" class="myInfo_Btn" onclick="location.href='passwd'">비밀번호 변경</button>
			<button type="button" class="myInfo_Btn" onclick="location.href='remove'">회원탈퇴</button>
		</div>
      <h2 id="nickname" class="mt-5">
        ${member.usernickname}
        <span id="region_name">${member.useraddress}</span>
      </h2>
      <ul id="profile-detail">
        <li class="profile-detail-title">매너온도 <span class="profile-detail-count">36.5°C</span></li>
        <li class="profile-detail-title">재거래희망률 <span class="profile-detail-count">표시될 만큼 충분히 거래하지 않았어요.</span></li>
      </ul>
      <div id="profile-image">
      	<!-- 
        <img alt="오무아" src="https://d1unjqcospf8gs.cloudfront.net/assets/users/default_profile_80-c649f052a34ebc4eee35048815d8e4f73061bf74552558bb70e07133f25524f9.png" />
         -->
      </div>
    </section>

    <div id="user-records-detail">
      <section id="user-filter">
    <ul class="d-flex justify-content-around">
      <li><a class="active" href="#">내 판매 물품 (${list.size()})</a></li>
        <li><a class="" href="#">거래 후기 (0)</a></li>
        <li><a class="" href="#">매너 칭찬</a></li>
    </ul>
</section>

  <section id="user-records" class="user-articles" data-total-page="1" data-current-page="1">
      <section class="cards-wrap">
      	<c:forEach items="${list}" var="list">
	        <article class="card ">
	  			<a class="card-link" href="../sharticle/get?id=${list.id}">
				    <div class="card-photo" >
				        <img src="../shattach/thumbnail/${list.id}" style="width : 100%; height : 100%" />
				    </div>
	    		<div class="card-desc">
		      		<h2 class="card-title">${list.title}</h2>
					<div class="card-price "><fmt:formatNumber value="${list.cost}" pattern="#,###" />원</div>
		      		<div class="card-region-name">${member.useraddress}</div>
					<div class="card-counts">
					    <span>관심 ${list.likeCount}</span>∙<span>채팅 ${list.chatCount}</span>
					</div>
	   			</div>
				</a>
			</article>
		</c:forEach>
      </section>
      
  </section>
    </div>

  </section>
<!-- Footer-->
<%@ include file="../include/footer.jspf" %>
