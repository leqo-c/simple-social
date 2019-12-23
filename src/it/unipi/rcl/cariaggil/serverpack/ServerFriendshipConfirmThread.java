package it.unipi.rcl.cariaggil.serverpack;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import it.unipi.rcl.cariaggil.constants.Constants;

public class ServerFriendshipConfirmThread implements Runnable
{
	UsersManager usersManager;
	FriendshipManager friendsManager;

	public ServerFriendshipConfirmThread(UsersManager uman, FriendshipManager fman)
	{
		usersManager = uman;
		friendsManager = fman;
	}
	
	@Override
	public void run()
	{
		ServerSocket ss = null;
		Socket s = null;
		try
		{
			ss = new ServerSocket(Constants.SERVER_FRIENDSHIP_CONFIRM_PORT, 10, InetAddress.getLocalHost());
			
			while (true) // SERVER LOOP	
			{
				s = ss.accept();
				Utils.updateOnlineUsers(usersManager);
				Thread t = new Thread(new FriendshipConfirmExecutor(usersManager, friendsManager, s));
				t.start();
			}
			
		}
		catch(IOException e)
		{
			System.out.println("Errore durante l'esecuzione di ServerFriendshipConfirmThread");
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
