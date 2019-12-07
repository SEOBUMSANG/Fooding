let printHello = () => console.log("Hello")

printHello();

var  client  = require ( 'cheerio-httpcli' ) ; 
 
// 검색어
var  word  = '강남구 맛집'; 
 
client . fetch ( 'https://search.naver.com/search.naver?sm=top_hty&fbm=1&ie=utf8&query=' , {  q :  word  } , function ( err , $ , res , body ) {       
  //  응답 헤더를 참조
  console . log ( res . headers ) ;
 
  //  HTML 제목을 표시
  console . log ( $ ( 'title' ) . text ( ) ) ;
 
  //  링크 목록을 표시
  $ ( 'a' ) . each ( function ( idx )  { 
    console . log ( $ ( this ) . attr ( 'href' ) ) ;
  } ) ;
} ) ;