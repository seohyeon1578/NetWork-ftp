package kr.hs.dgsw.network.test01.n2108.client;

import java.net.Socket;

public class ClientMain {

	public static void main(String[] args) {
		
		try {
			Socket sc = new Socket("10.80.162.41",5000);
			
			System.out.println("** 서버에 접속하였습니다. **");
			
			Thread ct = new ClientThread(sc);
			ct.start();
			
		} catch (Exception e) {
			System.out.println("연결 종료..");
			e.printStackTrace();
		}
	}
}
