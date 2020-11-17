const functions = require('firebase-functions');

// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
// exports.helloWorld = functions.https.onRequest((request, response) => {
//  response.send("Hello from Firebase!");
// });

// The Firebase Admin SDK to access the Firebase Realtime Database.
const admin = require('firebase-admin');
admin.initializeApp();

exports.NotifyOnNewOrder = functions.database.ref('/orders/{pushId}/')
    .onCreate((snapshot, context) => {
        var order_string = JSON.stringify(snapshot.val())
        var order = JSON.parse(order_string);
        var buyer_id = order.orderBuyerId;
        var item_id = order.orderItemId;
        var owner_id = order.orderOwnerId;


        var users_ref = admin.database().ref('users');
        users_ref.once('value').then((users_snapshot) => {

            var buyer_string = JSON.stringify(users_snapshot.child(buyer_id).val())
            var buyer = JSON.parse(buyer_string);

            var buyer_name = buyer.userName;

            var owner_string = JSON.stringify(users_snapshot.child(owner_id).val())
            var owner = JSON.parse(owner_string);

            var owner_token = owner.userToken;

            var item_ref = admin.database().ref('items/' + item_id);
            item_ref.once('value').then((item_snapshot) => {
                    var item_string = JSON.stringify(item_snapshot.val())
                    var item = JSON.parse(item_string);
                    var item_name = item.itemName;

                    var message = {
                        data: {
                            messageType: "1",
                            buyerName: buyer_name,
                            itemName: item_name
                        },
                        token: owner_token
                    }
                    admin.messaging().send(message)
                        .then((response) => {
                            // Response is a message ID string.
                            console.log('Successfully sent message:', response);
                            return null
                        })
                        .catch((error) => {
                            console.log('Error sending message:', error);
                        });
                    return 0;
                }
            ).catch(error => {
                console.log('Error sending message:', error);
            });

            return 0;
        }).catch(error => {
            console.log('Error sending message:', error);
        });
    });

// exports.SubOnLocationChange = functions.database.ref('/users/{pushId}/userLocation').onUpdate((snapshot, context) => {
//     snapshot.va
//     }
// );

exports.NotifyOnNewMessage = functions.database.ref('/chats/threads/{pushId}/{messageId}')
    .onCreate((snapshot, context) => {
        var message_string = JSON.stringify(snapshot.val())
        var message = JSON.parse(message_string);
        var sender_id = message.senderId;
        var receiverId = message.receiverId;
        var message_text = message.messageText;

        console.log(message.senderId + ' >> ' + message.receiverId + " : " + message.messageText);

        var users_ref = admin.database().ref('users');
        users_ref.once('value').then((users_snapshot) => {

                var receiver_string = JSON.stringify(users_snapshot.child(receiverId).val())
                var receiver = JSON.parse(receiver_string);
                var receiver_name = receiver.userName;
                var receiver_toekn = receiver.userToken;

                var sender_string = JSON.stringify(users_snapshot.child(sender_id).val())
                var sender = JSON.parse(sender_string);
                var sender_name = sender.userName;

                var message = {
                    data: {
                        messageType: "2",
                        messageText: message_text,
                        senderName: sender_name,
                        senderId: sender_id
                    },
                    token: receiver_toekn
                }
                admin.messaging().send(message)
                    .then((response) => {
                        // Response is a message ID string.
                        console.log('Successfully sent message:', response);
                        return null
                    })
                    .catch((error) => {
                        console.log('Error sending message:', error);
                    });
                return 0;
            }
        ).catch(error => {
            console.log('Error sending message:', error);
        });
    });


exports.NotifyOnNewItem = functions.database.ref('/items/{pushId}/')
    .onCreate((snapshot, context) => {
        var item_string = JSON.stringify(snapshot.val())
        var item = JSON.parse(item_string);
        var item_owner_id = item.itemOwnerId;
        var item_id = item.itemId;
        var item_name = item.itemName;
        var item_cat = item.itemCategory;

        var users_ref = admin.database().ref('users');
        users_ref.once('value').then((users_snapshot) => {

            var ownwer_string = JSON.stringify(users_snapshot.child(item_owner_id).val())
            var owner = JSON.parse(ownwer_string);

            var onwer_location = owner.userLocation;

            var message = {
                data: {
                    messageType: "3",
                    itemId: item_id,
                    itemCat: item_cat,
                    itemName: item_name
                }
            }

            admin.messaging().sendToTopic(onwer_location, message)
                .then((response) => {
                    // Response is a message ID string.
                    console.log('Successfully sent message:', response);
                    return null
                })
                .catch((error) => {
                    console.log('Error sending message:', error);
                });

            return 0;
        }).catch(error => {
            console.log('Error sending message:', error);
        });
    })
;