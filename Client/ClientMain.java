package LIMIC.Client;

import LIMIC.debugger.*;
import LIMIC.core.*;

import java.util.Scanner;

public class ClientMain {

	private static String CLIENT_IP = "localhost";
	private static int CLIENT_PORT = 60000;

	private static boolean isAlive;
	private Scanner scan;
	private MessageSender sender;
	private ClientSocketThread csThread;

	// 個人データとか

	public static void main (String[] args) {
		if (null != args) CLIENT_PORT = Integer.parseInt(args[0]);
		ClientMain main = new ClientMain();
		main.doMain();
	}
	
	public ClientMain() {
		scan = new Scanner(System.in);
		sender = new MessageSender(CLIENT_IP, CLIENT_PORT);
		csThread = new ClientSocketThread(CLIENT_PORT);
	}

	public void doMain() {
		try {
			isAlive = true;
			csThread.start();
			
			while(isAlive)
				inputData();
		} catch (Exception e) {
			isAlive = false;
			DEBUG.err("Client Main die.",e);
		}
	}

	public static boolean isRunning() {
		return isAlive;
	}

	public static synchronized void outputData(CommData data) {
		System.out.println(data.getMsg());
	}

	public void inputData(){
		System.out.print("\n LIMIC > ");
		String str = scan.next();
		if (str.equals("exit")) {
			isAlive = false;
		return;
		} 
		
		String ip = "localhost";
		int port = 60001;
		String msg = str;
		sender.send(ip, port, msg);
	}
}
