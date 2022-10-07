package com.talasila.downloader.controller;

import java.net.MalformedURLException;
import java.security.Principal;
import java.time.Duration;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.talasila.downloader.model.DownloadFileInfo;
import com.talasila.downloader.model.GoogleFileInformation;
import com.talasila.downloader.model.request.GetFileInfo;
import com.talasila.downloader.service.DownloadService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@RestController
@Slf4j
public class DownloadController {

	Logger log = LoggerFactory.getLogger(DownloadController.class);

	@Autowired
	DownloadService service;

	@PostMapping("/getfileinfo")
	public GoogleFileInformation getFileInfo(@RequestBody GetFileInfo fileInfo,
			@RegisteredOAuth2AuthorizedClient("google") OAuth2AuthorizedClient authorizedClient)
			throws MalformedURLException {
		// log.info(getFileInfo.getUrl());
		return service.getFileInformation(fileInfo, authorizedClient);
	}

	@PostMapping("/startdownload")
	public String download(@RequestBody GoogleFileInformation googleFileInformation,
			@RegisteredOAuth2AuthorizedClient("google") OAuth2AuthorizedClient authorizedClient) {
		return service.startDownload(googleFileInformation, authorizedClient);
	}

	@GetMapping(value = "/event/resources/usage", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<List<DownloadFileInfo>> getResourceUsage() {

		return Flux.interval(Duration.ofSeconds(1)).map(it -> service.getDownloadingList());

	}

	@GetMapping("/user")
	public Principal user(Principal principal) {
		
		return principal;
	}

	@GetMapping("/test")
	public String test() {

		return "Hello World";
	}

}
