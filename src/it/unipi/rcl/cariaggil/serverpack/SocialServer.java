package it.unipi.rcl.cariaggil.serverpack;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import it.unipi.rcl.cariaggil.constants.Constants;

public class SocialServer
{
	
	public static void main(String[] args)
	{	
		// Ricostruzione dello stato della rete.
		
		UsersManager users = Utils.restoreUsers();
		if(users == null)
			users = new UsersManager();
		
		FriendshipManager man = Utils.restoreFriends();
		if(man == null)
			man = new FriendshipManager();
		
		if(args.length == 1)
		{
			try
			{
				long seconds = Long.parseLong(args[0]);
				man.setPendingFriendshipTimeout(seconds);
			}
			catch (NumberFormatException e) {  }
		}
		
		Utils.setOfflineAll(users);
		
		// Attivazione di tutti i thread che implementano le funzionalità
		// esposte all'utente.
		
		Thread registration = new Thread(new ServerRegistrationThread(users));
		registration.start();
		
		Thread login = new Thread(new ServerLoginThread(users));
		login.start();
		
		Thread logout = new Thread(new ServerLogoutThread(users));
		logout.start();
		
		Thread friendshiplistener = new Thread(new ServerFriendshipListenerThread(users));
		friendshiplistener.start();
		
		Thread friendshiprequest = new Thread(new ServerFriendshipRequestThread(man, users));
		friendshiprequest.start();
		
		Thread friendshipconfirmer = new Thread(new ServerFriendshipConfirmThread(users, man));
		friendshipconfirmer.start();
		
		Thread keepalive = new Thread(new ServerKeepAliveThread(users));
		keepalive.start();
		
		Thread friendlist = new Thread(new ServerFriendListThread(users, man));
		friendlist.start();
		
		Thread findusers = new Thread(new ServerFindUsersThread(users));
		findusers.start();
		
		Thread publisher = new Thread(new ServerPublishContentThread(users));
		publisher.start();
		
		Thread onlinelistener = new Thread(new ServerOnlineListenerThread(users));
		onlinelistener.start();
		
		// Inizializzazione del servizio remoto utilizzato dagli utenti per 
		// registrare le callback e gli interessi per altri utenti.
		
		try
		{
			ServerService serv = new ServerService(users, man);
			
			IServerService stub = (IServerService) UnicastRemoteObject.exportObject(serv, Constants.SERVER_EXPORT_PORT);
			
			LocateRegistry.createRegistry(Constants.SERVER_RMI_PORT);
			
			Registry r = LocateRegistry.getRegistry(Constants.SERVER_RMI_PORT);
			
			r.rebind(Constants.SERVER_RMI_NAME, stub);
		}
		catch (RemoteException e)
		{
			System.out.println("Errore durante la fase di impostazione del servizio remoto (Social Server)");
			e.printStackTrace();
		}
		
		
		while(true)
		{
			
			try
			{
				// Salvataggio dello stato della rete.
				
				Thread.sleep(5000);
				Utils.saveUsers(users);
				Utils.saveFriends(man);
				
				// Controlla anche le richieste di amicizia in sospeso
				// ed elimina le più vecchie.
				
				man.updatePendingFriendships();
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}

}
