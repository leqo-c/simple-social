package it.unipi.rcl.cariaggil.clientpack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import it.unipi.rcl.cariaggil.constants.Constants;
import it.unipi.rcl.cariaggil.serverpack.IServerService;

public class SocialClient
{
	private static String myName = "<noName>";
	private static Vector<String> friends;
	private static Registry r = null;
	private static IServerService server = null;
	private static ClientService cliServ = null;
	private static Thread friendlistener = null, keepalive = null, online = null;
	
	public SocialClient() { }
	
	public static void main(String[] args)
	{
		int scelta = -1;
		String oAuth = "-1";
		Date timestamp = null;
		friends = new Vector<String>();
		while(true)
		{
			scelta = VisualizzaMenu();
			if(!isTokenValid(scelta, timestamp))
			{
				System.out.println("[SC] Token scaduto, rieffettua il login");
				scelta = 1;
			}
			switch(scelta)
			{
			
			// In base alla scelta dell'utente si attiva uno specifico thread, che 
			// apre una connessione TCP con l'equivalente thread del server.
			
			case 0:
				Registrazione();
				break;
			case 1:
				
				oAuth = Login(); //"-1" (stringa) sse errore
				
				if(oAuth.equals("-1"))
					System.out.println("[SC] Credenziali errate o utente già acceduto.");
				else
				{
					System.out.println("[SC] Login effettuato, oAuth = " + oAuth);
					timestamp = new Date();
					
					// Dopo il login vengono attivati i thread listener e il thread
					// di keepalive. I primi hanno il compito di rispondere al server 
					// quando esso cerca di inoltrare richieste o aggiornare la lista
					// degli utenti attivi.
					
					if(friendlistener != null)
						friendlistener.interrupt();
					friendlistener = new Thread(new FriendshipListenerThread(myName, friends));
					friendlistener.start();
					
					if(keepalive != null)
						keepalive.interrupt();
					keepalive = new Thread(new KeepAliveThread(myName));
					keepalive.start();
					
					
					if(online != null)
						online.interrupt();
					online = new Thread(new OnlineListenerThread(myName));
					online.start();
				}
					
				break;
				
				// Prima di eseguire un'operazione diversa da quella di registrazione
				// o da quella di login si controlla che il token generato dal server
				// sia ancora valido. Il server valida o invalida il token 
				// utilizzando RMI.
				
			case 3:
				if(cliServ != null && cliServ.isTokenValid())
					NuovaAmicizia();
				else 
					System.out.println("Rieffettua il login");
				break;	
			case 4:
				if(cliServ != null && cliServ.isTokenValid())
					GestisciAmicizie();
				else 
					System.out.println("Rieffettua il login");
				break;
			case 5:
				if(cliServ != null && cliServ.isTokenValid())
					ListaAmici();
				else 
					System.out.println("Rieffettua il login");				
				break;
			case 6:
				if(cliServ != null && cliServ.isTokenValid())
					RicercaUtenti();
				else 
					System.out.println("Rieffettua il login");
				break;
			case 7:
				if(cliServ != null && cliServ.isTokenValid())
					PubblicaContenuto();
				else 
					System.out.println("Rieffettua il login");
				
				break;
			case 8:
				if(cliServ != null && cliServ.isTokenValid())
					Follow();
				else 
					System.out.println("Rieffettua il login");
				
				break;
			case 9:
				if(cliServ != null && cliServ.isTokenValid())
					VisualizzaNuoviContenuti();
				else 
					System.out.println("Rieffettua il login");
				
				break;
			case 10:
				if(cliServ != null && cliServ.isTokenValid())
					Logout();
				else 
					System.out.println("Rieffettua il login");
				
				break;
			}
		}
	}

	private static void Logout()
	{
		// Al momento del logout salvo su disco le richieste di amicizia
		// non riscontrate e i contenuti non ancora letti.
		
		ClientUtils.saveRequests(toArrayList(friends), myName);
		ClientUtils.saveContents(cliServ.getContenuti(), myName);
			
		Thread t = new Thread(new LogoutThread(myName));
		t.start();
	}

	private static ArrayList<String> toArrayList(Vector<String> fr)
	{
		ArrayList<String> res = new ArrayList<String>();
		for(String s : fr)
		{
			res.add(s);
		}
		return res;
	}

	private static void PubblicaContenuto()
	{
		BufferedReader r = null;
		try
		{
			r = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("Scrivi qualcosa...");
			String post = r.readLine();
			
			Thread t = new Thread(new PublishContentThread(myName, post));
			t.start();
		}	
		catch(IOException e){System.out.println("Si è verificato un errore di IO");}
	}
	
	private static void VisualizzaNuoviContenuti()
	{
		ArrayList<String> myContents = ClientUtils.restoreContents(myName);
		if(myContents != null)
		{
			for(String cont : myContents)
			{
				System.out.println(cont);
			}
		}
		cliServ.viewContents();
	}

	private static void Follow()
	{
		BufferedReader r = null;
		try
		{
			r = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("Chi vuoi seguire?");
			String toFollow = r.readLine();
			
			boolean result = server.follow(myName, toFollow);
			if(result)
			{
				System.out.println("Adesso segui " + toFollow);
			}
			else
			{
				System.out.println("L'utente è inesistente o non è tuo amico");
			}
		}	
		catch(IOException e){System.out.println("Si è verificato un errore di IO");}
		
		//Non posso fare r.close perché causerebbe la chiusura di System.in
	}

