package it.unipi.rcl.cariaggil.serverpack;

import java.io.Serializable;

public class EstabilishedFriendship implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// Un'istanza di questa classe rappresenta un legame di amicizia
	// tra due utenti. 
	
	User fromUser;
	User toUser;
	
	public EstabilishedFriendship(User from, User to)
	{
		fromUser = from;
		toUser = to;
	}
	
	public User getFrom()
	{
		return fromUser;
	}
	
	public User getTo()
	{
		return toUser;
	}
	
	// Malgrado il nome delle variabili di istanza, se l'utente fromUser
	// è amico dell'utente toUser allora toUser è amico di fromUser.
	
	@Override
	public boolean equals(Object obj)
	{
		EstabilishedFriendship ef = (EstabilishedFriendship) obj;
		User u1 = ef.getFrom();
		User u2 = ef.getTo();
		boolean result = (u1.equals(this.getFrom()) && u2.equals(this.getTo()) || 
							u1.equals(this.getTo()) && u2.equals(this.getFrom()));
		
		return result;
	}
}
