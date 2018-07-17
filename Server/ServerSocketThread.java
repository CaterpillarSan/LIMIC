package LIMIC.Server;

import LIMIC.debugger.*;
import LIMIC.core.*;

import java.net.Socket;
import java.net.ServerSocket;
import java.net.SocketTimeoutException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Clientからの通信を受け取り, やりとりをするThreadを立ち上げる
 */
public class ServerSocketThread extends Thread{

	static final int THREADPOOL_SIZE = 10;
	ServerSocket ss;
	ExecutorService exec;

	protected ServerSocketThread(int port) {
		try {
			ss = new ServerSocket(port);
			exec = Executors.newFixedThreadPool(10);
		} catch (Exception e) {
			DEBUG.err("server socket cannot be established.",e);
		}
	}

	@Override
	public void run() {
		while(ServerMain.isRunning()){
			try {
				Socket sc = ss.accept();
				exec.submit(new SocketClient(sc));
			} catch (Exception e) {
				DEBUG.err("server socket cannot create Comm with Client.",e);
			}
		}
	}
}

/**
 * Clientと実際にやり取りをするThread
 */
class SocketClient implements Runnable {
	Socket recvDataSc, sendDataSc;
	ObjectInputStream ois;
	ObjectOutputStream oos;

	SocketClient(Socket s) throws Exception {
		recvDataSc = s;
		recvDataSc.setSoTimeout(5000);
	}

	@Override
	public void run() {
		try {
			ois = new ObjectInputStream(recvDataSc.getInputStream());
			CommData data = (CommData)ois.readObject();

			ois.close();
			recvDataSc.close();

			SendDataToCommPair(data); //TODO ack返す
		} catch (Exception e) {
			DEBUG.err("Server socket client triggered failure.",e);
			try {
				ois.close();
				recvDataSc.close();
			} catch (Exception ee) {
				DEBUG.err("ServerSocket cannot close.",ee);
			}
		}
	}

	/**
	 * A -> Bのトーク送信において, Aから受け取ったdataを元にBにdataを送信
	 */
	protected boolean SendDataToCommPair(CommData data) {
		// TODO 本当に友達か?

		try {	
			sendDataSc = new Socket(data.ipTo, data.portTo);
			oos = new ObjectOutputStream(sendDataSc.getOutputStream());

			oos.writeObject(data);

			oos.close();
			sendDataSc.close();
			return true;
		} catch (Exception e) {
			DEBUG.err("Server side cannot connect to "+data.ipTo +":"+ data.portTo, e);
			return false;
		}
	}
}
