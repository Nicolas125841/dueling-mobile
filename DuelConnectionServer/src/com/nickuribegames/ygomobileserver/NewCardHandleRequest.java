package com.nickuribegames.ygomobileserver;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;

import javax.imageio.ImageIO;

public class NewCardHandleRequest {

	ServerSocket requestListener;
	Socket requestMain;	
	
	public NewCardHandleRequest() throws IOException{
		
		requestListener = new ServerSocket(1240);
		
		while(true) {
			
			requestMain = requestListener.accept();
			//TODO: Print to see what works
			System.out.println("New Request");
			
			new Thread(new Runnable() {	

				@Override
				public void run() {
					// TODO Auto-generated method stub
					
					try {
					
						final Socket request = requestMain;
						DataInputStream requestStream = new DataInputStream(request.getInputStream());
						String cardId = requestStream.readUTF();
						requestStream.close();
						request.close();								
						URL imageServerURL = new URL("https://storage.googleapis.com/ygoprodeck.com/pics/" + cardId + ".jpg");
						HttpURLConnection connectionToImageServer = (HttpURLConnection) imageServerURL.openConnection();
						connectionToImageServer.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
						connectionToImageServer.setRequestMethod("GET");
						connectionToImageServer.connect();
						InputStream incomingImage = connectionToImageServer.getInputStream();
						File cardOutFile = new File("C:/wamp64/www/photos/" + cardId + ".jpg");
								
						if(!cardOutFile.exists()) {
									
							cardOutFile.createNewFile();
							FileOutputStream fileOut = new FileOutputStream(cardOutFile);
							byte[] imgBuffer = new byte[2048];
							int length = 0;
								
							while((length = incomingImage.read(imgBuffer)) != -1) {
									
								fileOut.write(imgBuffer, 0, length);
									
							}
								
							fileOut.flush();
							fileOut.close();
								
						}
								
						incomingImage.close();
						connectionToImageServer.disconnect();
						imageServerURL = null;
								
						System.out.println("Created Image " + cardId + " Large");
						
						BufferedImage originalImage = ImageIO.read(new File("C:/wamp64/www/photos/" + cardId + ".jpg"));						
						int type = originalImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : originalImage.getType();

						BufferedImage resizedImage = new BufferedImage(originalImage.getWidth()/10, originalImage.getHeight()/10, type);
						Graphics2D g = resizedImage.createGraphics();
						g.drawImage(originalImage, 0, 0, originalImage.getWidth()/10, originalImage.getHeight()/10, null);
						g.dispose();						
						
						ImageIO.write(resizedImage, "jpg", new File("C:/wamp64/www/photosSm/" + cardId + ".jpg"));

						
						
						/*
								
						imageServerURL = new URL("https://storage.googleapis.com/ygoprodeck.com/pics_small/" + cardId + ".jpg");
						connectionToImageServer = (HttpURLConnection) imageServerURL.openConnection();
						connectionToImageServer.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
						connectionToImageServer.setRequestMethod("GET");
						connectionToImageServer.connect();
						incomingImage = connectionToImageServer.getInputStream();
						cardOutFile = new File("C:/wamp64/www/photosSm/" + cardId + ".jpg");
					
						if(!cardOutFile.exists()) {
									
							cardOutFile.createNewFile();
							FileOutputStream fileOut = new FileOutputStream(cardOutFile);
							byte[] imgBuffer = new byte[2048];
							int length = 0;
								
							while((length = incomingImage.read(imgBuffer)) != -1) {
									
								fileOut.write(imgBuffer, 0, length);
									
							}
								
							fileOut.flush();
							fileOut.close();
								
						}
								
						incomingImage.close();
						connectionToImageServer.disconnect();
						imageServerURL = null;*/
								
						System.out.println("Created Image " + cardId + " Small");
								
								
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
							
				}				
										
			}).start();
					
		}			
			
			
	}

}
