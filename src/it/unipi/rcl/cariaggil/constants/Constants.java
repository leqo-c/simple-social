package it.unipi.rcl.cariaggil.constants;

import java.util.UUID;

public class Constants
{
	// Valori costanti che indicano numeri di porta, indirizzo IP
	// del gruppo di multicast su cui il server invia messaggi di
	// keep-alive.
	
	public static int SERVER_REGISTRATION_PORT = 22300;
	
	public static int SERVER_LOGIN_PORT = 4444;
	
	public static int SERVER_FRIENDSHIP_LISTENING_PORT = 22400;
	
	public static int SERVER_FRIENDSHIP_REQUEST_PORT = 22500;
	
	public static int SERVER_FRIENDSHIP_CONFIRM_PORT = 22600;
	
	public static int SERVER_KEEPALIVE_PORT = 22700;
	
	public static int SERVER_KEEPALIVE_RECEIVER_PORT = 22800;

	public static int SERVER_FRIEND_LIST_PORT = 22900;

	public static int SERVER_FIND_USERS_PORT = 23000;
	
	public static int SERVER_PUBLISH_PORT = 23100;
	
	public static int MULTICAST_KEEPALIVE_PORT = 1100;
	
	public static String MULTICAST_KEEPALIVE = "224.224.224.224";

	public static int SERVER_EXPORT_PORT = 39000;

	public static int SERVER_RMI_PORT = 5000;

	public static String SERVER_RMI_NAME = "SERVER_SIMPLESOCIAL";

	public static int CLIENT_EXPORT_PORT = 40000;

	public static int SERVER_ONLINE_CHECKER_SETTER_PORT = 23200;

	public static int SERVER_LOGOUT_PORT = 23300;

	// Metodo che genera identificatori univoci assegnati durante la
	// fase di login.
	
	public static String getNextUid()
	{
		return UUID.randomUUID().toString();
	}
}
