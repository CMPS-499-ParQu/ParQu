var admin = require("firebase-admin");

var serviceAccount = require("../newfirebaseauth-d6321-firebase-adminsdk-3x8xi-8964ae0daf.json");

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
  databaseURL: "https://newfirebaseauth-d6321.firebaseio.com"
});

// var serviceAccount = require("../fir-auth-45665-firebase-adminsdk-uy4gg-a050a10a78.json");

// admin.initializeApp({
//   credential: admin.credential.cert(serviceAccount),
//   databaseURL: "https://fir-auth-45665.firebaseio.com"
// });

var db = admin.database();

module.exports = {db, admin};