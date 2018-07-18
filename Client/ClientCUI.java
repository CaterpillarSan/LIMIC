package LIMIC.Client;

import LIMIC.debugger.*;

import java.util.Scanner;

public class ClientCUI {
	
	private Scanner scan;
	private MessageSender sender;
	private ClientDataStorage storage;

	public static int current; // negative -> home dir, positive -> current viewing friend id 
	public static String dir = "Home";

	public ClientCUI(MessageSender sender, ClientDataStorage storage) {
		scan = new Scanner(System.in);
		// scan.useDelimiter("¥n");
		this.sender = sender;
		this.storage = storage;
		current = -1;
	}

	public boolean inputCommand() {
		System.out.print("\nLIMIC ["+dir+"] > ");	
		String[] input = scan.nextLine().split(" ",2);
		try {
		switch(input[0]) {
			case "exit":			// > exit
				System.out.println("Good bye!");
				return false;
			case "changeName":		// > changeName Haruka
				storage.changeUserName(input[1]);
				break;
			case "changeComment":	// > changeComment Hello!
				storage.changeComment(input[1]);
				break;
			case "send":			// friend > send hello.
 				if (current > 0) {
					sender.sendMsg(current, input[1]);
					storage.storeSendMsg(current, input[1]);
				} else {
					System.out.println("Cannot use send command when open Home dir.");
				}
				break;
			case "mkdir":		// mkdir Hanako:12345 = add friend
				addFriend(input[1]);
			case "ls":				// ls
				if (current > 0) {
					System.out.println("Cannot use ls command when open friend dir.");
				} else {
					storage.showFriends();
				}
				break;
			case "cd":				// cd Hanako
				if (input.length < 2) {
					moveToHome();
				} else if (current > 0 && input[1] == "..") {
					moveToHome();
				} else {
					moveToFriend(input[1]);
				}
				break;
		}
		} catch (Exception e) {
			DEBUG.err("Wrong command.",e);
		}
		return true;
 	}

	public void moveToHome() {
		current = -1;
		System.out.printf("\033[2J");	// clear console
		dir = "Home";
	}
	
	public void moveToFriend(String name) {
		current = storage.getFriendId(name);
		System.out.printf("\033[2J");	// clear console
		System.out.println("--- talk channel with "+ name);
		storage.outputTalk(name);
		dir = name;
	}

	public void addFriend(String friendData) {
		// friendData Hanako:12345
		String[] data = friendData.split(":",2);
		storage.addFriend(data[0], Integer.parseInt(data[1]));
	}

}
