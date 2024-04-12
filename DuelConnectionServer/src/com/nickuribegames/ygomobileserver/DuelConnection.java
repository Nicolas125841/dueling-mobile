package com.nickuribegames.ygomobileserver;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class DuelConnection extends Thread {
		
	ConDetails[] map;
	ConDetails[] amap;
	ArrayList<DuelConnection> watchers = null;
	ArrayList<DuelConnection> hosts = null;
	ArrayList<GameController> activeDuels;		
	Socket player = null;
	DataInputStream is = null;
	DataOutputStream os = null;	
	String adress = "";
	String realAdr = "";
	boolean going = false;
	
	public DuelConnection(Socket c, ArrayList<DuelConnection> w, ArrayList<DuelConnection> h, ArrayList<GameController> ad, ConDetails[] cli, ConDetails[] au) throws IOException {
		
		this.map = cli;
		this.amap = au;
		watchers = w;
		hosts = h;
		player = c;
		is = new DataInputStream(c.getInputStream());
		os = new DataOutputStream(c.getOutputStream());	
		activeDuels = ad;
		
	}	
	
	@Override
	public void run() {
		System.out.println("A user connected");
		watchers.add(this);
		try {
			os.writeUTF(makeArray(hosts));
			os.flush();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		beginServer();
	
	}
	
	public void beginServer() {
		

		try {
			
			while(is.available() == 0) {				
				
				try {
						
					os.writeUTF("@");
					Thread.sleep(1);
					
				}catch(Exception e) {
					
					watchers.remove(this);
					is.close();
					os.flush();
					os.close();
					player.close();
					System.out.println("Bye By Mistake");
					return;
					
				}
				
				
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			
			String buffer = is.readUTF();
			if(buffer.equals("QUIT")) {
				watchers.remove(this);
				is.close();
				os.flush();
				os.close();
				player.close();
				System.out.println("Bye");
				return;
			}else if(buffer.equals("PING")) {
				System.out.println("ping");
				
			}else if(buffer.equals("HOST")){
				while(is.available() == 0) {Thread.sleep(1);};
				this.adress = is.readUTF();
				if(!checkIfAvailable(this.adress, hosts)) {
					
					beginServer();
					return;
					
				}else {
				this.realAdr = player.getInetAddress().getHostAddress();
				hosts.add(this);
				System.out.println("Sent " + adress);
				System.out.println(watchers.size() + "");
				for(int i = 0; i < watchers.size(); i++){
					try {
					watchers.get(i).os.writeUTF(makeArray(hosts));
					watchers.get(i).os.flush();
					}catch(Exception e) {
						
						watchers.get(i).is.close();
						watchers.get(i).os.flush();
						watchers.get(i).os.close();
						watchers.get(i).player.close();
						watchers.remove(i);
						System.out.println("kickuser");
						
					}
				}
				os.writeUTF("HOSTCONFIRM");
				os.flush();
				
				}						
						
				try {
				while(!going && is.read() != -1);
				}catch(Exception e) {
					
					
				}
					//System.out.println(is.read() + "");
				
					if(!going) {
					watchers.remove(this);
					hosts.remove(this);
					is.close();
					os.flush();
					os.close();
					player.close();
					for(int i = 0; i < watchers.size(); i++){
						try {
						watchers.get(i).os.writeUTF(makeArray(hosts));
						watchers.get(i).os.flush();
						}catch(Exception ex) {
							
							watchers.get(i).is.close();
							watchers.get(i).os.flush();
							watchers.get(i).os.close();
							watchers.get(i).player.close();
							watchers.remove(i);
							System.out.println("kickuser");
							
						}
					}
					System.out.println("Bye host");
					}
					return;
					
							
					
				
					
			}else {
				
				System.out.println("JOIN");
				DuelConnection hostTarget = findHost(buffer, hosts);
				//String targetAdr = hostTarget.player.getInetAddress().getHostAddress();
				int indexToUse = getOpenIndex();
				amap[indexToUse] = new ConDetails(amap, "placeholder");
				hostTarget.going = true;
				hostTarget.os.writeUTF("JOININGGAME" + indexToUse);
				hostTarget.os.flush();				
				os.writeUTF("JOININGGAME" + indexToUse);
				os.flush();
				watchers.remove(this);				
				watchers.remove(hostTarget);
				hosts.remove(hostTarget);				
				is.close();				
				os.close();
				is = null;
				os = null;
				player.close();
				hostTarget.os.close();
				hostTarget.is.close();
				hostTarget.os = null;
				hostTarget.is = null;
				hostTarget.player.close();
				for(int i = 0; i < watchers.size(); i++){
					try {
					watchers.get(i).os.writeUTF(makeArray(hosts));
					watchers.get(i).os.flush();
					}catch(Exception e) {
						
						watchers.get(i).is.close();
						watchers.get(i).os.flush();
						watchers.get(i).os.close();
						watchers.get(i).player.close();
						watchers.remove(i);
						System.out.println("kickuser");
						
					}
				}
				System.out.println("Bye user");
				System.out.println("Game Start");
				return;
			}		
				
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
	}
	
	public int getOpenIndex() {
		
		for(int i = 0; i < map.length; i++) {
			
			if(map[i] == null && amap[i] == null)
				return i;
			
		}
		
		return -1;
		
	}
	
	public boolean checkIfAvailable(String nameToCheck, ArrayList<DuelConnection> dc) {
		
		for(int i = 0; i < dc.size(); i++) {
			
			if(dc.get(i).adress.equals(nameToCheck))
				return false;
			
		}
		
		return true;
		
	}
	
	public DuelConnection findHost(String name, ArrayList<DuelConnection> dc) {
		
		for(int i = 0; i < dc.size(); i++) {
			
			if(dc.get(i).adress.equals(name))
				return dc.get(i);
			
		}
		
		return null;
		
	}
	
	public String makeArray(ArrayList<DuelConnection> dc) {
		String ret = "";
		for(int i = 0; i < dc.size(); i++) {
			
			ret += dc.get(i).adress + "/";
			
		}
		return ret;		
	}
	
}