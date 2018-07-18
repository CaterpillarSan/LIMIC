package LIMIC.Server;

import LIMIC.debugger.*;

import java.util.HashMap;

public class ServerDataStorage {

	private HashMap<Integer, UserData> IdToUserData;

	public ServerDataStorage() {
		IdToUserData = new HashMap<Integer, UserData>();
	}

	public void addUser(int id, String Host, int Port, String name) {
		if (IdToUserData.containsKey(id)) {
			DEBUG.log("already registered.");
			return;
		}
		UserData user = new UserData (id, Host, Port, name);
		IdToUserData.put(id, user);
	}

	public void changeName(int id, String name) {
		UserData user = IdToUserData.get(id);
		user.name = name;
	}
	
	public void changeComment(int id, String comm) {
		UserData user = IdToUserData.get(id);
		user.comment = comm;
	}


	public String getHost(int id) {
		UserData user = IdToUserData.get(id);
		return user.HOST;
	}

	public int getPort(int id) {
		UserData user = IdToUserData.get(id);
		return user.PORT;
	}

}

class UserData {
	int Id;
	String HOST;
	int PORT;
	
	String name;
	String comment;

	protected UserData(int id, String host, int port, String name) {
		Id = id;
		HOST = host;
		PORT = port;
		this.name = name;
	}


}
