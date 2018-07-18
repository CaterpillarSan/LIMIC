package LIMIC.debugger;

public class DEBUG {

	public static final boolean printLog = true;
	public static final boolean printErr = true;
	public static final boolean printStackTrace = false;

	public static void log(String str) {
		if (printLog)
			System.out.println(str);
	}

	public static void err(String str, Exception e) {
		if (printErr)
			System.err.println(str);
		
		if(printStackTrace)
			e.printStackTrace();
	}

}
