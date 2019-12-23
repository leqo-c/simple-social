package it.unipi.rcl.cariaggil.serverpack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

import it.unipi.rcl.cariaggil.clientpack.IClientService;

public class PublishContentExecutor implements Runnable
{
	UsersManager man;
	Socket s;

	public PublishContentExecutor(UsersManager usersManager, Socket sock)
	{
		man = usersManager;
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
			String message = reader.readLine();

			User u = man.getMatchingUser(usr);
			
			if(u == null) return;
			
			ArrayList<User> follwers = u.getFollowers();
			
			System.out.println(u.getUsername() + " ha pubblicato '" + message + "'");

			for(User f : follwers)
			{
				IClientService st = f.getStub();				
				
				// Se l'utente è online gli invio i contenuti. Altrimenti, se è offline,
				// memorizzo i contenuti.
				
				if(f.isUserOnline())
				{
					if(st != null)
						st.AggiungiContenuto(message);
					else
						f.storeContent(message);
				}
				else
				{
					f.storeContent(message);
				}
				
			}
		}	
		catch (IOException e)
		{
			System.out.println("Errore durante l'esecuzione di PublishContentExecutor");
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
