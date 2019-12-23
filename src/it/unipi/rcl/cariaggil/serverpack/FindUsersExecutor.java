package it.unipi.rcl.cariaggil.serverpack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class FindUsersExecutor implements Runnable
{
	UsersManager users;
	Socket s;
	
	public FindUsersExecutor(UsersManager man, Socket sock)
	{
		users = man;
		s = sock;
	}

	@Override
	public void run()
	{
		BufferedReader r = null;
		ObjectOutputStream out = null;
		try
		{
			r = new BufferedReader(new InputStreamReader(s.getInputStream()));
			String nameToFind = r.readLine();
			
			ArrayList<String> res = users.findUsers(nameToFind);
			out = new ObjectOutputStream(s.getOutputStream());
			out.writeObject(res);
		}
		catch (IOException e)
		{
			System.out.println("Errore durante l'esecuzione di FindUsersExecutor");
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if (out != null) out.close();
				if (r != null) r.close();
				if (s != null) s.close();
			}
			catch (IOException e)
			{
			}
		}
	}

}
