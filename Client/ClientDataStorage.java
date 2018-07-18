package LIMIC.Client;

import LIMIC.core.CONST;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.concurrent.LinkedBlockingQueue;

public class ClientDataStorage {

	protected String userName;
	protected int userId;
	protected String comment;

	protected String IP;
	protected int PORT;

	protected HashMap<String, Integer> friendsNtoId;
	protected HashMap<Integer, String> friendsIdtoN;

	protected HashMap<Integer, LinkedBlockingQueue<String>> histories;

	protected MessageSender sender;
	
	public ClientDataStorage(String name, String ip, int port, int userId, MessageSender sender) {
		userName = name;
		comment = "";
		IP = ip;
		PORT = port;
		friendsNtoId = new HashMap<String,Integer>();
		friendsIdtoN = new HashMap<Integer,String>();
		histories = new HashMap<Integer, LinkedBlockingQueue<String>>();

		friendsNtoId.put(name,userId);
		friendsIdtoN.put(userId,name);

		this.userId = userId;
		this.sender = sender;
	}

	public String getAddress(){
		return IP+":"+String.valueOf(PORT);
	}

	public void changeUserName(String newName) {
		userName = newName;
		sender.sendSelfInfo(CONST.SEND_NAME, newName);
	}

	public void changeComment(String newComment) {
		comment = newComment;
		sender.sendSelfInfo(CONST.SEND_COMMENT, newComment);
	}

	public int getFriendId(String friendName) {
		return friendsNtoId.get(friendName);
	}

	public void addFriend(String name, int id) {
		friendsNtoId.put(name, id);
		friendsIdtoN.put(id,name);
		LinkedBlockingQueue queue = new LinkedBlockingQueue(10);
		queue.add("This is the first talk with "+name+"!");
		histories.put(id, queue);
	}

	public void showFriends() {
		for (Entry<String, Integer> entry : friendsNtoId.entrySet()) {
   			 System.out.println(entry.getKey()+":"+entry.getValue());
		}
	}

	public String storeRecvMsg(int id, String str) {
		try {
			String name = friendsIdtoN.get(id);
			LinkedBlockingQueue queue = histories.get(id);
			if (queue.remainingCapacity() < 1) {
				queue.poll();
			}
			queue.offer(name+":"+str);

			return name;
		} catch (Exception e) {
			return "Anonymous";
		}
	}

	public String storeSendMsg(int id, String str) {
		String name = userName;
		LinkedBlockingQueue queue = histories.get(id);
		if (queue.remainingCapacity() < 1) {
			queue.poll();
		}
		queue.offer(name+":"+str);
		return name;
	}

	public void outputTalk(String name) {
		int id = friendsNtoId.get(name);
		LinkedBlockingQueue queue = histories.get(id);
		for(Object msg : queue.toArray(new String[0])) {
			System.out.println((String)msg);
		}
	}

}
