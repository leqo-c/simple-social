package it.unipi.rcl.cariaggil.clientpack;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IClientService extends Remote
{
	// Metodo chiamato dal server nell'istante in cui un utente di cui
	// sono follower pubblica un contenuto.
	
	public void AggiungiContenuto(String contenuto) throws RemoteException;
	
	// Il server può validare o invalidare il token generato in fase di
	// autenticazione e forzare l'utente a rieffettuare il login.
	
	public void setTokenValid(boolean value) throws RemoteException;
}
