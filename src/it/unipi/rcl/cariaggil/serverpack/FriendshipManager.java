package it.unipi.rcl.cariaggil.serverpack;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FriendshipManager implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	long pendingsTimeout;
	private List<PendingFriendship> pendings;
	private List<EstabilishedFriendship> friendships;
	
	public FriendshipManager()
	{
		pendings = new ArrayList<PendingFriendship>();
		friendships = new ArrayList<EstabilishedFriendship>();
		pendingsTimeout = 86400; //1 giorno in secondi
	}
	
	public synchronized void printPendings()
	{
		for(PendingFriendship f : pendings)
		{
			System.out.print(f.fromUser + " richiede a ");
			System.out.println(f.toUser);
		}
	}
	
	public synchronized boolean existsPending(String from, String to)
	{
		return pendings.contains(new PendingFriendship(from, to));
	}
	
	public synchronized boolean areFriends(User u1, User u2)
	{
		return friendships.contains(new EstabilishedFriendship(u1, u2));
	}
	
	public synchronized void addFriendship(User u1, User u2)
	{
		if(!friendships.contains(new EstabilishedFriendship(u1, u2)))
		{
			friendships.add(new EstabilishedFriendship(u1, u2));
		}
	}
	
	public synchronized void addPendingFriendship(String u1, String u2)
	{
		pendings.add(new PendingFriendship(u1, u2));
	}
	
	public synchronized boolean removePendingFriendship(String u1, String u2)
	{
		return pendings.remove(new PendingFriendship(u1, u2));
	}
	
	// Aggiorna la lista delle amicizie in sospeso rimuovendo quelle 
	// che, dopo pendingsTimeout secondi, non sono ancora state confermate
	// o rifiutate.
	
	public synchronized void updatePendingFriendships()
	{	
		Date now = new Date();
		for(int i = 0; i < pendings.size(); i++)
		{
			Date when = pendings.get(i).getRequestTime();
			long secondsPassed = (now.getTime() - when.getTime()) / 1000;
			
			if(secondsPassed > pendingsTimeout)
			{
				pendings.remove(i);
				i--;
			}
		}
	}

	public void setPendingFriendshipTimeout(long seconds)
	{
		pendingsTimeout = seconds;
	}
}
