package LIMIC.Server;

import LIMIC.debugger.*;

public class ServerMain {

	private static boolean isAlive;
	private static final int SERVER_PORT = 50000;

	// 個人データとか
	

	public static void main(String[] args) {
		try {
			isAlive = true;
			// TODO portを複数に割り振り
			(new ServerSocketThread(SERVER_PORT)).start();
		} catch (Exception e) {
			isAlive = false;
			DEBUG.err("Server Main die.",e);
		}
	}

	public static boolean isRunning() {
		return isAlive;
	}
}
