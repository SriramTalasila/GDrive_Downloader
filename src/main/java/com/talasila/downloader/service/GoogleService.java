package com.talasila.downloader.service;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.net.ssl.HttpsURLConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.talasila.downloader.model.DownloadFileInfo;
import com.talasila.downloader.model.GoogleFileInformation;

@Component
public class GoogleService {
	
	Logger log = LoggerFactory.getLogger(GoogleService.class);
	
	private ConcurrentHashMap<String,DownloadFileInfo> downloadingFiles = new ConcurrentHashMap<>();
	
	@Value("${talasila.downloader.path}")
	private String downloadPath;

	public GoogleFileInformation getFileInfo(String fileId, String accessToken) {
		URI uri;
		try {
			uri = new URI("https://www.googleapis.com/drive/v3/files/"+fileId+"?fields=*");

			RestTemplate rest = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.add("Authorization", "Bearer " + accessToken);
			HttpEntity<String> entity = new HttpEntity<>("body", headers);
			
			ResponseEntity<GoogleFileInformation> response = rest.exchange(uri, HttpMethod.GET, entity, GoogleFileInformation.class);
			System.out.println(response.getBody());
			return response.getBody();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@Async
	public void downloadFile(GoogleFileInformation googleFileInformation,String accessToken) {
		DownloadFileInfo download = new DownloadFileInfo();
		download.setFileId(googleFileInformation.getId());
		download.setFileName(googleFileInformation.getName());
		download.setSize(Long.parseLong(googleFileInformation.getSize()));
		download.setAdddedOn(new Date());
		download.setProgress(0);
		download.setSpeed(0d);
		downloadingFiles.put(googleFileInformation.getId(), download);
		try {
			log.info("Starting Download...........");
			log.info("Starting Download...........");
			URL url = new URL(
					"https://www.googleapis.com/drive/v3/files/"+googleFileInformation.getId()+"?alt=media");
			HttpsURLConnection httpConnection = (HttpsURLConnection) (url.openConnection());
			httpConnection.setRequestProperty ("Authorization", "Bearer "+ accessToken);
			httpConnection.setRequestMethod("GET");
			long completeFileSize = Long.parseLong(googleFileInformation.getSize());
			System.out.println("Complete file size:"+completeFileSize);
			java.io.BufferedInputStream in = new java.io.BufferedInputStream(httpConnection.getInputStream());
			java.io.FileOutputStream fos = new java.io.FileOutputStream(downloadPath+googleFileInformation.getName());
			java.io.BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);
			byte[] data = new byte[1024];
			long downloadedFileSize = 0;
			int x = 0;
		    long starttimeMilli = new Date().getTime();
			while ((x = in.read(data, 0, 1024)) >= 0) {
				long endtimeMilli = new Date().getTime();
				long diff = endtimeMilli-starttimeMilli;
				if(diff > 0) {
					double speed = (x/diff) * 0.001;
					download.setSpeed(Math.round(speed));
				}
				downloadedFileSize += x;				
				// calculate progress
				final int currentProgress = (int) ((((double) downloadedFileSize) / ((double) completeFileSize))
						* 100);
				download.setProgress(currentProgress);
				bout.write(data, 0, x);
				starttimeMilli = new Date().getTime();
			}
			bout.close();
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public List<DownloadFileInfo> getDownloadingList(){
		List<DownloadFileInfo> downloadings = new ArrayList<>();
		for( DownloadFileInfo info : downloadingFiles.values()) {
			downloadings.add(info);
		}
		return downloadings;
	}
}
