package it.unipi.rcl.cariaggil.clientpack;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

import it.unipi.rcl.cariaggil.constants.Constants;

public class FriendshipListenerThread implements Runnable
{
	private String username;
	Vector<String> myFriends;

	public FriendshipListenerThread(String user, Vector<String> friends)
	{
		username = user;
		myFriends = friends;
	}

	@Override
	public void run()
	{
		Socket s = null, sock = null;
		ServerSocket ss = null;
		BufferedWriter w = null;
		BufferedReader reader = null;
		InetAddress myAddr;
		int myPort;
		try
		{
			s = new Socket(InetAddress.getLocalHost(), Constants.SERVER_FRIENDSHIP_LISTENING_PORT);
			s.setReuseAddress(true);
			
			w = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));

			// Inizialmente apro una connessione con il server affinché esso
			// possa memorizzare il socket address su cui intendo ricevere
			// le richieste di amicizia.
			
			w.write(username);
			w.newLine();
			w.flush();

			myAddr = s.getLocalAddress();
			myPort = s.getLocalPort();
			
			if(s != null) 
				s.close();
			
			// Attendo connessioni da parte del server sul socket address precedentemente
			// inviato. Ogni volta che ricevo una richiesta di amicizia la
			// aggiungo ad una lista accedibile dal thread principale SocialClient.
			
			ss = new ServerSocket(myPort, 10, myAddr);
			
			while (true)
			{
				sock = ss.accept();
				reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));

				String usr = reader.readLine();
				myFriends.addElement(usr);
			}

		}
		catch (IOException e)
		{
			System.out.println("Errore durante l'esecuzione di FriendshipListenerThread");
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if(w != null) w.close();
				
				if(reader != null) reader.close();
				if(ss != null) ss.close();
			}
			catch (IOException e)
			{
			}
		}
	}

}
