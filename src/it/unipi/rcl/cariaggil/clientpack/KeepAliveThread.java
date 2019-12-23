package it.unipi.rcl.cariaggil.clientpack;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;


import it.unipi.rcl.cariaggil.constants.Constants;

public class KeepAliveThread implements Runnable
{
	private String username;
	
	public KeepAliveThread(String user)
	{
		username = user;
	}
	
	@Override
	public void run()
	{
		MulticastSocket multisock = null;
		DatagramSocket datasock = null;
		try
		{
			// Il client aderisce al gruppo di multicast.
			
			InetAddress group = InetAddress.getByName(Constants.MULTICAST_KEEPALIVE);
			int port = Constants.MULTICAST_KEEPALIVE_PORT;
			multisock = new MulticastSocket(port);
			multisock.joinGroup(group); 
			
			datasock = new DatagramSocket();
			
			while (true)
			{
				// Lettura dal gruppo di multicast del messaggio di keepalive.
				
				byte[] receiveBuffer = new byte[512];
				DatagramPacket pk = new DatagramPacket(receiveBuffer, receiveBuffer.length);
				multisock.receive(pk);
				
				// Rispondo al server (UDP)
				
				byte[] toSend = username.getBytes();
				DatagramPacket sendpack = new DatagramPacket(toSend, toSend.length, 
						InetAddress.getLocalHost(), //localhost
						Constants.SERVER_KEEPALIVE_RECEIVER_PORT);
				datasock.send(sendpack);
			}

		}
		catch (IOException e)
		{
			System.out.println("Errore durante l'esecuzione di KeepAliveThread");
			e.printStackTrace();
		}
		finally
		{
			if(multisock != null) multisock.close();
			if(datasock != null) datasock.close();
		}
		
	}

}
