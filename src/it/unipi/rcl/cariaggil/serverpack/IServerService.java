package it.unipi.rcl.cariaggil.serverpack;

import java.rmi.Remote;
import java.rmi.RemoteException;

import it.unipi.rcl.cariaggil.clientpack.IClientService;

public interface IServerService extends Remote
{
	// Metodo invocato dal client al momento del login. Dopo la chiamata,
	// il server ottiene l'oggetto remoto del client. Ogni volta che gli 
	// utenti seguiti dal client in questione pubblicano un contenuto, il
	// server chiama il metodo AggiungiContenuto definito dall'interfaccia
	// IClientService.
	
	public void registerCallback(String username, IClientService stubClient) throws RemoteException;
	
	// Il client invoca questo metodo per registrare il suo interesse
	// verso un altro utente e diventare quindi un suo follower.
	
	public boolean follow(String from, String targetUser) throws RemoteException;
}
