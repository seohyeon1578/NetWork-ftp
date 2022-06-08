package kr.hs.dgsw.network.test01.n2108.protocol;

import java.io.Serializable;

public class Protocol implements Serializable {

	public static final int PT_UNDEFINED = -1;
	public static final int PT_EXIT = 0;
	public static final int REQ_LOGIN = 1;   		//로그인요청
	public static final int RES_LOGIN = 2;   		//인증요청
	public static final int LOGIN_RESULT = 3;  		//인증결과
	public static final int RES_RELOGIN = 4;		//로그인 재요청
	public static final int RES_FILELIST = 5;		//파일 리스트 요청
	public static final int FILELIST_RESULT = 6;	//파일 리스트 결과
	public static final int RES_UPLOAD = 7;			//업로드 요청
	public static final int UPLOAD_RESULT = 8;		//업로드 결과
	public static final int RES_DOWNLOAD = 9;		//다운로드인증 요청
	public static final int REQ_DOWNLOAD = 10;		//다운로드 요청
	public static final int NONE_FILE = 11;			//다운로드 요청 파일 없음
	public static final int RES_FILECHECK = 12;		// 파일체크요청
	public static final int REQ_FILECHECK = 13;
	public static final int LEN_LOGIN_ID = 10;   		//ID길이
	public static final int LEN_LOGIN_PASSWORD = 10; 	//PW길이
	public static final int LEN_LOGIN_RESULT = 2;  		//로그인인증값 길이
	public static final int LEN_PROTOCOLTYPE = 1;  	//프로토콜타입 길이
	public static final int LEN_FILELIST = 100;		//파일 길이
	public static final int LEN_FILEPATH = 50;
	public static final int LEN_FILEIN = 2;
	public static final int LEN_FILESIZE = 100;
	public static final int LEN_FILECOUNT = 2;
	public static final int LEN_FILENAME = 100;		//파일이름 길이
	public static final int LEN_FILELIST_RESULT = 1000;   //파일여부 길이
	public static final int LEN_MAX = 1000;    			//최대 데이타 길이
	
	protected int protocolType;
	
	private byte[] packet = new byte[1024]; 
	
	 public Protocol(){
		  this(PT_UNDEFINED);
	}

	public Protocol(int protocolType) {
		// TODO Auto-generated constructor stub
		this.protocolType = protocolType;
		
		getPacket(protocolType);
	}

	public byte[] getPacket(int protocolType) {
		
		if (packet == null) {
			
			switch (protocolType) {
			case REQ_LOGIN : packet = new byte[LEN_PROTOCOLTYPE]; break;
		    case RES_LOGIN : packet = new byte[LEN_PROTOCOLTYPE + LEN_LOGIN_ID + LEN_LOGIN_PASSWORD]; break;
		    case RES_RELOGIN : packet = new byte[LEN_PROTOCOLTYPE]; break;
		    case RES_FILELIST : packet = new byte[LEN_MAX]; break;
		    case RES_FILECHECK : packet = new byte[LEN_PROTOCOLTYPE + LEN_FILENAME + LEN_FILEPATH]; break;
		    case REQ_FILECHECK : packet = new byte[LEN_PROTOCOLTYPE + LEN_FILENAME + LEN_FILEPATH + LEN_FILEIN]; break;
		    case RES_UPLOAD : packet = new byte[LEN_PROTOCOLTYPE + LEN_FILENAME]; break;
		    case RES_DOWNLOAD : packet = new byte[LEN_PROTOCOLTYPE + LEN_FILENAME]; break;
		    case REQ_DOWNLOAD : packet = new byte[LEN_PROTOCOLTYPE + LEN_FILENAME]; break;
		    case NONE_FILE : packet = new byte[LEN_PROTOCOLTYPE]; break;
		    case FILELIST_RESULT : packet = new byte[LEN_FILELIST_RESULT]; break;
		    case PT_UNDEFINED : packet = new byte[LEN_MAX]; break;
		    case LOGIN_RESULT : packet = new byte[LEN_PROTOCOLTYPE + LEN_LOGIN_RESULT]; break;
		    case PT_EXIT : packet = new byte[LEN_PROTOCOLTYPE]; break;
		    
			}
		}
		
		packet[0] = (byte)protocolType;
		
		return packet;
		
	}
	
	public String getLoginCheck() {
		return new String (packet, LEN_PROTOCOLTYPE, LEN_LOGIN_RESULT).trim();
	}
	
	public void setLoginCheck(String ok) {
		 System.arraycopy(ok.trim().getBytes(), 0, packet, LEN_PROTOCOLTYPE, ok.trim().getBytes().length); 
	}
	
