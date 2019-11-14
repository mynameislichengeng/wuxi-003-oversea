package com.wizarpos.base.net;

/**
 * Created by wu on 16/3/29.
 */
public interface SessionListener {

    int SESSION_ERROR_CODE = 188;

    void sessionAlive();
    void sessionDead();
}
