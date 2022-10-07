package com.talasila.downloader.service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.stereotype.Service;

import com.talasila.downloader.model.DownloadFileInfo;
import com.talasila.downloader.model.GoogleFileInformation;
import com.talasila.downloader.model.request.GetFileInfo;

@Service
public class DownloadService {
	
	
	@Autowired
	private GoogleService gService;
	
	
	
	Logger log = LoggerFactory.getLogger(DownloadService.class);

	public GoogleFileInformation getFileInformation(GetFileInfo getFileInfo, OAuth2AuthorizedClient authorizedClient) throws MalformedURLException {
		URL url = new URL(getFileInfo.getUrl());
		String fileId = "";
		if(getFileInfo.getUrl().contains("uc?")) {
			fileId= url.getQuery().split("=")[1];
		} else {
			fileId = url.getFile().split("/")[3];
		}
		return gService.getFileInfo(fileId, authorizedClient.getAccessToken().getTokenValue());
	}
	
	
	public String startDownload(GoogleFileInformation googleFileInformation, OAuth2AuthorizedClient authorizedClient) {
		gService.downloadFile(googleFileInformation, authorizedClient.getAccessToken().getTokenValue());
		return null;
	}
	
	public List<DownloadFileInfo> getDownloadingList() {
		gService.getDownloadingList();
		return gService.getDownloadingList();
	}
	
	
	

}
