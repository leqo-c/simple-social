package it.unipi.rcl.cariaggil.serverpack;

import java.io.Serializable;
import java.net.InetAddress;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import it.unipi.rcl.cariaggil.clientpack.IClientService;


public class UsersManager implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// Un oggetto di tipo UsersManager contiene la lista di tutti
	// gli utenti iscritti a Simple Social e fornisce diversi metodi
	// per interagire con essa. Gli utenti online si distinguono da 
	// quelli offline per il valore della variabile di istanza
	// online (della classe User).
	
	private List<User> allusers;
	
	public UsersManager()
	{
		allusers = new ArrayList<User>();
	}
	
	public synchronized boolean isRegisteredUser(String username)
	{
		for(User u : allusers)
		{
			if(u.getUsername().equals(username))
				return true;
		}
		return false;
	}
	
	public synchronized boolean isLoggedUser(String username)
	{
		for(User u : allusers)
		{
			if(u.getUsername().equals(username) && u.isUserOnline())
				return true;
		}
		return false;
	}
	
	public synchronized User getMatchingUser(String username, String password)
	{
		for(User u : allusers)
		{
			if(u.getUsername().equals(username) && u.getPassword().equals(password))
				return u;
		}
		return null;
	}
	
	public synchronized User getMatchingUser(String username)
	{
		for(User u : allusers)
		{
			if(u.getUsername().equals(username))
				return u;
		}
		return null;
	}
	
	public synchronized boolean addUser(User u)
	{
		if(u.getUsername() != null && u.getPassword() != null && (!containsName(allusers, u.getUsername())))
		{	
			allusers.add(u);
			return true;
		}
		else 
			return false;
	}
	
	private synchronized boolean containsName(List<User> users, String username)
	{
		if(users == null)
			return false;
		for(User u : users)
		{
			if(u.getUsername().equals(username))
				return true;
		}
		return false;
	}

	public synchronized boolean logUser(String username, String password)
	{
		if(username != null && password != null && isRegisteredUser(username) && !isLoggedUser(username))
		{	
			User u = getMatchingUser(username, password);	
			if(u != null)
			{
				u.setOnline(true); 
				
				// Se l'utente ha ricevuto dei contenuti mentre era offline allora
				// invio tali contenuti al momento del login.
				
				IClientService stub = u.getStub();
				
				if(stub != null)
				{
					ArrayList<String> conts = u.getContents();
					for(String c : conts)
					{
						try
						{
							stub.AggiungiContenuto(c);
						}	
						catch (RemoteException e)
						{
							System.out.println("RemoteException in stub.AggiungiContenuto");
							e.printStackTrace();
						}
					}
					u.cleanContents();
				}
				return true;	
			}
		}
		return false;
	}

	public synchronized void setFriendshipListener(String usr, InetAddress indirizzoCliente, int portaCliente)
	{
		User u = getMatchingUser(usr);
		if(u != null)
			u.setFriendshipListener(indirizzoCliente, portaCliente);
		
	}

	public synchronized void UpdateOnlineUsers(List<String> users, List<String> oldOnlineUsers)
	{			
		// Rimuove dalla lista degli utenti online quegli utenti
		// il cui nome non compare nella lista "users".
		
		for(int i = 0; i < allusers.size(); i++)
		{
			User u = allusers.get(i);
			
			if(oldOnlineUsers.contains(u.getUsername()))
			{
				boolean found = false;
				for(int j = 0; j < users.size(); j++)
				{				
					if(u.getUsername().equals(users.get(j)))
					{
						found = true;
						break;
					}
				}
				if(!found)
				{			
					// Al momento del logout del client la callback viene rimossa dal server.
					System.out.println(u.getUsername() + " non è più online.");
					u.setOnline(false);	
					u.removeStub();
				}
			}
		}
		
		System.out.println("Utenti rimasti online: " + countOnlineUsers(allusers));
	}

	private synchronized int countOnlineUsers(List<User> users)
	{
		int res = 0;
		for(User u : users)
		{
			if(u.isUserOnline()) res++;
		}
		return res;
	}

	public synchronized ArrayList<String> getFriendsInfo(String user, FriendshipManager fman)
	{
		ArrayList<String> friends = new ArrayList<String>();
		for(int i = 0; i < allusers.size(); i++)
		{
			User u = allusers.get(i);
			if(fman.areFriends(u, getMatchingUser(user)))
			{	
				if(u.isUserOnline())
					friends.add(u.getUsername() + " (ONLINE)");
				else
					friends.add(u.getUsername() + " (OFFLINE)");
			}
		}
		return friends;
	}

	public synchronized ArrayList<String> findUsers(String nameToFind)
	{
		ArrayList<String> res = new ArrayList<String>();
		for(int i = 0; i < allusers.size(); i++)
		{
			User u = allusers.get(i);
			if(u.getUsername().contains(nameToFind))
			{
				res.add(u.getUsername());
			}
		}
		return res;
	}

	public synchronized void setOnlineListener(String usr, InetAddress indirizzoCliente, int portaCliente)
	{
		User u = getMatchingUser(usr);
		if(u != null)
		{
			u.setOnlineListener(indirizzoCliente, portaCliente);
		}			
	}

	public synchronized List<String> getOnlineUsers()
	{
		ArrayList<String> res = new ArrayList<String>();
		for(User u : allusers)
		{
			if(u.isUserOnline())
				res.add(u.getUsername());
		}
		return res;
	}

	public synchronized void logoutUser(String usr)
	{
		User u = getMatchingUser(usr);
		
		u.setOnline(false);
		u.removeStub();		
	}

	public List<User> getAllUsers()
	{
		return allusers;
	}
}
