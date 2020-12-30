/*
 * AbstractChannel.java
 * MrBin99 © 2018
 */
package com.cookandroid.ccit_suda.laravelechoandroid.channel;


import com.cookandroid.ccit_suda.laravelechoandroid.EchoCallback;

/**
 * This class represents a basic channel.
 */
public abstract class AbstractChannel {

    /**
     * Listen for an event on the channel.
     *
     * @param event    event name
     * @param callback when event is received
     * @return this channel
     */
    public abstract AbstractChannel listen(String event, EchoCallback callback);

    /**
     * Listen for an event on the channel.
     *
     * @param callback when event is received
     * @return this channel
     */
    public AbstractChannel notification(EchoCallback callback) {
        return listen(".Illuminate\\\\Notifications\\\\Events\\\\BroadcastNotificationCreated", callback);
    }

    /**
     * Listen for a whisper event on the channel.
     *
     * @param event    event name
     * @param callback when event is received
     * @return this channel
     */
    public AbstractChannel listenForWhisper(String event, EchoCallback callback) {
        return listen(".client-" + event, callback);
    }
}
