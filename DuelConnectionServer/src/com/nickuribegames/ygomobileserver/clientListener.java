package com.nickuribegames.ygomobileserver;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class clientListener {
		
	public static void main(String[] args) throws IOException {
		
		ConDetails[] connections = new ConDetails[100];
		ConDetails[] audioThreads = new ConDetails[100];
		ArrayList<DuelConnection> watchers = new ArrayList<DuelConnection>();
		ArrayList<DuelConnection> hosts = new ArrayList<DuelConnection>();
		Socket client = null;
		//Scanner s = new Scanner(System.in);
		ArrayList<GameController> activeDuels = new ArrayList<GameController>();	
		ArrayList<AudioController> activeTeleComs = new ArrayList<AudioController>();
		
		@SuppressWarnings("resource")
		ServerSocket server = new ServerSocket(1236);	
		
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					new NewCardHandleRequest();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}			
						
		}).start();
		
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					new DuelMap(activeDuels, connections);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}).start();
		
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					new AudioMap(activeTeleComs, audioThreads);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}).start();		
				
		while(true) {					
			
			client = server.accept();
			
			new DuelConnection(client, watchers, hosts, activeDuels, connections, audioThreads).start();
			
		}
		
		//server.close();
		
    }	

}
