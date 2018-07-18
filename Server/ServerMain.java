package LIMIC.Server;

import LIMIC.debugger.*;

public class ServerMain {

	private static boolean isAlive;
	private static final int SERVER_PORT = 50000;

	// 個人データとか
	

	public static void main(String[] args) {
		try {
			isAlive = true;
			ServerDataStorage storage = new ServerDataStorage();
			(new ServerSocketThread(SERVER_PORT, storage)).start();
		} catch (Exception e) {
			isAlive = false;
			DEBUG.err("Server Main die.",e);
		}
	}

	public static boolean isRunning() {
		return isAlive;
	}
}
