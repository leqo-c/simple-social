package it.unipi.rcl.cariaggil.clientpack;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import it.unipi.rcl.cariaggil.constants.Constants;

public class OnlineListenerThread implements Runnable
{
	String username;
	
	public OnlineListenerThread(String name)
	{
		username = name;
	}

	@Override
	public void run()
	{
		Socket s = null, sock = null;
		ServerSocket ss = null;
		BufferedWriter w = null;
		InetAddress myAddr;
		int myPort;
		try
		{
			s = new Socket(InetAddress.getLocalHost(), Constants.SERVER_ONLINE_CHECKER_SETTER_PORT);
			s.setReuseAddress(true);
			
			w = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));

			// Inizialmente apro una connessione con il server affinché esso
			// possa memorizzare il socket address su cui intendo rispondere
			// ai messaggi "sonda" utili ad aggiornare la lista degli utenti
			// online.

			w.write(username);
			w.newLine();
			w.flush();

			myAddr = s.getLocalAddress();
			myPort = s.getLocalPort();
			
			if(s != null) 
				s.close();
			
			ss = new ServerSocket(myPort, 10, myAddr);
			
			while (true)
			{
				// Serve solo a far capire al server che sono online. La connessione
				// viene chiusa immediatamente.
				
				sock = ss.accept();
				sock.close();
			}

		}
		catch (IOException e)
		{
			System.out.println("Errore durante l'esecuzione di OnlineListenerThread");
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if(w != null) w.close();
				if(ss != null) ss.close();
			}
			catch (IOException e)
			{
			}
		}
	}

}
