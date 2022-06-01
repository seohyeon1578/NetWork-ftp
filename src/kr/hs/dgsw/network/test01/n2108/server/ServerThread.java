package kr.hs.dgsw.network.test01.n2108.server;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import kr.hs.dgsw.network.test01.n2108.protocol.Protocol;

public class ServerThread extends Thread {

	public Socket sc = null;

	private OutputStream os = null;
	private InputStream is = null;
	byte[] buf = null;
	String filefolder = "D:\\test\\";
	
	public ServerThread(Socket sc) {
		this.sc = sc;
		
		try {
			this.os = sc.getOutputStream();
			this.is = sc.getInputStream();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	public void run() {
		try {
			Protocol protocol = new Protocol(Protocol.REQ_LOGIN);
			
			os.write(protocol.getPacket());
			
			while(true){
				protocol = new Protocol();
				
				byte[] buf = protocol.getPacket();
				
				is.read(buf);
				
				int packetType = buf[0];
				
				protocol.setPacket(packetType, buf);
				
				if(packetType == Protocol.PT_EXIT){
				    protocol = new Protocol(Protocol.PT_EXIT);
				    os.write(protocol.getPacket());
				    System.out.println(sc.getInetAddress() + ": 종료");
				    break;
				}
				
				if(packetType == Protocol.PT_UNDEFINED) {
					protocol = new Protocol(Protocol.PT_UNDEFINED);
					os.write(protocol.getPacket());
				}
				
				File file = new File(filefolder);
				File[] files = file.listFiles();
				
				if(!file.exists()) {
					file.mkdirs();
				}
				
				switch(packetType){
					case Protocol.RES_LOGIN :
					    System.out.println("클라이언트가 로그인 정보를 보냈습니다.");
					    String id = protocol.getId();
					    String password = protocol.getPassword();
					    System.out.println("ID:" + id + ", PASS: " + password);
					    
					    if(id.equals("admin")){
					    	
					    	 if(password.equals("1234")){
					    		 protocol = new Protocol(Protocol.LOGIN_RESULT);
					    	     protocol.setLoginCheck("1");
					    	     System.out.println("로그인 성공");
					    	 }else{
					    	      protocol = new Protocol(Protocol.LOGIN_RESULT);
					    	      protocol.setLoginCheck("2");
					    	      System.out.println("암호 틀림");
					    	      
					    	 }
					    }else{
					        protocol = new Protocol(Protocol.LOGIN_RESULT);
					        protocol.setLoginCheck("3");
					        System.out.println("아이디 존재 안함");
					    }
					      
					    System.out.println("로그인 처리 결과 전송");
					    os.write(protocol.getPacket());
					    break;
					case Protocol.RES_RELOGIN :
						protocol = new Protocol(Protocol.REQ_LOGIN);
						os.write(protocol.getPacket());
						break;
					case Protocol.RES_FILELIST :
						System.out.println(sc.getInetAddress() + ": 파일리스트 요청");
						
						int count = 0;
						for(int i = 0; i < files.length; i++) {
							if(files[i].isFile()) {
								count++;
							}
						}
						String fileCount = Integer.toString(count);
						
						protocol = new Protocol(Protocol.FILELIST_RESULT);
						protocol.setFileCount(fileCount);
			    		os.write(protocol.getPacket());
						break;
					case Protocol.RES_UPLOAD :
						System.out.println("파일저장");
						
						String originalFilePath = protocol.getFilePath();
						String newFileName = protocol.getFileName();
						
						String copyFilePath = filefolder + newFileName;
						
						FileCopy cnd = new FileCopy(originalFilePath,copyFilePath,filefolder);
						int nCopyReturn = cnd.copy();
						
						if(nCopyReturn == 1)System.out.println(newFileName + " 저장");
						
						protocol = new Protocol(Protocol.PT_UNDEFINED);
						os.write(protocol.getPacket());
						
						break;
					case Protocol.RES_DOWNLOAD :
						String downloadFile = protocol.getDWFileName();
						
						boolean dwName = false;
						for(int i = 0; i < files.length; i++) {
			    			
							if(files[i].getName().equals(downloadFile)) {
								dwName = true;
								break;
							}
			    		}
						
						if(dwName) {
							System.out.println(sc.getInetAddress() + ": " + downloadFile + " 파일 다운로드 요청");
							
							protocol = new Protocol(Protocol.REQ_DOWNLOAD);
							protocol.setDWFileName(downloadFile);
						}else {
							System.out.println(downloadFile + "파일이 없음.");
							protocol = new Protocol(Protocol.NONE_FILE);
						}
						os.write(protocol.getPacket());
						
						break;
					
				}
			}
			is.close();
			os.close();
			sc.close();
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
