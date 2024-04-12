package com.nickuribegames.ygomobileserver;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class ConDetails {

	public Socket sock;
	public DataOutputStream os; 
	public DataInputStream is; 
	public ConDetails[] map;
	int index;
	String purpose = "normal";
	
	public ConDetails(ConDetails[] m, String p) {
		
		this.purpose = p;
		
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				try {
					Thread.sleep(300000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(m[index] != null && m[index].equals(this)) {
					System.out.println("Kicked Idle User");
					m[index] = null;
				}
				
			}		
			
			
		}).start();
		
	}
	
	public ConDetails(Socket s, DataOutputStream o, DataInputStream i, ConDetails[] m, int in) {
		
		this.sock = s;
		this.os = o;
		this.is = i;
		this.map = m;
		this.index = in;
		
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				try {
					Thread.sleep(300000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(m[index] != null && m[index].equals(this)) {
					System.out.println("Kicked Idle User");
					m[index] = null;
				}
				
			}		
			
			
		}).start();
		
	}
	
}
