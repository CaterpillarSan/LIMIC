package LIMIC.Client;

import LIMIC.debugger.*;
import LIMIC.core.*;

import java.util.Scanner;

public class ClientMain {

	private static String CLIENT_IP = "localhost";
	private static int CLIENT_PORT;

	public static boolean isAlive;
	private MessageSender sender;
	private ClientSocketThread csThread;
	private ClientDataStorage storage;
	private ClientCUI cui;

	// 個人データとか

	public static void main (String[] args) {
		Scanner scan = new Scanner(System.in);
		System.out.print("Name: ");		String name = scan.next();
		System.out.print("ID: "); 		int id = scan.nextInt();
		System.out.print("Port: ");		CLIENT_PORT = scan.nextInt();

		ClientMain main = new ClientMain(name, id);
		main.doMain();
	}
	
	public ClientMain(String name, int userId) {
		sender = new MessageSender(userId);
		storage = new ClientDataStorage(name,CLIENT_IP, CLIENT_PORT,userId, sender);
		csThread = new ClientSocketThread(CLIENT_PORT,storage);
		cui = new ClientCUI(sender, storage);
	}

	public void doMain() {
		try {
			// initiallize 
			sender.initialComm(storage);

			isAlive = true;
			csThread.start();
			
			while(isAlive)
				isAlive = cui.inputCommand();
		} catch (Exception e) {
			isAlive = false;
			DEBUG.err("Client Main die.",e);
		}
	}


}
