package it.unipi.rcl.cariaggil.clientpack;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

import it.unipi.rcl.cariaggil.constants.Constants;

public class FriendListThread implements Runnable
{
	private String username;
	
	public FriendListThread(String usr)
	{
		username = usr;
	}
	
	@Override
	public void run()
	{
		Socket s = null;
		BufferedWriter w = null;
		ObjectInputStream read = null;
		try
		{
			s = new Socket(InetAddress.getLocalHost(), Constants.SERVER_FRIEND_LIST_PORT);
			w = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));

			w.write(username);
			w.newLine();
			w.flush();
			
			read = new ObjectInputStream(s.getInputStream());
			ArrayList<String> result = (ArrayList<String>) read.readObject();
			
			if(result == null || result.isEmpty())
			{
				System.out.println("Non hai nessun amico");
			}
			else
			{
				for(String str : result)
					System.out.println(str);
			}
		}
		
		catch(IOException e)
		{
			System.out.println("Errore durante l'esecuzione di friendListThread");
			e.printStackTrace();
		}
		catch (ClassNotFoundException e)
		{
			System.out.println("Errore durante il cast");
			e.printStackTrace();
		}
		finally
		{
			try 
			{ 
				if(read != null) read.close();
				if(w != null) w.close(); 
				if(s != null) s.close(); 
			}
			catch (IOException e) { }
		}
		
	}

}
