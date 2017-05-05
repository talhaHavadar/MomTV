package com.scorptech.momtv.socket;

import android.os.AsyncTask;

/**
 * Created by talhahavadar on 14/01/2017.
 */

public abstract class Client extends AsyncTask<Object, Void, Client> {

    SocketListener mListener;
    String sourceAddress;
    int sourcePort;

    public Client(String sourceAddress, int sourcePort) {
        this.sourceAddress = sourceAddress;
        this.sourcePort = sourcePort;
    }

    @Override
    protected abstract Client doInBackground(Object... voids);

    public abstract void connect(Object... params);

    public void setSocketListener(SocketListener listener) {
        this.mListener = listener;
    }

    public interface IUDPClient {
        void send(String data, String toServer, int port);
    }

    public interface ITCPClient {
        void send(String data);
        void close();
    }
}
