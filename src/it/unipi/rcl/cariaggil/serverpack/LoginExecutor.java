package it.unipi.rcl.cariaggil.serverpack;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import it.unipi.rcl.cariaggil.constants.Constants;

public class LoginExecutor implements Runnable
{
	UsersManager manager;
	Socket s;

	public LoginExecutor(UsersManager man, Socket sock)
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

			String usr = reader.readLine();
			String pass = reader.readLine();

			w = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
			
			boolean res = manager.logUser(usr, pass);

			if (res) // Genera oAuth e lo scrive su w
			{
				w.write(Constants.getNextUid());
				w.newLine();
				w.flush();
			}
			else // Login fallito, scrive -1
			{
				w.write("-1");
				w.newLine();
				w.flush();
			}

		}
		catch (IOException e)
		{
			System.out.println("Errore durante l'esecuzione di LoginExecutor");
		}
		finally
		{
			try
			{
				w.close();
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
