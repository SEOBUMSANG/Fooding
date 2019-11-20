const admin = require('firebase-admin');
const functions = require('firebase-functions');
const Youtube = require('youtube-node');
const puppeteer = require('puppeteer');
const $ = require('cheerio');
admin.initializeApp(functions.config().firebase);

exports.searchVideo = functions.region('asia-northeast1').https.onRequest((req, res) => {

  let db = admin.firestore();

  var youtube = new Youtube();
  var wordArray = ['강남 구구당', '강남 땀땀', '강남 바비레드', '강남 마초쉐프', '강남 고에몬', '강남 낙원타코', '강남 쉐이크쉑', '강남 스시메이진', '강남 마녀주방', '강남 감성타코', '강남 어글리스토브', '강남 장인닭갈비', '강남 베트남이랑', '강남 낙원테산도'];
  var limit = 10;

  youtube.setKey('AIzaSyBLSk0tcF9cIqo-1nVOi4nwi6JvyDRkLfs');
  youtube.addParam('order', 'relevance');
  youtube.addParam('type', 'video');
  youtube.addParam('videoLicense', 'youtube');

  function youtubeSearch(word) {
    return new Promise(function (resolve, reject) {
      youtube.search(word, limit, function (err, result) {
        if (err) {
          console.log(err);
          reject("error");
        }
        else {
          resolve(result);
        }
      });
    });
  }

  function youtubeCallBack(result, word) {
    var items = result["items"];
    for (var itemIndex in items) {
      var it = items[itemIndex];
      var title = it["snippet"]["title"];
      var description = it["snippet"]["description"];
      var video_id = it["id"]["videoId"];
      var thumbnail = it["snippet"]["thumbnails"]["default"]["url"];
      var url = "https://www.youtube.com/watch?v=" + video_id;
      let docRef1 = db.collection('강남구').doc(word);
      let docRef2 = docRef1.collection('유튜브 동영상').doc('동영상 ' + itemIndex);
      let setAda2 = docRef2.set({
        title: title,
        description: description,
        thumbnail: thumbnail,
        URL: url
      });

    }
  }
  async function searchData() {
    var i = 0;
    var length = wordArray.length;
    for (i = 0; i < length; i++) {
      var searchResult = await youtubeSearch(wordArray[i])
      youtubeCallBack(searchResult, wordArray[i]);
    }
    if (i == length) {
      res.send('완료');
    }
  }

  searchData();

});

exports.autoCrawling = functions.region('asia-northeast1').https.onRequest((req, res) => {

  let db = admin.firestore();

  function printConsole(content) {
    const body = $.load(content);
    const selector = 'div.list_item_inner';
    var anchors = [];
    var title;
    body(selector).each(function () {
      anchors.push($(this));
    });

    if (anchors.length > 0) {
      var i = 0;
      for (const index of anchors) {
        title = index.find(".tit .tit_inner a span").text();
        info = index.find(".info_area div.txt.ellp").text();
        console.log(title);
        if (info == "") {
          console.log("정보가 없습니다.");
        }
        else {
          console.log(info);
        }
        console.log('-----------------');

        let docRef = db.collection('네이버 크롤링').doc(title);
        let setAda = docRef.set({
          title: title,
          info: info
        });

        i++;
      }
    }
  }

  (async () => {
    const browser = await puppeteer.launch();
    const page = await browser.newPage();
    await page.goto("https://store.naver.com/restaurants/list?entry=pll&filterId=s13479290&query=%EA%B0%95%EB%82%A8%EC%97%AD%EB%A7%9B%EC%A7%91&sessionid=aUf0SUs8iwZ4YiBFHyCNDQ%3D%3D", { waitUntil: "networkidle2" });
    const content = await page.content();
    await printConsole(content);
    await browser.close();
  })();
});