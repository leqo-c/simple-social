package it.unipi.rcl.cariaggil.clientpack;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;

import it.unipi.rcl.cariaggil.constants.Constants;

public class PublishContentThread implements Runnable
{
	String from;
	String message;

	public PublishContentThread(String myName, String post)
	{
		from = myName;
		message = post;
	}

	@Override
	public void run()
	{
		Socket s = null;
		BufferedWriter w = null;
		
		try
		{
			s = new Socket(InetAddress.getLocalHost(), Constants.SERVER_PUBLISH_PORT);
			
			w = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));

			w.write(from);
			w.newLine();
			w.flush();
			
			w.write(message);
			w.newLine();
			w.flush();
			
		}
		catch (IOException e)
		{
			System.out.println("Errore durante l'esecuzione di PublishContentThread");
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if(w != null) w.close();				
				if(s != null) s.close();
			}
			catch (IOException e)
			{
			}
		}
	}

}
