package com.psquaredsoftware.ygomobile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketTransfer implements Serializable {

    transient DataInputStream is;
    transient DataOutputStream os;
    transient ServerSocket host;
    transient Socket client;
    transient Socket player;

    public SocketTransfer(DataOutputStream o, DataInputStream i, Socket cl, ServerSocket h){
        is = i;
        os = o;
        client = cl;
        host = h;
    }

    public SocketTransfer(DataOutputStream o, DataInputStream i, Socket p){
        is = i;
        os = o;
        player = p;
    }

    public void setClient(Socket client) {
        this.client = client;
    }

    public void setHost(ServerSocket host) {
        this.host = host;
    }

    public void setIs(DataInputStream is) {
        this.is = is;
    }

    public void setOs(DataOutputStream os) {
        this.os = os;
    }

    public void setPlayer(Socket player) {
        this.player = player;
    }

    public DataOutputStream getOs() {
        return os;
    }

    public DataInputStream getIs(){
        return is;
    }

    public Socket getClient() {
        return client;
    }

    public Socket getPlayer() {
        return player;
    }

    public ServerSocket getHost() {
        return host;
    }

}
