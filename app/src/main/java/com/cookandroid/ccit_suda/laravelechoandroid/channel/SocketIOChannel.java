/*
 * SocketIOChannel.java
 * MrBin99 © 2018
 */
package com.cookandroid.ccit_suda.laravelechoandroid.channel;

import android.util.Log;

import com.cookandroid.ccit_suda.laravelechoandroid.EchoCallback;
import com.cookandroid.ccit_suda.laravelechoandroid.EchoException;
import com.cookandroid.ccit_suda.laravelechoandroid.EchoOptions;
import com.cookandroid.ccit_suda.laravelechoandroid.util.EventFormatter;

import io.socket.client.Socket;

import org.json.JSONObject;

import java.util.*;

/**
 * This class represents a Socket.io channel.
 */
public class SocketIOChannel extends AbstractChannel {

    /**
     * The socket.
     */
    protected Socket socket;

    /**
     * The channel name.
     */
    private String name;

    /**
     * Echo options.
     */
    protected EchoOptions options;

    /**
     * Event formatter.
     */
    protected EventFormatter formatter;

    /**
     * Events callback.
     */
    private Map<String, List<EchoCallback>> eventsCallbacks;

    /**
     * Create a new Socket.IO channel.
     *
     * @param socket  the socket
     * @param name    channel name
     * @param options Echo options
     */
    public SocketIOChannel(Socket socket, String name, EchoOptions options) {
        this.socket = socket;
        this.name = name;
        this.options = options;
        this.formatter = new EventFormatter(options.eventNamespace);
        this.eventsCallbacks = new HashMap<>();

        try {
            this.subscribe(null);
        } catch (EchoException e) {
            e.printStackTrace();
        }

        configureReconnector();
    }

    /**
     * Subscribe to a Socket.io channel.
     *
     * @param callback callback with response from the server
     * @throws EchoException if error when subscribing to the channel.
     */
    public void subscribe(EchoCallback callback) throws EchoException {
        JSONObject object = new JSONObject();
        try {
            object.put("channel", name);
            object.put("auth", options.getAuth());

            if (callback == null) {
                socket.emit("subscribe", object);
            } else {
                socket.emit("subscribe", object, callback);
            }

        } catch (Exception e) {
            throw new EchoException("Cannot subscribe to channel '" + name + "' : " + e.getMessage());
        }
    }

    /**
     * Unsubscribe from channel and unbind event callbacks.
     *
     * @param callback callback with response from the server
     * @throws EchoException if error when unsubscribing to the channel.
     */
    public void unsubscribe(EchoCallback callback) throws EchoException {
        unbind();

        JSONObject object = new JSONObject();
        try {
            object.put("channel", name);
            object.put("auth", options.getAuth());

            if (callback == null) {
                socket.emit("unsubscribe", object);
            } else {
                socket.emit("unsubscribe", object, callback);
            }

        } catch (Exception e) {
            throw new EchoException("Cannot unsubscribe to channel '" + name + "' : " + e.getMessage());
        }
    }

    @Override
    public AbstractChannel listen(String event, EchoCallback callback) {
        on(formatter.format(event), callback);

        return this;
    }

    /**
     * Bind the channel's socket to an event and store the callback.
     *
     * @param event    event name
     * @param callback callback
     */
    public void on(String event, final EchoCallback callback) {
        EchoCallback listener = new EchoCallback() {
            @Override
            public void call(Object... objects) {
                if (objects.length > 0 && objects[0] instanceof String) {
                    String channel = (String) objects[0];

                    if (channel.equals(name)) {
                        callback.call(objects);
                    }
                }
            }
        };

        socket.on(event, listener);
        bind(event, listener);
    }

    /**
     * Attach a 'reconnect' listener and bind the event.
     */
    private void configureReconnector() {
        EchoCallback callback = new EchoCallback() {
            @Override
            public void call(Object... objects) {
                try {
                    subscribe(null);
                } catch (EchoException e) {
                    e.printStackTrace();
                }
            }
        };

        socket.on(Socket.EVENT_RECONNECT, callback);
        bind(Socket.EVENT_RECONNECT, callback);
    }

    /**
     * Bind the channel's socket to an event and store the callback.
     *
     * @param event    event name
     * @param callback callback when event is triggered
     */
    public void bind(String event, EchoCallback callback) {
        if (!eventsCallbacks.containsKey(event)) {
            eventsCallbacks.put(event, new ArrayList<EchoCallback>());
        }

        eventsCallbacks.get(event).add(callback);
    }

    /**
     * Unbind the channel's socket from all stored event callbacks.
     */
    public void unbind() {
        Iterator<String> iterator = eventsCallbacks.keySet().iterator();
        Log.e("이벤트 콜백",String.valueOf(eventsCallbacks.size()));
        Log.e("이벤트 콜백",eventsCallbacks.keySet().toString());
        while (iterator.hasNext()) {
            Log.e("이벤트 콜백",iterator.next());
            socket.off(iterator.next());
            iterator.remove();
        }
    }

    /**
     * @return the channel name.
     */
    public String getName() {
        return name;
    }

    public void select_unbind(EchoCallback callback) throws EchoException {


        JSONObject object = new JSONObject();
        try {
            object.put("channel", name);
            object.put("auth", options.getAuth());

            if (callback == null) {
                socket.emit("unsubscribe", object);
            } else {
                socket.emit("unsubscribe", object, callback);
            }

        } catch (Exception e) {
            throw new EchoException("Cannot unsubscribe to channel '" + name + "' : " + e.getMessage());
        }
    }
}
