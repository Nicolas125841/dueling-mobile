package com.nickuribegames.ygomobileserver;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class DuelMap {

	ConDetails[] clients;
	
	ServerSocket server;
	Socket tmp;
	ArrayList<GameController> gc;
	
	public DuelMap(ArrayList<GameController> gcr, ConDetails[] c) throws IOException {
		
		this.gc = gcr;
		this.clients = c;
		server = new ServerSocket(1237);
		
		while(true) {
			
			tmp = server.accept();
			DataInputStream is = new DataInputStream(tmp.getInputStream());
			DataOutputStream os = new DataOutputStream(tmp.getOutputStream());
			while(is.available() == 0);
			int ind = Integer.parseInt(is.readUTF());
			if(clients[ind] == null) {
				clients[ind] = new ConDetails(tmp, os, is, clients, ind);		
				System.out.println(ind + "MAP");
			}else {
				gc.add(new GameController(tmp, clients[ind].sock, os, clients[ind].os, is, clients[ind].is, gc));
				clients[ind] = null;
			}			
			
		}
		
	}
	
}