	public void setProtocolType(int protocolType){
		this.protocolType = protocolType;
	}

		 
	public int getProtocolType(){
		return protocolType;
	}
		 
		 
	public byte[] getPacket(){
		return packet;
	}
		 
		 
	public void setPacket(int pt, byte[] buf){
		packet = null;
		packet = getPacket(pt);
		protocolType = pt;
		System.arraycopy(buf, 0, packet, 0, packet.length);
	}
		 
		 
	public String getId(){
		return new String(packet, LEN_PROTOCOLTYPE, LEN_LOGIN_ID).trim();
	} 
		 
	public void setId(String id){
		System.arraycopy(id.trim().getBytes(), 0, packet, LEN_PROTOCOLTYPE, id.trim().getBytes().length);
	}
		 
		 
	public String getPassword(){
		return new String(packet, LEN_PROTOCOLTYPE + LEN_LOGIN_ID, LEN_LOGIN_PASSWORD).trim();
	}
		 
		 
	public void setPassword(String password){
		System.arraycopy(password.trim().getBytes(), 0, packet, LEN_PROTOCOLTYPE+LEN_LOGIN_ID, password.trim().getBytes().length);
		packet[LEN_PROTOCOLTYPE + LEN_LOGIN_ID + password.trim().getBytes().length] = '\0';
	}
	
	public String getFileCount() {
		return new String(packet, LEN_PROTOCOLTYPE, LEN_FILECOUNT).trim();
	}
	
	public void setFileCount(String count) {
		System.arraycopy(count.trim().getBytes(), 0, packet, LEN_PROTOCOLTYPE, count.trim().getBytes().length);
	}
	
	public String getFileList() {
		return new String(packet, LEN_PROTOCOLTYPE + LEN_FILECOUNT, LEN_FILELIST).trim();
	}
	
	public void setFileLIst(String name) {
		System.arraycopy(name.trim().getBytes(), 0, packet, LEN_PROTOCOLTYPE + LEN_FILECOUNT, name.trim().getBytes().length);
		packet[LEN_PROTOCOLTYPE + LEN_FILECOUNT + name.trim().getBytes().length] = '\0';
	}
	
	public String getFileSize() {
		return new String(packet, LEN_PROTOCOLTYPE + LEN_FILECOUNT + LEN_FILELIST, LEN_FILESIZE).trim();
	}
	
	public void setFileSize(String size) {
		System.arraycopy(size.trim().getBytes(), 0, packet, LEN_PROTOCOLTYPE + LEN_FILECOUNT + LEN_FILELIST, size.trim().getBytes().length);
		packet[LEN_PROTOCOLTYPE + LEN_FILECOUNT + LEN_FILELIST+ size.trim().getBytes().length] = '\0';
	}
	
	public String getFilePath() {
		return new String(packet, LEN_PROTOCOLTYPE , LEN_FILEPATH).trim();
	}
	
	public void setFilePath(String path) {
		System.arraycopy(path.trim().getBytes(), 0, packet, LEN_PROTOCOLTYPE, path.trim().getBytes().length);
	}
	
	public String getFileName() {
		return new String(packet, LEN_PROTOCOLTYPE + LEN_FILEPATH, LEN_FILENAME).trim();
	}
	
	public void setFileName(String name) {
		System.arraycopy(name.trim().getBytes(), 0, packet, LEN_PROTOCOLTYPE + LEN_FILEPATH, name.trim().getBytes().length);
		packet[LEN_PROTOCOLTYPE + LEN_FILEPATH + name.trim().getBytes().length] = '\0';
	}
	
	public String getFileIn() {
		return new String(packet, LEN_PROTOCOLTYPE + LEN_FILEPATH + LEN_FILENAME, LEN_FILEIN).trim();
	}
	
	public void setFileIn(String in) {
		System.arraycopy(in.trim().getBytes(), 0, packet, LEN_PROTOCOLTYPE + LEN_FILEPATH + LEN_FILENAME, in.trim().getBytes().length);
		packet[LEN_PROTOCOLTYPE + LEN_FILEPATH + LEN_FILENAME + in.trim().getBytes().length] = '\0';
	}
	
	public String getDWFileName() {
		return new String(packet, LEN_PROTOCOLTYPE, LEN_FILENAME).trim();
	}
	
	public void setDWFileName(String name) {
		System.arraycopy(name.trim().getBytes(), 0, packet, LEN_PROTOCOLTYPE, name.trim().getBytes().length);
	}
		  
}
