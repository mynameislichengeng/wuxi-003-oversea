package com.wizarpos.pay.recode.http;

public interface HttpNewCallback {

    <T> void onSuccess(T t);

    <M> void onError(int code, M m);

    <N> void onError(N m);
}
