/*
 * SocketIOConnector.java
 * MrBin99 © 2018
 */
package com.cookandroid.ccit_suda.laravelechoandroid.connector;

import android.util.Log;

import com.cookandroid.ccit_suda.laravelechoandroid.EchoCallback;
import com.cookandroid.ccit_suda.laravelechoandroid.EchoException;
import com.cookandroid.ccit_suda.laravelechoandroid.EchoOptions;
import com.cookandroid.ccit_suda.laravelechoandroid.channel.AbstractChannel;
import com.cookandroid.ccit_suda.laravelechoandroid.channel.SocketIOChannel;
import com.cookandroid.ccit_suda.laravelechoandroid.channel.SocketIOPresenceChannel;
import com.cookandroid.ccit_suda.laravelechoandroid.channel.SocketIOPrivateChannel;
import com.cookandroid.ccit_suda.log;

import io.socket.client.IO;
import io.socket.client.Socket;


import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class creates a connector to a Socket.io server.
 */
public class SocketIOConnector extends AbstractConnector {

    /**
     * The socket.
     */
    private Socket socket;

    /**
     * All of the subscribed channel names.
     */
    private ConcurrentHashMap<String, SocketIOChannel> channels;

    /**
     * Create a new Socket.IO connector.
     *
     * @param options options
     */
    public SocketIOConnector(EchoOptions options) {
        super(options);

        channels = new ConcurrentHashMap<>();

    }

    @Override
    public void connect(EchoCallback success, EchoCallback error) {
        try {
            socket = IO.socket(this.options.host);
            socket.connect();

            if (success != null) {
                socket.on(Socket.EVENT_CONNECT, success);
            }

            if (error != null) {
                socket.on(Socket.EVENT_CONNECT_ERROR, error);
            }
        } catch (URISyntaxException e) {
            if (error != null) {
                error.call();
            }
        }
    }

    /**
     * Listen for general event on the socket.
     *
     * @param eventName event name
     * @param callback  callback
     * @see Socket list of event types to listen to
     */
    public void on(String eventName, EchoCallback callback) {
        socket.on(eventName, callback);
    }

    /**
     * Remove all listeners for a general event.
     *
     * @param eventName event name
     */
    public void off(String eventName) {
        socket.off(eventName);
    }

    /**
     * Listen for an event on a channel.
     *
     * @param channel  channel name
     * @param event    event name
     * @param callback callback
     * @return the channel
     */
    public SocketIOChannel listen(String channel, String event, EchoCallback callback) {
        return (SocketIOChannel) this.channel(channel).listen(event, callback);
    }

    @Override
    public AbstractChannel channel(String channel) {
        if (!channels.containsKey(channel)) {
            channels.put(channel, new SocketIOChannel(socket, channel, options));
        }
        return channels.get(channel);
    }

    @Override
    public AbstractChannel privateChannel(String channel) {
        String name = "private-" + channel;

        if (!channels.containsKey(name)) {
            channels.put(name, new SocketIOPrivateChannel(socket, name, options));
        }
        return channels.get(name);
    }

    @Override
    public AbstractChannel presenceChannel(String channel) {
        String name = "presence-" + channel;

        if (!channels.containsKey(name)) {
            channels.put(name, new SocketIOPresenceChannel(socket, name, options));
        }
        return channels.get(name);
    }

    @Override
    public void leave(String channel) {
        String privateChannel = "private-" + channel;
        String presenceChannel = "presence-" + channel;


        for (String subscribed : channels.keySet()) {
            if (subscribed.equals(channel) || subscribed.equals(privateChannel) || subscribed.equals(presenceChannel)) {
                try {
                    Log.e("삭제시 채널 훑어보기",channels.keySet().toString());
                    Log.e("삭제시 채널 훑어보기",subscribed);

                    Log.e("삭제시 채널 훑어보기",channels.entrySet().toString());
                    channels.get(subscribed).unsubscribe(null);
//                    channels.get(subscribed).select_unbind(null);
                    Log.e("삭제시 채널 훑어보기",channels.entrySet().toString());
                } catch (EchoException e) {
                    e.printStackTrace();
                }

                channels.remove(subscribed);
            }
        }
        Log.e("삭제시 채널 훑어보기",channels.keySet().toString());
    }

    @Override
    public boolean isConnected() {
        return socket.connected();
    }

    @Override
    public void disconnect() {
        for (String subscribed : channels.keySet()) {
            try {
                channels.get(subscribed).unsubscribe(null);
            } catch (EchoException e) {
                e.printStackTrace();
            }
        }

        channels.clear();
        socket.disconnect();
    }
    public boolean isChannelExists(String channel){

        for (String subscribed : channels.keySet()) {
            if (subscribed.equals(channel)) {
                return true;
            }
        }
        return false;
    }
}
