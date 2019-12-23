package it.unipi.rcl.cariaggil.serverpack;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import it.unipi.rcl.cariaggil.constants.Constants;

public class ServerRegistrationThread implements Runnable
{
	UsersManager manager;
	
	public ServerRegistrationThread(UsersManager man)
	{
		manager = man;
	}
	
	@Override
	public void run()
	{
		ServerSocket ss = null;
		try
		{
			ss = new ServerSocket(Constants.SERVER_REGISTRATION_PORT, 10, InetAddress.getLocalHost());
			
			while (true) // SERVER LOOP	
			{
				Socket s = ss.accept();
				Utils.updateOnlineUsers(manager);
				Thread t = new Thread(new RegistrationExecutor(manager, s));
				t.start();
			}
			
		}
		catch(Exception e)
		{
			System.out.println("Errore durante l'esecuzione di ServerRegistrationThread");
			e.printStackTrace();
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
