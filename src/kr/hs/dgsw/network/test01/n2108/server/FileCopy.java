package kr.hs.dgsw.network.test01.n2108.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class FileCopy {
	public String originalFilePath;
	public String copyFilePath;
	public String filefolder;

	public FileCopy(String originalFilePath, String copyFilePath,String copyDirPath) {
		this.originalFilePath = originalFilePath;
		this.copyFilePath = copyFilePath;
		this.filefolder = copyDirPath;
	}

	public int copy() {
		File originalFile = new File(originalFilePath);
		File copyFile = new File(copyFilePath);
		File copyfilefolder = new File(filefolder);
		
		if(!copyfilefolder.exists()) {
			copyfilefolder.mkdirs();
		}
		
		try {
			FileInputStream fis = new FileInputStream(originalFile);
			
			FileOutputStream fos = new FileOutputStream(copyFile);

			int readbit = 0;
			byte[] bytes = new byte[1024];
			
			while ((readbit = fis.read(bytes)) != -1) {
				fos.write(bytes, 0, readbit);
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
