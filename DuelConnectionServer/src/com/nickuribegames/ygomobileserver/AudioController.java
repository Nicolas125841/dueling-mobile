package com.nickuribegames.ygomobileserver;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class AudioController {

	byte[] buf1 = new byte[1024];
	byte[] buf2 = new byte[1024];
	Socket player1; 
	Socket player2;
	DataOutputStream os1; 
	DataOutputStream os2;
	DataInputStream is1; 
	DataInputStream is2;
	AudioController realThis;
	int len;
	int len2;
	
	public AudioController(Socket p1, Socket p2, DataOutputStream o1, DataOutputStream o2, DataInputStream i1, DataInputStream i2, ArrayList<AudioController> games) {
		
		System.out.println("AudioStart");
		this.player1 = p1;
		this.player2 = p2;
		this.os1 = o1;
		this.os2 = o2;
		this.is1 = i1;
		this.is2 = i2;
		this.realThis = this;
		
		try {
			player1.setSoTimeout(10000);
			player2.setSoTimeout(10000);
		} catch (SocketException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}	
		
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				while(true) {
				try {				
					//while(is1.available() < 1024);
					len = is1.read(buf1, 0, 1024);
					os2.write(buf1, 0, len);
					if(new String(buf1).contains("ENDCOMM")) {
						System.out.println("END OF VOICE");
						player1.close();
						player2.close();
						player1 = null;
						player2 = null;
						games.remove(realThis);
						return;
						
					}
					//buf1 = new byte[1024];
				} catch (Exception e) {
					// TODO Auto-generated catch block	
					System.out.println("FAIL");
					//e.printStackTrace();
					try {
						player1.close();
						player2.close();
						player1 = null;
						player2 = null;
					} catch (Exception e1) {						
						try {
							player1.close();
							player2.close();
							player1 = null;
							player2 = null;
						} catch (IOException e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
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
					len2 = is2.read(buf2, 0, 1024);
					os1.write(buf2, 0, len2);
					if(new String(buf2).contains("ENDCOMM")) {
						System.out.println("END OF VOICE");
						player1.close();
						player2.close();
						player1 = null;
						player2 = null;
						games.remove(realThis);
						return;						
					}
					//dbuf2 = new byte[1024];
				} catch (Exception e) {
					// TODO Auto-generated catch block	
					//e.printStackTrace();
					try {
						player1.close();
						player2.close();
						player1 = null;
						player2 = null;
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						try {
							player1.close();
							player2.close();
							player1 = null;
							player2 = null;
						}catch(Exception ex) {
							
						}
						e1.printStackTrace();
					}
					games.remove(realThis);
					return;
				}
				}
			}					
			
		}).start();
		
	}
	
	
}