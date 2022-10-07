package com.talasila.downloader.model;

import java.util.Date;

public class DownloadFileInfo {

	private String fileId;
	private String fileName;
	private Date adddedOn;
	private long size;
	private int progress;
	private double speed;
	public String getFileId() {
		return fileId;
	}
	public void setFileId(String fileId) {
		this.fileId = fileId;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public Date getAdddedOn() {
		return adddedOn;
	}
	public void setAdddedOn(Date adddedOn) {
		this.adddedOn = adddedOn;
	}
	public int getProgress() {
		return progress;
	}
	public void setProgress(int progress) {
		this.progress = progress;
	}
	public double getSpeed() {
		return speed;
	}
	public void setSpeed(double speed) {
		this.speed = speed;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	
	
	
}
