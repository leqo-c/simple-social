package it.unipi.rcl.cariaggil.serverpack;

import java.io.Serializable;
import java.util.Date;

public class PendingFriendship implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// Un'istanza di questa classe rappresenta una richiesta di amicizia
	// inviata da fromUser e destinata a toUser. 
	
	String fromUser;
	String toUser;
	Date requestTime;
	
	public PendingFriendship(String from, String to)
	{
		fromUser = from;
		toUser = to;
		requestTime = new Date();
	}
	
	public Date getRequestTime()
	{
		return requestTime;
	}
	
	public String getFrom()
	{
		return fromUser;
	}

	public String getTo()
	{
		return toUser;
	}
	
	
	@Override
	public boolean equals(Object obj)
	{
		PendingFriendship other = (PendingFriendship) obj;
		if (!fromUser.equals(other.fromUser)) return false;
		if (!toUser.equals(other.toUser)) return false;
		return true;
	}

}
