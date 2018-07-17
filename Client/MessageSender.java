package LIMIC.Client;

import LIMIC.core.*;
import LIMIC.debugger.*;

import java.net.Socket;
import java.io.ObjectOutputStream;

public class MessageSender {

	private String myIp;
	private int myPort;

	public MessageSender (String ip, int port) {
		myIp = ip;
		myPort = port;
	}

	
	public void send(String ipTo, int portTo, String msg) {
		CommData data = new CommData(ipTo, portTo, msg, myIp, myPort);
		try {
			Socket sendDataSc = new Socket(ipTo, portTo);
			ObjectOutputStream oos = new ObjectOutputStream(sendDataSc.getOutputStream());
			
			oos.writeObject(data);
			
			oos.close();
			sendDataSc.close();
			
		} catch (Exception e) {
			DEBUG.err("Server side cannot connect to "+data.ipTo +":"+ data.portTo, e);
		}
	}

}
