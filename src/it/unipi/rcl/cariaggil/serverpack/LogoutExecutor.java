package it.unipi.rcl.cariaggil.serverpack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;


public class LogoutExecutor implements Runnable
{
	UsersManager manager;
	Socket s;

	public LogoutExecutor(UsersManager users, Socket sock)
	{
		manager = users;
		s = sock;
	}

	@Override
	public void run()
	{
		BufferedReader reader = null;
		try
		{
			reader = new BufferedReader(new InputStreamReader(s.getInputStream()));

			String usr = reader.readLine();
			
			manager.logoutUser(usr);
		}
		catch (IOException e)
		{
			System.out.println("Errore durante l'esecuzione di LogoutExecutor");
		}
		finally
		{
			try
			{
				reader.close();
				s.close();
			}
			catch (IOException e)
			{
				System.out.println("Errore durante le operazioni di pulizia");
			}

		}
	}

}
