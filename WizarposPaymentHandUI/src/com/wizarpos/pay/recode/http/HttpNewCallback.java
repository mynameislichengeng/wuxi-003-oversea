package com.wizarpos.pay.recode.http;

public interface HttpNewCallback {

    <T> void onSuccess(T t);

    <M> void onError(M r);

    <M> void onError(int code,M r);
}
