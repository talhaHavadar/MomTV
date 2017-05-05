package com.scorptech.momtv.socket;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by talhahavadar on 22/04/2017.
 */

public class TCPClient extends Client implements Client.ITCPClient {
    private static final String TAG = "TCPClient";

    private PrintWriter out;
    private BufferedReader in;
    private boolean mRun;

    public TCPClient(String ip, int port) {
        super(ip, port);
        mRun = false;
    }

    @Override
    protected TCPClient doInBackground(Object... voids) {
        try {
            Socket socket = null;
            try {
                InetAddress address = InetAddress.getByName(this.sourceAddress);
                socket = new Socket(address, this.sourcePort);

                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String line = null;
                while(mRun) {
                    line = in.readLine();
                    if(line != null && mListener != null) {
                        mListener.onData(this, line);
                    }
                }

            } catch (UnknownHostException e) {
                e.printStackTrace();
            } finally {
                out.flush();
                out.close();
                in.close();
                socket.close();
                Log.d(TAG, "Socket done.");
            }
        } catch (Exception e) {
            Log.e(TAG, "ERROR", e);
        }

        return this;
    }

    @Override
    public void connect(Object... params) {
        mRun = true;
        this.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void setServerIP(String ip) {
        this.sourceAddress = ip;
    }

    public void send(String message) {
        if(out != null && !out.checkError()) {
            out.println(message);
            out.flush();
            Log.d(TAG, "send: '" + message + "'");
        } else {
            Log.e(TAG, "send: null");
        }
    }

    @Override
    public void close() {
        mRun = false;
    }

}
