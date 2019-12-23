package it.unipi.rcl.cariaggil.serverpack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;

public class FriendshipListenerExecutor implements Runnable
{

	UsersManager manager;
	Socket s;

	public FriendshipListenerExecutor(UsersManager man, Socket sock)
	{
		manager = man;
		s = sock;
	}

	@Override
	public void run()
	{
		// Questo thread sta in ascolto di richieste provenienti da thread (dei
		// clienti)
		// che desiderano rendersi reperibili per la notifica delle amicizie.
		// Il suo unico compito è quello di memorizzare il socket address su cui
		// il cliente intende ricevere notifiche di amicizia.

		BufferedReader reader = null;
		try
		{
			reader = new BufferedReader(new InputStreamReader(s.getInputStream()));

			String usr = reader.readLine();

			int portaCliente = s.getPort();
			InetAddress indirizzoCliente = s.getInetAddress();

			manager.setFriendshipListener(usr, indirizzoCliente, portaCliente);

		}
		catch (IOException e)
		{
			System.out.println("Errore durante l'esecuzione di serverLoginThread");
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
