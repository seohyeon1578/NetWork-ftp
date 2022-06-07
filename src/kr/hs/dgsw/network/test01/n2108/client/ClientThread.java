package kr.hs.dgsw.network.test01.n2108.client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

import kr.hs.dgsw.network.test01.n2108.protocol.Protocol;

public class ClientThread extends Thread{
	
	public Socket sc = null;

	private OutputStream os = null;
	private InputStream is = null;
	byte[] buf = null;
	
	public ClientThread(Socket sc) {
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
			
			while(true){
				Protocol protocol = new Protocol();
				
			    buf = protocol.getPacket();
				
				is.read(buf);
	
				int packetType = buf[0];
			    protocol.setPacket(packetType, buf);
			    
			    File file = new File("D:\\UPLOAD\\");
				File[] files = file.listFiles();
			    
			    if(packetType == Protocol.PT_EXIT){
			    	System.out.println("접속 종료..");
			    	break;
			    }
			    
			    if(packetType == Protocol.PT_UNDEFINED) {
			    	Scanner scan = new Scanner(System.in);
			    	String command = scan.nextLine();
			    	if(command.equals("/파일목록")) {
			    		protocol = new Protocol(Protocol.RES_FILELIST);
			    		
			    	}else if(command.substring(0, 4).equals("/업로드")) {
			    		String filePath = command.split(" ")[1];
			    		String[] length = filePath.split("/");
			    		String fileName = length[length.length - 1];
			    		if(command.split(" ").length > 2) {
			    			fileName = command.split(" ")[2];
			    		}
			    		protocol = new Protocol(Protocol.RES_FILECHECK);
			    		protocol.setFilePath(filePath);
			    		protocol.setFileName(fileName);
			    		
			    	}else if(command.substring(0, 5).equals("/다운로드")) {
			    		String DWFileName = command.split(" ")[1];
			    		protocol = new Protocol(Protocol.RES_DOWNLOAD);
			    		protocol.setDWFileName(DWFileName);
			    		
			    	}else if(command.equals("/접속종료")) {
			    		protocol = new Protocol(Protocol.PT_EXIT);
			    		
			    	}else {
			    		protocol = new Protocol(Protocol.PT_UNDEFINED);
			    	}
			    	os.write(protocol.getPacket());
			    }
		    	
			    switch(packetType){
				    case Protocol.REQ_LOGIN :
					    BufferedReader userIn = new BufferedReader(new InputStreamReader(System.in));
					    System.out.print("ID: ");
					    String id = userIn.readLine();
					    System.out.print("PASS: ");
					    String pass = userIn.readLine();
		
					    protocol = new Protocol(Protocol.RES_LOGIN);
					    protocol.setId(id);
					    protocol.setPassword(pass);
					    os.write(protocol.getPacket());
					    break;
				    
				    case Protocol.LOGIN_RESULT :
				    	String result = protocol.getLoginCheck();
				    	
				    	if(result.equals("1")){
				    		System.out.println("** FTP 서버에 접속하였습니다. **");
				    		protocol = new Protocol(Protocol.PT_UNDEFINED);
				    	}else if(result.equals("2") || result.equals("3")){
				    		System.out.println("** ID 또는 PASS가 틀렸습니다.! **");
				    		protocol = new Protocol(Protocol.RES_RELOGIN);
				    	}
				    	
				    	os.write(protocol.getPacket());
				    	break;
				    case Protocol.FILELIST_RESULT :
				    	String FileCount = protocol.getFileCount();
				    	String FileName = protocol.getFileList();
				    	String size = protocol.getFileSize();
				    	
				    	System.out.println("** [File List] **");
				    	for(int i = 0; i < Integer.parseInt(FileCount); i++) {
				    		
				    		System.out.printf("** %-15s %10s **\n", FileName.split(" ")[i], size.split(" ")[i]);				    		
				    	}
				    	System.out.println("** " + FileCount + "개 파일 **");
				    	
				    	protocol = new Protocol(Protocol.PT_UNDEFINED);
				    	os.write(protocol.getPacket());
				    	break;
				    case Protocol.REQ_DOWNLOAD :
				    	String DWFileName = protocol.getDWFileName();
				    	FileDownload fd = new FileDownload(DWFileName);
				    	
				    	int nDownReturn = fd.down();
				    	
				    	if(nDownReturn == 1)System.out.println("** "+ DWFileName + "을 D:/download/로 다운로드 하였습니다. **");
				    	
				    	protocol = new Protocol(Protocol.PT_UNDEFINED);
				    	os.write(protocol.getPacket());
				    	break;
				    case Protocol.NONE_FILE : 
				    	System.out.println("해당 파일이 없습니다.");
				    	
				    	protocol = new Protocol(Protocol.PT_UNDEFINED);
				    	os.write(protocol.getPacket());
				    	break;
				    case Protocol.REQ_FILECHECK :
				    	String fileName = protocol.getFileName();
				    	String filePath = protocol.getFilePath();
				    	String sName = protocol.getFileIn();

			    		if(sName.equals("1")) {
			    			System.out.print("파일이 이미 있습니다. 덮어쓰기 하실건가요??(Yes: 덮어쓰기 / No: 업로드 취소):");
			    			Scanner scan = new Scanner(System.in);
			    			String overwrite = scan.nextLine();
			    			if(overwrite.equals("Yes")) {
			    				System.out.println("** " + fileName + " 파일을 업로드하였습니다.**");
			    				DataOutputStream dos = new DataOutputStream(this.os);
			    				BufferedOutputStream bos = new BufferedOutputStream(dos);
			    				
			    				File fl = new File(filePath);

			    				BufferedInputStream bis = new BufferedInputStream(
			    						new FileInputStream(fl)
			    				);

			    				protocol = new Protocol(Protocol.RES_UPLOAD);
			    				protocol.setFileName(fileName);
			    				os.write(protocol.getPacket());
			    				
			    				byte[] bytes = new byte[1024];
			    				int readbit = 0;
			    				
			    				while((readbit = bis.read(bytes)) != -1)
			    					bos.write(bytes,0,readbit);
			    				
			    				bos.flush();
			    			}else {
			    				System.out.println("업로드를 취소하였습니다.");
			    				protocol = new Protocol(Protocol.PT_UNDEFINED);
			    				os.write(protocol.getPacket());
			    			}
			    		}else {
				    		System.out.println("** " + fileName + " 파일을 업로드하였습니다.**");
				    		DataOutputStream dos = new DataOutputStream(this.os);
		    				BufferedOutputStream bos = new BufferedOutputStream(dos);
		    				
		    				File fl = new File(filePath);

		    				BufferedInputStream bis = new BufferedInputStream(
		    						new FileInputStream(fl)
		    				);
		    				
		    				protocol = new Protocol(Protocol.RES_UPLOAD);
		    				protocol.setDWFileName(fileName);
		    				os.write(protocol.getPacket());

		    				byte[] bytes = new byte[1024];
		    				int readbit = 0;
				    		
		    				while((readbit = bis.read(bytes)) != -1)
		    					bos.write(bytes, 0, readbit);
		    				
		    				bos.flush();
			    		}
			    		
			    }
			   
			  }
	
			  os.close();
			  is.close();
			  sc.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
