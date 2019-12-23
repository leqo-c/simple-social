package it.unipi.rcl.cariaggil.serverpack;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;

public class FriendshipRequestExecutor implements Runnable
{
	FriendshipManager manager;
	UsersManager usersmanager;
	Socket s;

	public FriendshipRequestExecutor(FriendshipManager man, Socket sock, UsersManager uman)
	{
		manager = man;
		s = sock;
		usersmanager = uman;
	}

	@Override
	public void run()
	{
		BufferedReader reader = null;
		BufferedWriter w = null, wfrom = null;
		Socket sock = null;
		boolean errore = false;
		try
		{
			reader = new BufferedReader(new InputStreamReader(s.getInputStream()));

			String fromUsr = reader.readLine();
			String toUsr = reader.readLine();
			User u = usersmanager.getMatchingUser(toUsr);
			
			if(u != null)
			{
				// Ottengo il socket address su cui intendo spedire
				// la richiesta di amicizia.
				
				InetAddress addr = u.getFriendshipAddress();
				int port = u.getFriendshipPort();
				
				try { sock = new Socket(addr, port); }
				catch(IOException | NullPointerException e) 
				{ 
					// Connessione TCP non stabilita.
					
					errore = true;
				}
				
				if(!errore)
				{
					// Memorizzo l'amicizia in sospeso.
					
					manager.addPendingFriendship(fromUsr, toUsr);
						
					// Inoltro la richiesta all'interessato.
					
					w = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
					w.write(fromUsr);
					w.newLine();
					w.flush();
					
					// Avverto il richiedente.
					
					wfrom = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
					wfrom.write("Richiesta inoltrata con successo");
					wfrom.newLine();
					wfrom.flush();
				}
				else
				{
					// Informo il richiedente dell'errore.
					
					wfrom = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
					wfrom.write("Non sono riuscito ad inoltrare la richiesta di amicizia" );
					wfrom.newLine();
					wfrom.flush();
				}
				
			}
			else // (u non esiste nella lista di utenti)
			{
				// Informo il richiedente dell'errore.
				wfrom = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
				wfrom.write("Username inesistente");
				wfrom.newLine();
				wfrom.flush();
			}

		}
		catch (IOException e)
		{
			System.out.println("Errore durante l'esecuzione di FriendshipRequestExecutor");
		}
		finally
		{
			try
			{
				if(w != null) w.close();
				if(wfrom != null) wfrom.close();
				if(reader != null) reader.close();
				if(s != null) s.close();
				
				if(sock != null) sock.close();
			}
			catch (IOException e)
			{
				System.out.println("Errore durante le operazioni di pulizia");
			}
		}
	}

}
