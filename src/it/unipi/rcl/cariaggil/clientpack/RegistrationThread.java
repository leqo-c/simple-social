package it.unipi.rcl.cariaggil.clientpack;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;

import it.unipi.rcl.cariaggil.constants.Constants;

public class RegistrationThread implements Runnable
{
	String usr;
	String pass;
	
	public RegistrationThread(String usrname, String password)
	{
		usr = usrname;
		pass = password;
	}
	
	@Override
	public void run()
	{
		Socket s = null;
		BufferedWriter w = null;
		
		try
		{
			s = new Socket(InetAddress.getLocalHost(), Constants.SERVER_REGISTRATION_PORT);
			w = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
			w.write(usr);
			w.newLine();
			
			w.write(pass);
			w.newLine();
			
			w.flush();
			
			BufferedReader r = new BufferedReader(new InputStreamReader(s.getInputStream()));

			String result = r.readLine();
			System.out.println(result);
			System.out.flush();
		}
		
		catch(IOException e)
		{
			System.out.println("Errore durante la fase di registrazione.");
			e.printStackTrace();
		}
		
		finally
		{
			try
			{
				if(s != null) s.close();
				if(w != null) w.close();
			}
			catch (IOException e) {}
		}
		
		
	}

}
