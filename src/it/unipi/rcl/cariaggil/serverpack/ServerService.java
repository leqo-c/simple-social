package it.unipi.rcl.cariaggil.serverpack;

import java.rmi.RemoteException;
import java.rmi.server.RemoteServer;

import it.unipi.rcl.cariaggil.clientpack.IClientService;

public class ServerService extends RemoteServer implements IServerService
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	UsersManager users;
	FriendshipManager friends;
	
	public ServerService(UsersManager uman, FriendshipManager fman)
	{
		users = uman;
		friends = fman;
	}

	@Override
	public void registerCallback(String username, IClientService stubClient) throws RemoteException
	{	
		User u = users.getMatchingUser(username);
		if(u != null)
		{
			u.addCallback(stubClient);	
		}
	}

	@Override
	public boolean follow(String from, String targetUser) throws RemoteException
	{
		User target = users.getMatchingUser(targetUser);
		User me = users.getMatchingUser(from);
		
		if(!friends.areFriends(target, me)) return false;
		
		if(target != null && me != null)
		{
			target.addFollower(me);
			return true;
		}
		
		return false;
	}

}
