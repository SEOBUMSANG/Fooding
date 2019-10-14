const admin = require('firebase-admin');
const functions = require('firebase-functions');
const Youtube = require('youtube-node');
const puppeteer = require('puppeteer');
const $ = require('cheerio');
admin.initializeApp(functions.config().firebase);

exports.searchVideo = functions.region('asia-northeast1').https.onRequest(async (req, res) => {

  let db = admin.firestore();

  var youtube = new Youtube();
  var wordArray = [];
  var limit = 9;
  var wordIndex = 0;
  var count = 0;

  youtube.setKey('AIzaSyBa6lDf0K-_meL_ON8kA4bEPIb3GctemO8');
  youtube.addParam('order', 'rating');
  youtube.addParam('type', 'video');
  youtube.addParam('videoLicense', 'youtube');


  for (wordIndex in wordArray) {
    youtube.search(wordArray[wordIndex], limit, function (err, result) {

      if (err) {
        console.log(err);
        return;
      }

      var items = result["items"];

      for (var itemIndex in items) {
        var it = items[itemIndex];
        var title = it["snippet"]["title"];
        var video_id = it["id"]["videoId"];
        var thumbnail = it["snippet"]["thumbnails"]["default"]["url"];
        var url = "https://www.youtube.com/watch?v=" + video_id;
        let docRef = db.collection(wordArray[count]).doc('영상 ' + itemIndex).set({
          title: title,
          thumbnail: thumbnail,
          URL: url
        });
      }
      count++;
    });
  }
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