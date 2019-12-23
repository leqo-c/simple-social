package it.unipi.rcl.cariaggil.serverpack;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.unipi.rcl.cariaggil.constants.Constants;

public class ServerKeepAliveThread implements Runnable
{
	UsersManager man;
	
	public ServerKeepAliveThread(UsersManager m)
	{
		man = m;
	}

	@Override
	public void run()
	{
		DatagramSocket datasock = null;
		DatagramSocket s = null;
		List<String> onlineUsers = new ArrayList<String>();
		List<String> oldOnlineUsers = new ArrayList<String>();
		try
		{
			datasock = new DatagramSocket();
			byte[] toSend = "KEEPALIVE".getBytes();
			DatagramPacket sendpack = new DatagramPacket(toSend, toSend.length, 
					InetAddress.getByName(Constants.MULTICAST_KEEPALIVE), 
					Constants.MULTICAST_KEEPALIVE_PORT);
			
			// Manda il messaggio ogni 10 secondi, aspetta le risposte e aggiorna la
			// lista degli utenti online.
			
			while(true)
			{
				Thread.sleep(10000);
				s = new DatagramSocket(Constants.SERVER_KEEPALIVE_RECEIVER_PORT);
				
				// "Fotografia" degli utenti attualmente online
				oldOnlineUsers = man.getOnlineUsers();
				
				datasock.send(sendpack);
				
				// Leggo le risposte
				
				byte[] receivedMessage;
				DatagramPacket receivepack;
				
				Date now = new Date();
				int timeout = 10000; 
				do
				{
					try 
					{
						receivedMessage = new byte[256];
						receivepack = new DatagramPacket(receivedMessage, receivedMessage.length);
						
						s.setSoTimeout(timeout);
						s.receive(receivepack);

						String aUser = new String(receivepack.getData());
						aUser = aUser.trim();
						onlineUsers.add(aUser);
						
						timeout -= new Date().getTime() - now.getTime();
						
						if(timeout < 1) 
							timeout = 1;
					} 	
					catch(SocketTimeoutException e){ break; }
					
				} while(true);
				
				s.close();
				
				//Stampa anche il numero di utenti rimasti online.
				
				man.UpdateOnlineUsers(onlineUsers, oldOnlineUsers);
				onlineUsers.clear();
				oldOnlineUsers.clear();
			}
		}
		catch (IOException e)
		{
			System.out.println("Errore durante l'esecuzione di ServerKeepAliveThread");
			e.printStackTrace();
		}
		catch (InterruptedException e)
		{
			System.out.println("Errore durante la sleep di 10 secondi");
		}
		finally
		{
			datasock.close();
			s.close();
		}
	}

}
