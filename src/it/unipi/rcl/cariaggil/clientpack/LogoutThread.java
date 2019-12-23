package it.unipi.rcl.cariaggil.clientpack;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;

import it.unipi.rcl.cariaggil.constants.Constants;

public class LogoutThread implements Runnable
{
	String myName;

	public LogoutThread(String name)
	{
		myName = name;
	}

	@Override
	public void run()
	{
		Socket s = null;
		BufferedWriter w = null;
		try
		{
			s = new Socket(InetAddress.getLocalHost(), Constants.SERVER_LOGOUT_PORT);
			w = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));

			w.write(myName);
			w.newLine();
			w.flush();
		}
		
		catch(IOException e)
		{
			System.out.println("Errore durante l'esecuzione di LogoutThread");
			e.printStackTrace();
		}
		finally
		{
			try 
			{ 
				if(w != null) w.close(); 
				if(s != null) s.close(); 
			}
			catch (IOException e) { }
		}
	}

}
