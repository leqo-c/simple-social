package it.unipi.rcl.cariaggil.serverpack;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import it.unipi.rcl.cariaggil.constants.Constants;

public class ServerLoginThread implements Runnable
{
	UsersManager manager;
	
	public ServerLoginThread(UsersManager man)
	{
		manager = man;
	}

	@Override
	public void run()
	{
		ServerSocket ss = null;
		try
		{
			ss = new ServerSocket(Constants.SERVER_LOGIN_PORT, 10, InetAddress.getLocalHost());
			
			while (true) // SERVER LOOP	
			{
				Socket s = ss.accept();
				Thread t = new Thread(new LoginExecutor(manager, s));
				t.start();
			}
			
		}
		catch(IOException e)
		{
			System.out.println("Errore durante l'esecuzione di serverLoginThread");
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
