package it.unipi.rcl.cariaggil.clientpack;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;


public class ClientUtils
{
	public static ArrayList<String> restoreRequests(String myName)
	{
		FileInputStream reqsfile = null;
		ObjectInputStream reqsobj = null;
		ArrayList<String> reqs = null;
		
		try
		{
			reqsfile = new FileInputStream("reqs_" + myName + ".ser");
			reqsobj = new ObjectInputStream(reqsfile);
			
			reqs = (ArrayList<String>) reqsobj.readObject();
		}
		catch (FileNotFoundException e)
		{ /* Non gestita in quanto generata soltanto la prima volta che si cerca di eseguire restorereqs() */ }
		catch (IOException e)
		{
			System.out.println("Errore nella creazione dell'object Input stream (dentro restorereqs)");
			e.printStackTrace();
		}
		catch (ClassNotFoundException e)
		{
			System.out.println("Errore nella read object (dentro restorereqs)");
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if(reqsobj != null) reqsobj.close();
				if(reqsfile != null) reqsfile.close();
			}
			catch (IOException e) { }
		}
		ClientUtils.saveRequests(new ArrayList<String>(), myName);
		return reqs;	
	}
	
	public static void saveRequests(ArrayList<String> reqs, String myName)
	{
		FileOutputStream reqsfile = null;
		ObjectOutputStream reqsobj = null;
		
		try
		{
			reqsfile = new FileOutputStream("reqs_" + myName + ".ser", false);
			reqsobj = new ObjectOutputStream(reqsfile);
			
			reqsobj.writeObject(reqs);
		}
		catch (FileNotFoundException e)
		{
			System.out.println("File not found (dentro savereqs)");
			e.printStackTrace();
		}
		catch (IOException e)
		{
			System.out.println("Errore nella creazione dell'object output stream (dentro savereqs)");
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if(reqsobj != null) reqsobj.close();
				if(reqsfile != null) reqsfile.close();
			}
			catch (IOException e) { }
		}
	}
	
	public static ArrayList<String> restoreContents(String myName)
	{
		FileInputStream contentsfile = null;
		ObjectInputStream contentsobj = null;
		ArrayList<String> contents = null;
		
		try
		{
			contentsfile = new FileInputStream("contents_" + myName + ".ser");
			contentsobj = new ObjectInputStream(contentsfile);
			
			contents = (ArrayList<String>) contentsobj.readObject();
			
		}
		catch (FileNotFoundException e)
		{ /* Non gestita in quanto generata soltanto la prima volta che si cerca di eseguire restorecontents() */ }
		catch (IOException e)
		{
			System.out.println("Errore nella creazione dell'object Input stream (dentro restorecontents)");
			e.printStackTrace();
		}
		catch (ClassNotFoundException e)
		{
			System.out.println("Errore nella read object (dentro restorecontents)");
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if(contentsobj != null) contentsobj.close();
				if(contentsfile != null) contentsfile.close();
			}
			catch (IOException e) { }
		}
		ClientUtils.saveContents(new ArrayList<String>(), myName);
		return contents;
	}	

	public static void saveContents(ArrayList<String> contents, String myName)
	{
		FileOutputStream contentsfile = null;
		ObjectOutputStream contentsobj = null;
		
		try
		{
			contentsfile = new FileOutputStream("contents_" + myName + ".ser", false);
			contentsobj = new ObjectOutputStream(contentsfile);
			
			contentsobj.writeObject(contents);
		}
		catch (FileNotFoundException e)
		{
			System.out.println("File not found (dentro savecontents)");
			e.printStackTrace();
		}
		catch (IOException e)
		{
			System.out.println("Errore nella creazione dell'object output stream (dentro savecontents)");
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if(contentsobj != null) contentsobj.close();
				if(contentsfile != null) contentsfile.close();
			}
			catch (IOException e) { }
		}
	}
}
