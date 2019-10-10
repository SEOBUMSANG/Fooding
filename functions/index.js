const admin = require('firebase-admin');
const functions = require('firebase-functions');
const Youtube = require('youtube-node');
admin.initializeApp(functions.config().firebase);

exports.testFunction = functions.https.onRequest(async (req, res) => {
  // Grab the text parameter.
  let db = admin.firestore();
  const original = req.query.text;
  var youtube = new Youtube();
  var word = "강남 맛집";
  var limit = 10;
  var snapshot;
  youtube.setKey('AIzaSyBa6lDf0K-_meL_ON8kA4bEPIb3GctemO8');

  youtube.addParam('order', 'rating');
  youtube.addParam('type', 'video');
  youtube.addParam('videoLicense', 'creativeCommon');
  youtube.search(word, limit, function (err, result) {
      if (err) {
          console.log(err);
          return;
      }

      var items = result["items"];
      for (var i in items) {
          var it = items[i];
          var title = it["snippet"]["title"];
          var video_id = it["id"]["videoId"];
          var thumbnail = it["snippet"]["thumbnails"]["default"]["url"];
          var url = "https://www.youtube.com/watch?v=" + video_id;
          let docRef = db.collection('users').doc(title);
          let setAda = docRef.set({
            title: title,
            thumbnail: thumbnail,
            URL: url
          });
          
      }
  });

});