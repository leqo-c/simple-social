package it.unipi.rcl.cariaggil.clientpack;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class ClientService extends UnicastRemoteObject implements IClientService
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ArrayList<String> contents;
	boolean tokenValid;
	
	public ClientService() throws RemoteException
	{
		super();
		contents = new ArrayList<String>();
		tokenValid = true;
	}

	@Override
	public void AggiungiContenuto(String contenuto) throws RemoteException
	{
		contents.add(contenuto);
	}
	
	public ArrayList<String> getContenuti()
	{
		return contents;
	}
	
	public void viewContents()
	{
		if(contents.isEmpty())
		{
			System.out.println("Nessun nuovo contenuto.");
			return;
		}
		
		for(String s : contents)
			System.out.println(s);
		contents.clear();
	}

	public void cleanContents()
	{
		contents.clear();
	}

	@Override
	public void setTokenValid(boolean value) throws RemoteException
	{
		tokenValid = value;
	}
	
	public boolean isTokenValid()
	{
		return tokenValid;
	}

}
