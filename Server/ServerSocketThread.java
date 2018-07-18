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
	ServerDataStorage storage;
	ExecutorService exec;

	protected ServerSocketThread(int port , ServerDataStorage storage) {
		try {
			ss = new ServerSocket(port);
			ss.setSoTimeout(5000);
			this.storage = storage;
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
				exec.submit(new SocketClient(sc,storage));
			} catch (SocketTimeoutException e) {
				continue;
			} catch (Exception e) {
				DEBUG.err("server socket cannot create Comm with Client.",e);
				break;
			}
		}
	}
}

/**
 * Clientと実際にやり取りをするThread
 */
class SocketClient implements Runnable {
	ServerDataStorage storage;
	Socket recvDataSc, sendDataSc;
	ObjectInputStream ois;
	ObjectOutputStream oos;

	SocketClient(Socket s, ServerDataStorage storage) throws Exception {
		recvDataSc = s;
		this.storage = storage;
		recvDataSc.setSoTimeout(5000);
	}

	@Override
	public void run() {
		try {
			ois = new ObjectInputStream(recvDataSc.getInputStream());
			CommData data = (CommData)ois.readObject();

			ois.close();
			recvDataSc.close();

			dataTransmit(data); //TODO ack返す
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

	protected void dataTransmit(CommData data) {
		switch (data.tag) {
			case CONST.SEND_MSG:
				sendDataToCommPair(data);
				break;
			case CONST.SEND_NAME:
				storage.changeName(data.myId,data.getMsg());	
				break;
			case CONST.SEND_COMMENT:
				storage.changeComment(data.myId,data.getMsg());	
				break;
			case CONST.INITIAL:
				addUser(data);
				break;	
		}
	}


	/**
	 * A -> Bのトーク送信において, Aから受け取ったdataを元にBにdataを送信
	 */
	protected boolean sendDataToCommPair(CommData data) {
		// TODO 本当に友達か?

		try {	
			sendDataSc = new Socket(storage.getHost(data.friendId),storage.getPort(data.friendId) );
			oos = new ObjectOutputStream(sendDataSc.getOutputStream());

			oos.writeObject(data);

			oos.close();
			sendDataSc.close();
			return true;
		} catch (Exception e) {
			DEBUG.err("Server side cannot connect to "+data.friendId, e);
			return false;
		}
	}

	protected void addUser(CommData data) {
		int id = data.myId;
		String[] address = data.getMsg().split(":");
		String host = address[0];
		int port = Integer.parseInt(address[1]);
		storage.addUser(id,host,port,address[2]);
		DEBUG.log("Add user: "+id+": "+port);
	}
}
