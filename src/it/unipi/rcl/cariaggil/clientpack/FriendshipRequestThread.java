package it.unipi.rcl.cariaggil.clientpack;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;

import it.unipi.rcl.cariaggil.constants.Constants;

public class FriendshipRequestThread implements Runnable
{
	private String myusername;
	private String friendusername;

	public FriendshipRequestThread(String me, String friend)
	{
		myusername = me;
		friendusername = friend;
	}

	@Override
	public void run()
	{
		Socket s = null;
		BufferedWriter w = null;
		BufferedReader reader = null;
		try
		{
			s = new Socket(InetAddress.getLocalHost(), Constants.SERVER_FRIENDSHIP_REQUEST_PORT);
			w = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));

			w.write(myusername);
			w.newLine();
			w.flush();
			
			w.write(friendusername);
			w.newLine();
			w.flush();

			reader = new BufferedReader(new InputStreamReader(s.getInputStream()));

			String outcome = reader.readLine();
			System.out.println(outcome);
		}
		catch (IOException e)
		{
			System.out.println("Errore durante l'esecuzione di FriendshipRequestThread");
		}
		finally
		{
			try
			{
				w.close();
				s.close();
				reader.close();
			}
			catch (IOException e)
			{
			}
		}
	}

}