	private static void SetCallback()
	{
		try
		{
			r = LocateRegistry.getRegistry(Constants.SERVER_RMI_PORT);
			
			server = (IServerService) r.lookup(Constants.SERVER_RMI_NAME);	
			
			cliServ = new ClientService();
			
			server.registerCallback(myName, cliServ);
		}
		catch (RemoteException | NotBoundException e)
		{
			System.out.println("Errore in SetCallback (Social Client)");
			e.printStackTrace();
		}
	}

	private static void RicercaUtenti()
	{
		BufferedReader r = null;
		try
		{
			r = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("Immetti un nome o una sua parte");
			String searchParam = r.readLine();
			Thread t = new Thread(new FindUsersThread(searchParam));
			t.start();
		}
		catch(IOException e){System.out.println("Si è verificato un errore di IO");}
		
		//Non posso fare r.close perché causerebbe la chiusura di System.in
		
	}

	private static void ListaAmici()
	{
		Thread t = new Thread(new FriendListThread(myName));
		t.start();
	}

	private static void GestisciAmicizie()
	{
		BufferedReader r = null;
		try
		{
			if(StampaAmici())
			{
				r = new BufferedReader(new InputStreamReader(System.in));
				System.out.println("Scegli l'utente:");
				String richiedente = r.readLine();
				
				System.out.println("Vuoi confermare l'amicizia? [Y/N]");
				String choice = r.readLine();
	
				Thread t = new Thread(new FriendshipConfirmThread(richiedente, myName, choice, friends));
				t.start();
			}
			else
			{
				System.out.println("Non ci sono richieste di amicizia in sospeso");
			}
		}
		catch(IOException e){System.out.println("Si è verificato un errore di IO");}
		
		//Non posso fare r.close perché causerebbe la chiusura di System.in
	}

	private static boolean StampaAmici()
	{
		ArrayList<String> reqs = ClientUtils.restoreRequests(myName);
		if(reqs != null)
			friends.addAll(reqs);
		
		if(friends.isEmpty())
			return false;
		else
		{
			for(String u : friends)
			{
				System.out.println(u);
			}
			return true;
		}
	}

	private static void NuovaAmicizia()
	{
		BufferedReader r = null;
		try
		{
			r = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("Richiedi amicizia a:");
			String toUsr = r.readLine();

			Thread t = new Thread(new FriendshipRequestThread(myName, toUsr));
			t.start();
			t.join();
		}
		catch(IOException e){System.out.println("Si è verificato un errore di IO");}
		catch (InterruptedException e) { System.out.println("[SC] interrotto mentre aspettavo il completamento di friendshipRequestThread"); }
		
		//Non posso fare r.close perché causerebbe la chiusura di System.in
	}

	private static boolean isTokenValid(int scelta, Date timestamp)
	{
		if(scelta == -1 || scelta == 0 || scelta == 1 )
			return true;
		if(timestamp != null)
			return ( (new Date().getTime() - timestamp.getTime()) / 3600000 ) < 24 ;
		else return false;
	}

	private static String Login()
	{
		BufferedReader r = null;
		ExecutorService serv = null;
		String toReturn = "-1";
		try
		{
			r = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("Username");
			String usr = r.readLine();
			System.out.println("Password");
			String pass = r.readLine();
			myName = usr;
			
			SetCallback();
			
			serv = Executors.newFixedThreadPool(1);
			Callable<String> call = new LoginThread(usr, pass);
			Future<String> res = serv.submit(call);
			toReturn = res.get();
		}
		catch(IOException e){System.out.println("Si è verificato un errore di IO");}
		catch (InterruptedException e) { System.out.println("[SC] interrotto mentre aspettavo il completamento di loginThread"); }
		catch (ExecutionException e) { System.out.println("[SC] loginThread ha lanciato un'eccezione"); }
		finally
		{ 
			serv.shutdown(); 
			//Non posso fare r.close perché causerebbe la chiusura di System.in
		}
		return toReturn; 
	}

	private static void Registrazione()
	{
		BufferedReader r = null;
		try
		{
			r = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("Username");
			String usr = r.readLine();
			System.out.println("Password");
			String pass = r.readLine();
			myName = usr;
			Thread t = new Thread(new RegistrationThread(usr, pass));
			t.start();
			t.join();
		}
		catch(IOException e){System.out.println("Si è verificato un errore di IO");}
		catch (InterruptedException e) { System.out.println("[SC] interrotto mentre aspettavo il completamento di registrationThread"); }
		
		//Non posso fare r.close perché causerebbe la chiusura di System.in
	}

	private static int VisualizzaMenu()
	{
		System.out.println("--- SIMPLE SOCIAL ---");
		System.out.println("0. Registrati");
		System.out.println("1. Login");
		System.out.println("3. Nuova amicizia");
		System.out.println("4. Conferma amicizie");
		System.out.println("5. Lista amici");
		System.out.println("6. Cerca utenti");
		System.out.println("7. Pubblica contenuto");
		System.out.println("8. Segui un utente");
		System.out.println("9. Visualizza nuovi contenuti");
		System.out.println("10. Logout");
		
		InputStreamReader is = new InputStreamReader(System.in);
		BufferedReader r = new BufferedReader(is);
		int scelta = -1;
		try
		{
			scelta = Integer.parseInt(r.readLine());
		}
		catch(NumberFormatException e){System.out.println("Scelta non valida");}
		catch (Exception e) { e.printStackTrace(); }
		
		return scelta;
	}

}
