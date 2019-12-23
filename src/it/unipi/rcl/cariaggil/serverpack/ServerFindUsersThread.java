package it.unipi.rcl.cariaggil.serverpack;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import it.unipi.rcl.cariaggil.constants.Constants;

public class ServerFindUsersThread implements Runnable
{
	UsersManager man;
	
	public ServerFindUsersThread(UsersManager uman)
	{
		man = uman;
	}
	
	@Override
	public void run()
	{
		ServerSocket ss = null;
		Socket s = null;
		try
		{
			ss = new ServerSocket(Constants.SERVER_FIND_USERS_PORT, 10, InetAddress.getLocalHost());
			
			while (true) // SERVER LOOP	
			{
				s = ss.accept();
				Utils.updateOnlineUsers(man);
				Thread t = new Thread(new FindUsersExecutor(man, s));
				t.start();
			}
			
		}
		catch(IOException e)
		{
			System.out.println("Errore durante l'esecuzione di ServerFindUsersThread");
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
