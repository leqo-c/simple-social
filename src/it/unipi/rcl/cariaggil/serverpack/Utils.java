package it.unipi.rcl.cariaggil.serverpack;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Utils
{
	private static Socket s;

	public static void setOfflineAll(UsersManager man)
	{
		List<User> usrs = man.getAllUsers();
		for(User u : usrs)
		{
			u.setOnline(false);
			u.removeStub();
		}
	}
	
	public static void updateOnlineUsers(UsersManager users)
	{
		ArrayList<User> all = (ArrayList<User>) users.getAllUsers();
		for (User u : all)
		{
			if (u.hasSetOnlineListener())
			{

				try
				{
					s = new Socket(u.getOnlineAddress(), u.getOnlinePort());
				}
				catch (IOException | NullPointerException e)
				{
					u.setOnline(false);
					u.removeStub();
				}
				finally
				{
					try { if(s != null) s.close(); }
					catch (IOException e) { }
				}
			}
		}
	}

	// Metodi di utilità che permettono di ripristinare e di 
	// salvare su disco lo stato dell'intera rete. Al suo avvio, il
	// server ricarica (se possibile) tutte le informazioni
	// riguardanti gli utenti e la rete di amicizie e, mentre
	// è attivo, salva periodicamente lo stato della rete.
	
	public static void saveUsers(UsersManager users)
	{
		FileOutputStream usersfile = null;
		ObjectOutputStream usersobj = null;
		
		try
		{
			usersfile = new FileOutputStream("users.ser", false);
			usersobj = new ObjectOutputStream(usersfile);
			
			usersobj.writeObject(users);
		}
		catch (FileNotFoundException e)
		{
			System.out.println("File not found (dentro saveUsers)");
			e.printStackTrace();
		}
		catch (IOException e)
		{
			System.out.println("Errore nella creazione dell'object output stream (dentro saveUsers)");
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if(usersobj != null) usersobj.close();
				if(usersfile != null) usersfile.close();
			}
			catch (IOException e) { }
		}
	}
	
	public static UsersManager restoreUsers()
	{
		FileInputStream usersfile = null;
		ObjectInputStream usersobj = null;
		UsersManager users = null;
		
		try
		{
			usersfile = new FileInputStream("users.ser");
			usersobj = new ObjectInputStream(usersfile);
			
			users = (UsersManager) usersobj.readObject();
		}
		catch (FileNotFoundException e)
		{ /* Non gestita in quanto generata soltanto la prima volta che si cerca di eseguire restoreUsers() */ }
		catch (IOException e)
		{
			System.out.println("Errore nella creazione dell'object Input stream (dentro restoreUsers)");
			e.printStackTrace();
		}
		catch (ClassNotFoundException e)
		{
			System.out.println("Errore nella read object (dentro restoreUsers)");
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if(usersobj != null) usersobj.close();
				if(usersfile != null) usersfile.close();
			}
			catch (IOException e) { }
		}
		return users;
	}
	
	public static void saveFriends(FriendshipManager friends)
	{
		FileOutputStream friendsfile = null;
		ObjectOutputStream friendsobj = null;
		
		try
		{
			friendsfile = new FileOutputStream("friends.ser", false);
			friendsobj = new ObjectOutputStream(friendsfile);
			
			friendsobj.writeObject(friends);
		}
		catch (FileNotFoundException e)
		{
			System.out.println("File not found (dentro savefriends)");
			e.printStackTrace();
		}
		catch (IOException e)
		{
			System.out.println("Errore nella creazione dell'object output stream (dentro savefriends)");
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if(friendsobj != null) friendsobj.close();
				if(friendsfile != null) friendsfile.close();
			}
			catch (IOException e) { }
		}
	}
	
	public static FriendshipManager restoreFriends()
	{
		FileInputStream friendsfile = null;
		ObjectInputStream friendsobj = null;
		FriendshipManager friends = null;
		
		try
		{
			friendsfile = new FileInputStream("friends.ser");
			friendsobj = new ObjectInputStream(friendsfile);
			
			friends = (FriendshipManager) friendsobj.readObject();
		}
		catch (FileNotFoundException e)
		{ /* Non gestita in quanto generata soltanto la prima volta che si cerca di eseguire restorefriends() */ }
		catch (IOException e)
		{
			System.out.println("Errore nella creazione dell'object Input stream (dentro restorefriends)");
			e.printStackTrace();
		}
		catch (ClassNotFoundException e)
		{
			System.out.println("Errore nella read object (dentro restorefriends)");
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if(friendsobj != null) friendsobj.close();
				if(friendsfile != null) friendsfile.close();
			}
			catch (IOException e) { }
		}
		return friends;
	}

}
