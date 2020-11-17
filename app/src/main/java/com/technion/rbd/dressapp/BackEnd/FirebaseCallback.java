package com.technion.rbd.dressapp.BackEnd;

public interface FirebaseCallback<T> {
    void onSuccess(T t);

    void onFailure();

    void onServerFailure();
}
