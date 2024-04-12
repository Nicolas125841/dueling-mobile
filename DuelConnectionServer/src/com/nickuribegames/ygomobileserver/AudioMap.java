package com.nickuribegames.ygomobileserver;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class AudioMap {

	ConDetails[] clients;
	
	ServerSocket server;
	Socket tmp;
	ArrayList<AudioController> ac;
	
	public AudioMap(ArrayList<AudioController> gcr, ConDetails[] threads) throws IOException {
		
		this.ac = gcr;
		this.clients = threads;
		server = new ServerSocket(1238);
		
		while(true) {
			
			tmp = server.accept();
			DataInputStream is = new DataInputStream(tmp.getInputStream());
			DataOutputStream os = new DataOutputStream(tmp.getOutputStream());
			while(is.available() == 0);
			int ind = Integer.parseInt(is.readUTF());
			if(clients[ind] == null || clients[ind].purpose != "normal") {
				clients[ind] = new ConDetails(tmp, os, is, clients, ind);		
				System.out.println(ind);
			}else {
				ac.add(new AudioController(tmp, clients[ind].sock, os, clients[ind].os, is, clients[ind].is, ac));
				clients[ind] = null;
			}			
			
		}
		
	}
	
}
