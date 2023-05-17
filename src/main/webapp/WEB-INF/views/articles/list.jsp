<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<c:set var="pageTitle" value="당근 찾기"></c:set>

<%@ include file="../include/header.jspf"%>
<link rel="stylesheet" media="all" href="https://d1unjqcospf8gs.cloudfront.net/assets/home/search/index-0ea06e6a74007d2189f614229a312e9d24063f6171f58f38e95232750d810017.css" />
<link rel="stylesheet" type="text/css" href="../css/list.css">

<section id="result">
	<div class="result-container">
		<div id="flea-market-wrap" class="articles-wrap">
			<div style="display: flex; justify-content: space-between;">
				<p class="article-kind">중고거래</p>
				<c:if test="${loginUser != null }">
					<a href="/ex02/articles/new" class="article-kind">당근 등록</a>
				</c:if>
			</div>
			<div id="result-area">
			<c:forEach items="${list}" var="list">
			<article class="flea-market-article flat-card">
				<a class="flea-market-article-link" href="get?id=${list.id}">
					<div class="card-photo" style="background: url(../attach/thumbnail/${list.id});background-size: cover;">
						<!-- <img src="../attach/thumbnail/${list.id}" /> -->
					</div>
					<div class="article-info">
						<div class="article-title-content">
							<span class="article-title">${list.title}</span> 
								<span class="article-content">${list.body}</span>
						</div>

						<p class="article-region-name">충남 천안시 동남구 수신면</p>
						<p class="article-price "><fmt:formatNumber value="${list.cost}" pattern="#,###" />원</p>
						<section class="article-sub-info">
							<span class="article-watch"> <img class="watch-icon"
								alt="Watch count"
								src="https://d1unjqcospf8gs.cloudfront.net/assets/home/base/like-8111aa74d4b1045d7d5943a901896992574dd94c090cef92c26ae53e8da58260.svg" />
								1
							</span>
						</section>
					</div>
				</a>
			</article>
			</c:forEach>
			</div>
			<div class="more-btn" data-o-keyword="노트북" data-page="1" data-total-pages="834">
   			<span class="more-text">더보기</span>
      		<div class="more-loading">
        	<div class="loader"></div>
		</div>
	</div>
</section>
<script src="../js/list.js"></script>
<%@ include file="../include/footer.jspf"%>