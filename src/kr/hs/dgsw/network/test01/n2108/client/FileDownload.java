package kr.hs.dgsw.network.test01.n2108.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class FileDownload {

	public String fileName;

	public FileDownload(String fileName) {
		this.fileName = fileName;
	}

	public int down() {
		File originalFile = new File("D:/test/" + fileName);
		File copyFile = new File("D:/download/" + fileName);
		File copyfilefolder = new File("D:/download");
		
		if(!copyfilefolder.exists()) {
			copyfilefolder.mkdirs();
		}
		
		try {
			FileInputStream fis = new FileInputStream(originalFile);
			
			FileOutputStream fos = new FileOutputStream(copyFile);

			int readbit = 0;
			
			while ((readbit = fis.read()) != -1) {
				fos.write(readbit);
			}
			
			fos.flush();
			fis.close();
			fos.close();

		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
		
		return 1;
	}
}
