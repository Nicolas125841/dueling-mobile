package com.nickuribegames.ygomobileserver;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;


public class GameController {

	Socket player1; 
	Socket player2;
	DataOutputStream os1; 
	DataOutputStream os2;
	DataInputStream is1; 
	DataInputStream is2;
	GameController realThis;
	String buffer;
	String buffer2;
	
	public GameController(Socket p1, Socket p2, DataOutputStream o1, DataOutputStream o2, DataInputStream i1, DataInputStream i2, ArrayList<GameController> games) {
		
		this.player1 = p1;
		this.player2 = p2;
		this.os1 = o1;
		this.os2 = o2;
		this.is1 = i1;
		this.is2 = i2;
		this.realThis = this;
		
		try {
			player1.setSoTimeout(600000);
			player2.setSoTimeout(600000);
		} catch (SocketException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}		
		
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				while(true) {
				try {					
					buffer = is1.readUTF();
					System.out.println(buffer);
					if(buffer.equals("ENDTHEGAME")) {
						os2.writeUTF(buffer);
						os2.flush();
						player1.close();
						player2.close();
						player1 = null;
						player2 = null;
						games.remove(realThis);
						return;
						
					}else {
					os2.writeUTF(buffer);
					os2.flush();
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block		
					
					try {
						
						os2.writeUTF("ENDTHEGAME");
						os2.flush();
						player1.close();
						player2.close();
						player1 = null;
						player2 = null;
						
					}catch(Exception ex) {
						
						try {
							
							player1.close();
							player2.close();
							player1 = null;
							player2 = null;
							
						}catch(Exception exe) {
							
							player1 = null;
							player2 = null;
							
						}
						
					}
					
					games.remove(realThis);
					return;
				}
				}
			}			
			
		}).start();
		
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				while(true) {
				try {
					buffer2 = is2.readUTF();
					System.out.println(buffer2);
					if(buffer2.equals("ENDTHEGAME")) {
						os1.writeUTF(buffer2);
						os1.flush();
						player1.close();
						player2.close();
						player1 = null;
						player2 = null;
						games.remove(realThis);
						return;
						
					}else {
					os1.writeUTF(buffer2);
					os1.flush();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block	
					
						try {
						
						os1.writeUTF("ENDTHEGAME");
						os1.flush();
						player1.close();
						player2.close();
						player1 = null;
						player2 = null;
						
					}catch(Exception ex) {
						
						try {
							
							player1.close();
							player2.close();
							player1 = null;
							player2 = null;
							
						}catch(Exception exe) {
							
							player1 = null;
							player2 = null;
							
						}
						
					}
					
					games.remove(realThis);
					return;
				}
				}
			}					
			
		}).start();
		
	}
	
	
}
