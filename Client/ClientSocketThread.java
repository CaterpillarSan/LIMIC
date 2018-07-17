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
	ExecutorService exec;

	protected ClientSocketThread(int port) {
		try {
			ss = new ServerSocket(port);
			exec = Executors.newFixedThreadPool(10);
		} catch (Exception e) {
			DEBUG.err("client socket cannot be established.",e);
		}
	}

	@Override
	public void run() {
		while(ClientMain.isRunning()){
			try {
				Socket sc = ss.accept();
				exec.submit(new SocketClient(sc));
			} catch (Exception e) {
				DEBUG.err("client socket cannot create Comm with Client.",e);
			}
		}
	}
}

class SocketClient implements Runnable {
	Socket recvDataSc;
	ObjectInputStream ois;

	SocketClient(Socket sc) {
		recvDataSc = sc;
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

	public void readData(CommData data) {
		// 安全性向上
		ClientMain.outputData(data);
	}
}
