package it.unipi.rcl.cariaggil.serverpack;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class FriendshipConfirmExecutor implements Runnable
{
	UsersManager usersManager;
	FriendshipManager friendsManager;
	Socket s;

	public FriendshipConfirmExecutor(UsersManager uman, FriendshipManager fman, Socket sock)
	{
		usersManager = uman;
		friendsManager = fman;
		s = sock;
	}

	@Override
	public void run()
	{
		// from, to, choice
		BufferedReader r = null;
		BufferedWriter w = null;
		try
		{
			r = new BufferedReader(new InputStreamReader(s.getInputStream()));
			w = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
			String from = r.readLine();
			String to = r.readLine();
			String choice = r.readLine();
			
			if(friendsManager.existsPending(from, to))
			{	
				//modifica la rete di amicizie e manda una conferma
				switch(choice)
				{
				case "Y":
					friendsManager.removePendingFriendship(from, to);
					User u1 = usersManager.getMatchingUser(from);
					User u2 = usersManager.getMatchingUser(to);
					friendsManager.addFriendship(u1, u2);
					
					w.write("Amicizia confermata");
					break;
				case "N":
					friendsManager.removePendingFriendship(from, to);
					w.write("Amicizia rifiutata");
					break;
				default:
					w.write("Scelta non riconosciuta, nessuna operazione effettuata");
					break;
				}
				w.newLine();
				w.flush();
			}
			else //Rispondo con un messaggio di errore
			{
				w.write("La richiesta non è presente e non può quindi essere manipolata");
				w.newLine();
				w.flush();
			}
		}
		catch (IOException e)
		{
			System.out.println("Errore durante l'esecuzione di FriendshipConfirmExecutor");
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if(r != null) r.close();
				if(w != null) w.close();
				if(s != null) s.close();
			}
			catch (IOException e)
			{ }
		}
		
	}

}
