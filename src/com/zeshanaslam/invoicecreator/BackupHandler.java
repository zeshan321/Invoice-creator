package com.zeshanaslam.invoicecreator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.time.LocalDate;

public class BackupHandler {
	
	File backupDir = null;
	File source = null;
	
	public BackupHandler() {
		source = new File("data.db");
		
		backupDir = new File(System.getProperty("user.home") + "/Desktop/"  + "Invoice Backup/");
		backupDir.mkdirs();
	}
	
	public void startBackup() {
		copyFile(source, new File(System.getProperty("user.home") + "/Desktop/"  + "Invoice Backup/" + LocalDate.now().toString() + ".db"));
	}
	
	public void copyFile(File source, File dest) {
		try {
			FileChannel inputChannel = null;
			FileChannel outputChannel = null;
			try {
				inputChannel = new FileInputStream(source).getChannel();
				outputChannel = new FileOutputStream(dest).getChannel();
				outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
			} finally {
				inputChannel.close();
				outputChannel.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
