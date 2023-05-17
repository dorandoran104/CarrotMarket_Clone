/**
 * 
 */
 
 
 $(document).ready(function(){
 	let pageNum = 1;
 	
 	$(".more-btn").on("click",function(e){
 		console.log("click");
 		let loading = '<div class="spinner-border text-warning" role="status"><span class="visually-hidden">Loading...</span></div>'
 		
 		let clone = $(this).clone();
 		
 		$(this).html(loading);
 		
 		getList(pageNum, clone);
 	});
 	
 	function getList(pageNum, clone){
 		let page = pageNum+1;
 		
 		$.ajax({
 			url : 'list/'+page,
 			success : function(result){
 				console.log(result);
 				$("#result-area").empty();
 				appendList(result);
 				$(".more-btn").html(clone.html());
 			}	
 		})
 	}
 })
 
 function appendList(result){
	let str = '';
	
	for(let i = 0; i<result.length; i++){
		str +='<article class="flea-market-article flat-card">'
		str +='<a class="flea-market-article-link" href="get?id=' + result[i].id + '" target="_blank">'
		str +='<div class="card-photo" style="background: url(../attach/thumbnail/' + result[i].id + ');background-size: cover;"></div>'						
		str +='<div class="article-info">';
		str +='<div class="article-title-content">';
		str +='<span class="article-title">' + result[i].title + '</span>'; 
		str +='<span class="article-content">' + result[i].body +'</span></div>';
		str +='<p class="article-region-name">' + result[i].address + '</p>';
		str +='<p class="article-price ">' + AddComma(result[i].cost) + '원</p>';
		str +='<section class="article-sub-info">'
		str +='<span class="article-watch">' 
		str +='<img class="watch-icon" alt="Watch count" src="https://d1unjqcospf8gs.cloudfront.net/assets/home/base/like-8111aa74d4b1045d7d5943a901896992574dd94c090cef92c26ae53e8da58260.svg" /> 1</span>';
		str +='</section></div></a></article>'; 
	}
	
	$("#result-area").append(str);
 }
 
 function AddComma(num) {
    var regexp = /\B(?=(\d{3})+(?!\d))/g;
    return num.toString().replace(regexp, ',');
}
 
 

 