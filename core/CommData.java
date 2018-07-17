package LIMIC.core;

import java.io.Serializable;

public class CommData implements Serializable {
	// TODO 暗号化?
	
	// 送信先情報 //TODO グループ
	public String ipTo;
	public int portTo;
	
	// 送信内容
	private String msg;

	//送信元情報
	public String ipFrom;
	public int portFrom;

	public CommData(String ipTo, int portTo, String msg, String ipFrom, int portFrom) {
		this.ipTo = ipTo;
		this.portTo = portTo;
		this.msg = msg;
		this.ipFrom = ipFrom;
		this.portFrom = portFrom;
	}

	public String getMsg(){
		// TODO key
		return msg;
	}
}
