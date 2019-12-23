package it.unipi.rcl.cariaggil.serverpack;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import it.unipi.rcl.cariaggil.constants.Constants;

public class ServerPublishContentThread implements Runnable
{
	UsersManager usersManager;
	
	public ServerPublishContentThread(UsersManager uman)
	{
		usersManager = uman;
	}

	@Override
	public void run()
	{
		ServerSocket ss = null;
		Socket s = null;
		try
		{
			ss = new ServerSocket(Constants.SERVER_PUBLISH_PORT, 10, InetAddress.getLocalHost());
			
			while (true) // SERVER LOOP	
			{
				s = ss.accept();
				Utils.updateOnlineUsers(usersManager);
				Thread t = new Thread(new PublishContentExecutor(usersManager, s));
				t.start();
			}
			
		}
		catch(IOException e)
		{
			System.out.println("Errore durante l'esecuzione di ServerPublishContentThread");
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
