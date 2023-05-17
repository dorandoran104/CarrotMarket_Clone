<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>  
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<c:set var="pageTitle" value="${article.title}"></c:set>
<%@ include file="../include/header.jspf"%>
<c:if test="${article.lng != null && article.lat != null }">
<script type="text/javascript" src="${kakaoKey}"></script>
</c:if>

<!-- bootstrap -->
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.7.1/font/bootstrap-icons.css">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-KK94CHFLLe+nY2dmCWGMq91rCGa5gtU4mk92HdvYe+M/SXH301p5ILy+dN9+nJOZ" crossorigin="anonymous">
<!-- 당근 -->
 <link rel="stylesheet" media="all" href="https://d1unjqcospf8gs.cloudfront.net/assets/home/articles/show-761545e418b84f277aa422ad6dc89cee119e0355d95f099cd3141f529fea4806.css" />
<!-- slick -->
<script src="//code.jquery.com/jquery-3.3.1.min.js"></script>
<script src="//cdn.jsdelivr.net/npm/slick-carousel@1.8.1/slick/slick.min.js"></script>
<link rel="stylesheet" href="//cdn.jsdelivr.net/npm/slick-carousel@1.8.1/slick/slick.css" />
<link rel="stylesheet" href="//cdn.jsdelivr.net/npm/slick-carousel@1.8.1/slick/slick-theme.css" />
<link rel="stylesheet" href="../css/article.css" />
<!-- 카카오 맵 -->

<div style="margin: 95px auto; width: 100%; font-size: 1.5rem">
	<article id="content" data-id="${article.id}">
		<h1 class="hide">참치캔</h1>
		<div class="row">
			<span class="prev col-1" id="aro1_prev"><i class="bi bi-arrow-left"></i></span>
			<div class="imageArea col-10" style="height:350px; overflow: hidden; margin: auto">
				
				
			</div>
			<span class="next col-1" id="aro1_next"><i class="bi bi-arrow-right"></i></span>
		</div>
		<section id="article-profile">
		
				<h3 class="hide">프로필</h3>
				<div class="space-between">
					<div style="display: flex;">
						<!-- 
						<div id="article-profile-image">
							<img alt="당근당근쥬스"
								src="https://d1unjqcospf8gs.cloudfront.net/assets/users/default_profile_80-c649f052a34ebc4eee35048815d8e4f73061bf74552558bb70e07133f25524f9.png" />
						</div>
						 -->
						<div id="article-profile-left">
							<div id="nickname">${article.nickname }</div>
							<div id="region-name">${article.address }</div>
						</div>
					</div>
					<div id="article-profile-right">
						<dl id="temperature-wrap">
							<dt>매너온도</dt>
							<dd class="text-color-04 ">
								90.4 <span>°C</span>
							</dd>
						</dl>
						<div class="meters">
							<div class=" bar bar-color-04" style="width: 39%;"></div>
						</div>
						<div class=" face face-04"></div>
					</div>
				</div>
		</section>

		<section id="article-description">
			<h1 property="schema:name" id="article-title"
				style="margin-top: 0px;">${article.title}</h1>
			<p id="article-category">
				가공식품 ∙
				<time> 6일 전 </time>
			</p>
			
			<p style="font-size: 18px; font-weight: bold;">
				<fmt:formatNumber value="${article.cost}" pattern="#,###" />원
			 </p>
			<!--  <div property="schema:description" id="article-detail">-->
				<pre>${article.body}</pre>
			<!-- </div> -->
			<p id="article-counts">관심 19 ∙ 채팅 56 ∙ 조회 864</p>
			
			<c:if test="${article.lng != null && article.lat != null }">
				<div id="staticMap" style="width:100%;height:350px; margin:auto;" data-lng="${article.lng}" data-lat="${article.lat}"></div>	
			</c:if>
			
			<button id="chat-btn" class="mt-2">채팅하기</button>
		</section>
		<section id="article-reply">
			<h2>댓글</h2>
			
			<div class="input-group">
			  <textarea class="form-control" aria-label="With textarea"></textarea>
			  <span class="input-group-text reply-btn">댓글 작성</span>
			</div>
			
		</section>
	</article>
</div>
<c:if test="${article.lng != null && article.lat != null }">
<script src="../js/ArticleMap.js"></script>
</c:if>
<script src="../js/article.js"></script>
<script src="../js/image.js"></script>
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/js/bootstrap.bundle.min.js"
	integrity="sha384-ENjdO4Dr2bkBIFxQpeoTz1HIcje39Wm4jDKdf19U8gI4ddQ3GYNS7NTKfAdVQSZe"
	crossorigin="anonymous"></script>

<%@ include file="../include/footer.jspf"%>