package kr.hs.dgsw.network.test01.n2108.server;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class ServerMain {

	public static void main(String[] args) {
		
		ServerSocket ss = null;
		Socket sc = null;
		
		try{
			ss = new ServerSocket(5000);
			System.out.println("서버 시작...");
			
			while(true) {	
				sc = ss.accept();
				System.out.println("주소: " + sc.getInetAddress());
				
				Thread st = new ServerThread(sc);
				st.start();
			}
			
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally {
			try {
				ss.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
}
