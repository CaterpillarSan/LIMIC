package LIMIC.core;

import java.io.Serializable;

public class CommData implements Serializable {
	// TODO 暗号化?
	
	
	// 送信内容
	public int tag;
	private String msg;

	//送信元情報
	public int friendId;
	public int myId;

	public CommData(int tag, String msg, int idTo, int idFrom) {
		this.tag = tag;
		this.msg = msg;
		friendId = idTo;
		myId = idFrom;
	}

	public String getMsg(){
		// TODO key
		return msg;
	}
}
