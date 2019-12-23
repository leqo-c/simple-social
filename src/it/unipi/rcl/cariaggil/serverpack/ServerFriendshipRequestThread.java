package it.unipi.rcl.cariaggil.serverpack;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import it.unipi.rcl.cariaggil.constants.Constants;

public class ServerFriendshipRequestThread implements Runnable
{
	FriendshipManager manager;
	UsersManager umanager;
	public ServerFriendshipRequestThread(FriendshipManager man, UsersManager uman)
	{
		manager = man;
		umanager = uman;
	}
	
	
	@Override
	public void run()
	{
		ServerSocket ss = null;
		try
		{
			ss = new ServerSocket(Constants.SERVER_FRIENDSHIP_REQUEST_PORT, 10, InetAddress.getLocalHost());
			
			while (true) // SERVER LOOP	
			{
				Socket s = ss.accept();
				Utils.updateOnlineUsers(umanager);
				Thread t = new Thread(new FriendshipRequestExecutor(manager, s, umanager));
				t.start();
			}
			
		}
		catch(IOException e)
		{
			System.out.println("Errore durante l'esecuzione di ServerFriendshipRequestThread");
		}
		finally
		{
			try
			{
				ss.close();
			}
			catch (IOException e) { }
		}
		
	}

}
