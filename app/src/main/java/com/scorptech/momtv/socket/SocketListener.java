package com.scorptech.momtv.socket;

/**
 * Created by talhahavadar on 01/05/2017.
 */

public interface SocketListener {
    void onData(Client client, String data);
}
