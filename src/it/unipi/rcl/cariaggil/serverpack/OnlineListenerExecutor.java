package it.unipi.rcl.cariaggil.serverpack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;

public class OnlineListenerExecutor implements Runnable
{
	UsersManager manager;
	Socket s;

	public OnlineListenerExecutor(UsersManager uman, Socket sock)
	{
		manager = uman;
		s = sock;
	}

	@Override
	public void run()
	{
		// Questo thread sta in ascolto di richieste provenienti da thread (dei
		// clienti)
		// che desiderano rendersi reperibili per dimostrare che sono online.
		// Il suo unico compito è quello di memorizzare il socket address su cui
		// il cliente intende farsi contattare.

		BufferedReader reader = null;
		try
		{
			reader = new BufferedReader(new InputStreamReader(s.getInputStream()));

			String usr = reader.readLine();

			int portaCliente = s.getPort();
			InetAddress indirizzoCliente = s.getInetAddress();

			manager.setOnlineListener(usr, indirizzoCliente, portaCliente);
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
