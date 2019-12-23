package it.unipi.rcl.cariaggil.serverpack;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class RegistrationExecutor implements Runnable
{
	UsersManager manager;
	Socket s;

	public RegistrationExecutor(UsersManager man, Socket sock)
	{
		manager = man;
		s = sock;
	}

	@Override
	public void run()
	{
		BufferedReader reader = null;
		BufferedWriter w = null;
		try
		{
			reader = new BufferedReader(new InputStreamReader(s.getInputStream()));

			String username = reader.readLine();
			System.out.flush();
			String password = reader.readLine();
			System.out.flush();

			w = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));

			// Registrazione dell'utente.
			
			boolean res = manager.addUser(new User(username, password));

			if (res)
			{
				w.write("Utente " + username + " registrato con successo");
				w.newLine();
			}
			else
			{
				w.write("Utente non valido o già presente");
				w.newLine();
			}
			w.flush();
		}

		catch (IOException e)
		{
			System.out.println("Errore durante l'operazione di registrazione");
		}
		
		finally
		{
			try
			{
				if(reader != null) reader.close();
				if(w != null) w.close();
				if(s != null) s.close();
			}
			catch (IOException e) { }
		}

	}

}
