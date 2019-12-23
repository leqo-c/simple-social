package it.unipi.rcl.cariaggil.clientpack;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.Callable;

import it.unipi.rcl.cariaggil.constants.Constants;

public class LoginThread implements Callable<String>
{
	private String username;
	private String password;
	
	public LoginThread(String usr, String pass)
	{
		username = usr;
		password = pass;
	}
	
	@Override
	public String call()
	{
		Socket s = null;
		BufferedWriter w = null;
		BufferedReader r = null;
		try
		{
			s = new Socket(InetAddress.getLocalHost(), Constants.SERVER_LOGIN_PORT);
			w = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));

			w.write(username);
			w.newLine();
			w.flush();
			w.write(password);
			w.newLine();
			w.flush();
			
			r = new BufferedReader(new InputStreamReader(s.getInputStream()));
			String result = r.readLine();
			
			return result; //"-1" (stringa) sse login errato
		}
		
		catch(IOException e)
		{
			e.printStackTrace();
			return null;
		}
		finally
		{
			try 
			{ 
				if(r != null) r.close(); 
				if(w != null) w.close(); 
				if(s != null) s.close(); 
			}
			catch (IOException e) { }
		}
	}	

}
