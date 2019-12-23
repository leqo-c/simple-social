package it.unipi.rcl.cariaggil.clientpack;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Vector;

import it.unipi.rcl.cariaggil.constants.Constants;

public class FriendshipConfirmThread implements Runnable
{
	private String from;
	private String to;
	private String choice;
	private Vector<String> friends;
	
	public FriendshipConfirmThread(String fr, String t, String ch, Vector<String> amici)
	{
		from = fr;
		to = t;
		choice = ch;
		friends = amici;
	}

	@Override
	public void run()
	{
		Socket s = null;
		BufferedWriter w = null;
		BufferedReader reader = null;
		
		try
		{
			s = new Socket(InetAddress.getLocalHost(), Constants.SERVER_FRIENDSHIP_CONFIRM_PORT);
			
			w = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));

			w.write(from);
			w.newLine();
			w.flush();
			
			w.write(to);
			w.newLine();
			w.flush();
			
			w.write(choice);
			w.newLine();
			w.flush();

			//Leggo la risposta del server
			reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
			String res = reader.readLine();
			if(res.equals("Amicizia confermata") || res.equals("Amicizia rifiutata"))
			{
				friends.remove(from);
			}
			System.out.println(res);
			
		}
		catch (IOException e)
		{
			System.out.println("Errore durante l'esecuzione di FriendshipConfirmThread");
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if(w != null) w.close();				
				if(reader != null) reader.close();
				if(s != null) s.close();
			}
			catch (IOException e)
			{
			}
		}
	}

}
