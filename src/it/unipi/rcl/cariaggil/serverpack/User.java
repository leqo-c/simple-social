package it.unipi.rcl.cariaggil.serverpack;

import java.io.Serializable;
import java.net.InetAddress;
import java.rmi.RemoteException;
import java.util.ArrayList;

import it.unipi.rcl.cariaggil.clientpack.IClientService;

public class User implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// Contiene tutte le informazioni necessarie al server per 
	// interagire con l'utente rappresentato dall'istanza di questa
	// classe.
	
	private String username;
	private String password;
	private InetAddress friendshipaddress;
	private InetAddress onlineaddress;
	private ArrayList<User> followers;
	private ArrayList<String> contents;
	private transient IClientService stub;
	private int friendshipport;
	private int onlineport;
	private boolean isOnline;
	
	public User(String name, String pass)
	{
		username = name;
		password = pass;
		stub = null;
		friendshipaddress = null;
		onlineport = -1;
		onlineaddress = null;
		isOnline = false;
		followers = new ArrayList<User>();
		contents = new ArrayList<String>();
	}
	
	public IClientService getStub()
	{
		return stub;
	}
	
	public boolean hasSetOnlineListener()
	{
		return onlineaddress != null && onlineport != -1;
	}
	
	public ArrayList<User> getFollowers()
	{
		return followers;
	}
	
	public void addFollower(User u)
	{
		followers.add(u);
	}	
	
	public void setOnline(boolean value)
	{
		isOnline = value;
	}
	
	public boolean isUserOnline()
	{
		return isOnline;
	}
	
	public void setFriendshipListener(InetAddress ia, int p)
	{
		friendshipport = p;
		friendshipaddress = ia;
	}
	
	public int getFriendshipPort()
	{
		return friendshipport;
	}
	
	public InetAddress getFriendshipAddress()
	{
		return friendshipaddress;
	}
	
	public void addCallback(IClientService s)
	{
		if(s != null)
		{
			try
			{ stub = s;  stub.setTokenValid(true);}
			catch (RemoteException e)
			{ System.out.println("Errore durante la fase di login (RMI)"); }
		}
	}
	
	public String getUsername()
	{
		return username;
	}
	
	public String getPassword()
	{
		return password;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		User other = (User) obj;
		if (password == null)
		{
			if (other.password != null) return false;
		}
		else
			if (!password.equals(other.password)) return false;
		if (username == null)
		{
			if (other.username != null) return false;
		}
		else
			if (!username.equals(other.username)) return false;
		return true;
	}

	public void storeContent(String message)
	{
		contents.add(message);
	}
	
	public ArrayList<String> getContents()
	{
		return contents;
	}
	
	public void cleanContents()
	{
		contents.clear();
	}

	public void removeStub()
	{
		if(stub != null) 
		{
			try
			{
				stub.setTokenValid(false);
			}
			catch (RemoteException e) 
			{ }
		}
		stub = null;
	}

	public void setOnlineListener(InetAddress indirizzoCliente, int portaCliente)
	{
		onlineaddress = indirizzoCliente;
		onlineport = portaCliente;
	}
	
	public int getOnlinePort()
	{
		return onlineport;
	}
	
	public InetAddress getOnlineAddress()
	{
		return onlineaddress;
	}
	
}
