package com.technion.rbd.dressapp.BackEnd;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.widget.Toast;

import com.technion.rbd.dressapp.FrontEnd.Item;
import com.technion.rbd.dressapp.FrontEnd.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static android.app.PendingIntent.getActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;



/*
 * this class represents the connections between client and server.
 * it provides custom APIs to work with the database in firebase.
 * */

public class DatabaseAPI {
    private DatabaseReference rootRef;
    private DatabaseReference usersRef;
    private DatabaseReference itemsRef;
    private DatabaseReference ordersRef;
    private StorageReference mStorageRef;
    private DatabaseReference chatsRef;

    public DatabaseAPI() {
        usersRef = FirebaseDatabase.getInstance().getReference("users");
        itemsRef = FirebaseDatabase.getInstance().getReference("items");
        ordersRef = FirebaseDatabase.getInstance().getReference("orders");
        chatsRef = FirebaseDatabase.getInstance().getReference("chats");
        mStorageRef = FirebaseStorage.getInstance().getReference();
    }

    public void updateUserInfo(final User user, final boolean flag, final FirebaseCallback<User> callback) {
        final String key = user.getUserId();
        //Log.d("API logx", "Updating user by id = " + key);
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (flag == true && dataSnapshot.hasChild(key)) {
                    Log.e("API error logx", String.format("User with id = %s already exist!", key));
                    callback.onFailure();
                    return;
                }

                if (flag == false && !dataSnapshot.hasChild(key)) {
                    Log.e("API error logx", String.format("User with id = %s doesn't exist!", key));
                    callback.onFailure();
                    return;
                }
                usersRef.child(key).setValue(user, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                if (databaseError == null) {
                                    callback.onSuccess(user);
                                } else {
                                    Log.e("API error logx", "Error while updating user info: " + databaseError.toString());
                                    callback.onServerFailure();
                                }
                            }
                        }
                );


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onServerFailure();
            }
        });
    }

    public void getUserById(final String id, final FirebaseCallback<User> callback) {
        //Log.d("API logx", "getting user by id: " + id);
        usersRef.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final User user = dataSnapshot.getValue(User.class);
                if (user == null) {
                    Log.e("API error logx", "No user by id = " + id);
                    callback.onFailure();
                } else {
                    callback.onSuccess(user);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onServerFailure();
            }
        });
    }

    public void deleteUserById(final String id, final FirebaseCallback<User> callback) {
        //Log.d("API logx", "Deleting user by id: " + id);
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final User user = dataSnapshot.child(id).getValue(User.class);
                if (dataSnapshot.child(id).exists()) {
                    usersRef.child(id).removeValue(
                            new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                    if (databaseError == null) {
                                        callback.onSuccess(user);
                                    } else {
                                        callback.onServerFailure();
                                    }
                                }
                            }
                    );
                } else {
                    Log.e("API error logx", String.format("user with id = %s doesn't exist!", id));
                    callback.onFailure();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("API error logx", "The read failed: " + databaseError.getCode());
            }
        });
    }

    public void addNewItem(final Item item, final FirebaseCallback<Item> callback) {
        String key = itemsRef.push().getKey();
        // Log.d("API logx", "Adding new item by ID = " + key);
        item.setItemId(key);
        itemsRef.child(key).setValue(item, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null) {
                    callback.onSuccess(item);
                } else {
                    Log.e("API error logx", "Error while adding new item: " + databaseError.toString());
                    callback.onServerFailure();
                }
            }
        });
    }

    public void addNewItemWithPic(final Item item, Uri filePath, final FirebaseCallback<Item> callback) {
        final String key = itemsRef.push().getKey();
        if (filePath != null) {
            StorageReference riversRef = mStorageRef.child("items_images/" + key);
            riversRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            //Log.d("API logx", "Adding new item by ID = " + key);
                            item.setItemId(key);
                            item.setItemPicUrl(downloadUrl.toString());
                            itemsRef.child(key).setValue(item, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                    if (databaseError == null) {
                                        callback.onSuccess(item);
                                    } else {
                                        Log.e("API error logx", "Error while adding new item: " + databaseError.toString());
                                        callback.onServerFailure();
                                    }
                                }
                            });

                            //Log.d("API logx", "image uploaded successfully!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Log.e("API error logx", "failed to upload image!");
                        }
                    });
        } else {
            Log.e("API error logx", "Image uri is null!");
        }
    }

    public void getFilteredItems(final Checker<Item> checker, final FirebaseCallback<List<Item>> callback) {
        //Log.d("Api logx", "getting filtered items list");
        itemsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Item> items_list = new ArrayList<>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Item item = postSnapshot.getValue(Item.class);
                    if (checker.check(item)) {
                        items_list.add(item);
                    }
                }
                if (items_list.isEmpty()) {
                    callback.onFailure();
                } else {
                    callback.onSuccess(items_list);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onServerFailure();
            }
        });
    }

    public void getItemById(final String id, final FirebaseCallback<Item> callback) {
        //Log.d("API logx", "getting item by id: " + id);
        itemsRef.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final Item item = dataSnapshot.getValue(Item.class);
                if (item == null) {
                    Log.e("API error logx", "No item by id = " + id);
                    callback.onFailure();
                } else {
                    callback.onSuccess(item);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onServerFailure();
            }
        });
    }

    public void updateItemInfo(final Item item, final FirebaseCallback<Item> callback) {
        final String key = item.getItemId();
        //Log.d("API logx", "Updating item by id = " + key);
        itemsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChild(key)) {
                    Log.e("API error logx", String.format("Item with id = %s doesn't exist!", key));
                    callback.onFailure();
                    return;
                }
                itemsRef.child(key).setValue(item, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                if (databaseError == null) {
                                    callback.onSuccess(item);
                                } else {
                                    Log.e("API error logx", "Error while updating item info: " + databaseError.toString());
                                    callback.onServerFailure();
                                }
                            }
                        }
                );
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onServerFailure();
            }
        });
    }

    public void updateItemInfoWithPic(final Item item, Uri filePath, final FirebaseCallback<Item> callback) {
        final String key = item.getItemId();
        if (filePath != null) {
            StorageReference riversRef = mStorageRef.child("items_images/" + key);
            riversRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            final Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            //Log.d("API logx", "Updating item by id = " + key);
                            itemsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (!dataSnapshot.hasChild(key)) {
                                        Log.e("API error logx", String.format("Item with id = %s doesn't exist!", key));
                                        callback.onFailure();
                                        return;
                                    }
                                    item.setItemPicUrl(downloadUrl.toString());
                                    itemsRef.child(key).setValue(item, new DatabaseReference.CompletionListener() {
                                                @Override
                                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                                    if (databaseError == null) {
                                                        callback.onSuccess(item);
                                                    } else {
                                                        Log.e("API error logx", "Error while updating item info: " + databaseError.toString());
                                                        callback.onServerFailure();
                                                    }
                                                }
                                            }
                                    );
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    callback.onServerFailure();
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Log.e("API error logx", "failed to upload image!");
                        }
                    });
        } else {
            this.updateItemInfo(item, callback);
        }


    }

    public void deleteItemById(final String id, final FirebaseCallback<Item> callback) {
        itemsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.child(id).exists()) {
                    callback.onFailure();
                } else {
                    final Item item = dataSnapshot.child(id).getValue(Item.class);
                    itemsRef.child(id).removeValue(new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if (databaseError == null) {
                                callback.onSuccess(item);
                            } else {
                                callback.onServerFailure();
                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onServerFailure();
            }
        });
    }
