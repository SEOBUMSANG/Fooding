const admin = require('firebase-admin');
const functions = require('firebase-functions');
const Youtube = require('youtube-node');
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