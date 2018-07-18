package LIMIC.Client;

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
 * Serverからのメッセージを待つ
 */
public class ClientSocketThread extends Thread{
	
	static final int THREADPOOL_SIZE = 10;
	ServerSocket ss;
	ClientDataStorage storage;
	ExecutorService exec;

	protected ClientSocketThread(int port, ClientDataStorage storage) {
		try {
			ss = new ServerSocket(port);
			ss.setSoTimeout(5000);
			this.storage = storage;
			exec = Executors.newFixedThreadPool(10);
		} catch (Exception e) {
			DEBUG.err("client socket cannot be established.",e);
		}
	}

	@Override
	public void run() {
		while(ClientMain.isAlive){
			try {
				Socket sc = ss.accept();
				exec.submit(new SocketClient(sc,storage));
			} catch (SocketTimeoutException e) {
				continue;
			} catch (Exception e) {
				DEBUG.err("client socket cannot create Comm with Client.",e);
				break;
			}
		}
	}
}

class SocketClient implements Runnable {
	Socket recvDataSc;
	ObjectInputStream ois;
	ClientDataStorage storage;

	SocketClient(Socket sc, ClientDataStorage storage) {
		recvDataSc = sc;
		this.storage = storage;
	}

	@Override
	public void run () {
		try {
			ois = new ObjectInputStream(recvDataSc.getInputStream());
			CommData data = (CommData)ois.readObject();
			readData(data); //TODO ack返す

			ois.close();
			recvDataSc.close();
		} catch (Exception e) {
			DEBUG.err("Clientside socket client triggered failure.",e);
			try {
				ois.close();
				recvDataSc.close();
			} catch (Exception ee) {
				DEBUG.err("Socket cannot close.",ee);
			}
		}
	}

	public synchronized void readData(CommData data) {
		String name = storage.storeRecvMsg(data.myId, data.getMsg());
		System.out.println("\n>>> New message from "+name);
		System.out.print("LIMIC > ");
	}
}
