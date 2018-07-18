package LIMIC.Client;

import LIMIC.core.*;
import LIMIC.debugger.*;

import java.net.Socket;
import java.io.ObjectOutputStream;

public class MessageSender {

	private int userId;

	public MessageSender (int id) {
		userId = id;
	}

	
	public void send(CommData data) {
		try {
			Socket sendDataSc = new Socket(CONST.SERVER_IP, CONST.SERVER_PORT);
			ObjectOutputStream oos = new ObjectOutputStream(sendDataSc.getOutputStream());
			
			oos.writeObject(data);
			
			oos.close();
			sendDataSc.close();
			
		} catch (Exception e) {
			DEBUG.err("Server side cannot connect to "+data.friendId +":"+ data.myId, e);
		}
	}

	public void initialComm(ClientDataStorage storage) {
		String msg = storage.getAddress()+":"+storage.userName;
		CommData data = new CommData(CONST.INITIAL, msg, -1, storage.userId);
		send(data);
	}

	public void sendSelfInfo(int tag, String str) {
		CommData data = new CommData(tag, str, -1, userId);
		send(data);
	}

	public void sendMsg(int userIdTo, String msg) {
		CommData data = new CommData(CONST.SEND_MSG, msg, userIdTo, userId);
		send(data);
	}

}