//
//    public void updateUserWithPic(final User user, final boolean flag, final Uri filePath, final FirebaseCallback<User> callback) {
//        final String key = user.getUserId();
//        Log.d("API logx", "Updating user by id = " + key);
//        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if (flag == true && dataSnapshot.hasChild(key)) {
//                    Log.e("API error logx", String.format("User with id = %s already exist!", key));
//                    callback.onFailure();
//                    return;
//                }
//
//                if (flag == false && !dataSnapshot.hasChild(key)) {
//                    Log.e("API error logx", String.format("User with id = %s doesn't exist!", key));
//                    callback.onFailure();
//                    return;
//                }
//
//                StorageReference riversRef = mStorageRef.child("items_images/" + key);
//                riversRef.putFile(filePath)
//                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                            @Override
//                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                                final Uri downloadUrl = taskSnapshot.getDownloadUrl();
//                                usersRef.child(key).setValue(user, new DatabaseReference.CompletionListener() {
//                                            @Override
//                                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
//                                                if (databaseError == null) {
//                                                    callback.onSuccess(user);
//                                                } else {
//                                                    Log.e("API error logx", "Error while updating user info: " + databaseError.toString());
//                                                    callback.onServerFailure();
//                                                }
//                                            }
//                                        }
//                                );
//
//
//
//
//
//                        )
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception exception) {
//                                Log.d("API error logx", "failed to upload image!");
//                            }
//                        });
//
//
//
//
//
//
//                );
//
//
//            }
//
//
//    }
//


    public void updateUserInfoWithPic(final User user, final boolean flag, final Uri filePath, final FirebaseCallback<User> callback) {
        final String key = user.getUserId();
        //Log.d("API logx", "Updating user by id = " + key);
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (flag == true && dataSnapshot.hasChild(key)) {
                    Log.e("API error logx", String.format("User with id = %s already exist!", key));
                    callback.onFailure();
                    return;
                }

                if (flag == false && !dataSnapshot.hasChild(key)) {
                    Log.e("API error logx", String.format("User with id = %s doesn't exist!", key));
                    callback.onFailure();
                    return;
                }

                StorageReference riversRef = mStorageRef.child("profile_pics/" + key);
                riversRef.putFile(filePath)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                user.setUserProfilePic(downloadUrl.toString());
                                usersRef.child(key).setValue(user, new DatabaseReference.CompletionListener() {
                                            @Override
                                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                                if (databaseError == null) {
                                                    callback.onSuccess(user);
                                                } else {
                                                    Log.e("API error logx", "Error while updating user info: " + databaseError.toString());
                                                    callback.onServerFailure();
                                                }
                                            }
                                        }
                                );
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                Log.e("API error logx", "failed to upload image!");
                            }
                        });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onServerFailure();
            }
        });
    }

    public void addNewOrder(final Order order, final FirebaseCallback<Order> callback) {
//        String key = ordersRef.push().getKey();
        String key = order.getOrderItemId();
        // Log.d("API logx", "Adding new item by ID = " + key);
        //item.setItemId(key);
        ordersRef.child(key).setValue(order, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null) {
                    callback.onSuccess(order);
                } else {
                    Log.e("API error logx", "Error while adding new order");
                    callback.onServerFailure();
                }
            }
        });
    }

    public void startChat(final String id1, final String id2, final FirebaseCallback<String> callback) {

        chatsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("users").child(id1).child(id2).exists()) {
                    final String thread_id = dataSnapshot.child("users").child(id1).child(id2).getValue(String.class);
                    callback.onSuccess(thread_id);
                } else {
                    final String thread_id = chatsRef.child("threads/").push().getKey();
                    // todo: check if needed completion listener !
                    chatsRef.child("users").child(id1).child(id2).setValue(thread_id);
                    chatsRef.child("users").child(id2).child(id1).setValue(thread_id);
                    callback.onSuccess(thread_id);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onServerFailure();
            }
        });

    }

}

