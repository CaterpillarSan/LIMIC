package LIMIC.core;

import java.util.concurrent.LinkedBlockingQueue;
import java.security.PublicKey;

public class UserData {
	
	public int id;
	public String name;

	public PublicKey pubKey;
	
	public LinkedBlockingQueue<String> history;

	public UserData (String name, int id) {
		this.id = id;
		this.name = name;
		history = new LinkedBlockingQueue<String>(10);
		history.add("This is the first talk with "+name+"!");
	}
}
