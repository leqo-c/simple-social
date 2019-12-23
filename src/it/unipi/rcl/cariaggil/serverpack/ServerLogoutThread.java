package it.unipi.rcl.cariaggil.serverpack;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import it.unipi.rcl.cariaggil.constants.Constants;

public class ServerLogoutThread implements Runnable
{
	UsersManager manager;
	
	public ServerLogoutThread(UsersManager users)
	{
		manager = users;
	}

	@Override
	public void run()
	{
		ServerSocket ss = null;
		try
		{
			ss = new ServerSocket(Constants.SERVER_LOGOUT_PORT, 10, InetAddress.getLocalHost());
			
			while (true) // SERVER LOOP	
			{
				Socket s = ss.accept();
				Utils.updateOnlineUsers(manager);
				Thread t = new Thread(new LogoutExecutor(manager, s));
				t.start();
			}
			
		}
		catch(IOException e)
		{
			System.out.println("Errore durante l'esecuzione di serverLogoutThread");
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
