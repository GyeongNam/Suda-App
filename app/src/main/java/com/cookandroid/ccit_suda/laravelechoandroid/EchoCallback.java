/*
 * EchoCallback.java
 * MrBin99 © 2018
 */
package com.cookandroid.ccit_suda.laravelechoandroid;

import io.socket.client.Ack;
import io.socket.emitter.Emitter;

/**
 * Echo callback.
 */
public interface EchoCallback extends Emitter.Listener, Ack {
}
