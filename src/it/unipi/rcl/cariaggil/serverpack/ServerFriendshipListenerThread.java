package it.unipi.rcl.cariaggil.serverpack;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import it.unipi.rcl.cariaggil.constants.Constants;

public class ServerFriendshipListenerThread implements Runnable
{
	UsersManager manager;
	
	public ServerFriendshipListenerThread(UsersManager man)
	{
		manager = man;
	}
	
	@Override
	public void run()
	{
		ServerSocket ss = null;
		Socket s = null;
		try
		{
			ss = new ServerSocket(Constants.SERVER_FRIENDSHIP_LISTENING_PORT, 10, InetAddress.getLocalHost());
			
			while (true) // SERVER LOOP	
			{
				s = ss.accept();
				Thread t = new Thread(new FriendshipListenerExecutor(manager, s));
				t.start();
			}
			
		}
		catch(IOException e)
		{
			System.out.println("Errore durante l'esecuzione di ServerFriendshipListenerThread");
		}
		finally
		{
			try
			{
				ss.close();
			}
			catch (IOException e)
			{
				System.out.println("Errore durante le operazioni di pulizia");
			}
			
		}
	}

}
