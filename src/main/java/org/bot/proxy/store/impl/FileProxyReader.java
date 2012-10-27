package org.bot.proxy.store.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.bot.proxy.store.ProxyReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gargoylesoftware.htmlunit.ProxyConfig;

public class FileProxyReader implements ProxyReader {
	private static final Logger LOG = LoggerFactory.getLogger(FileProxyReader.class);
	private static final String HOST_PORT_SEPARATOR = ":";
	
	private File proxyFile;

	public FileProxyReader(File proxyFile) {
		this.proxyFile = proxyFile;
	}

	public Collection<ProxyConfig> getProxies() throws IOException {
		List<String> lines = FileUtils.readLines(proxyFile);
		return parseProxies(lines);
	}
	
	protected Collection<ProxyConfig> parseProxies(List<String> lines) throws IOException {
		List<ProxyConfig> proxies = new ArrayList<ProxyConfig>();
		ProxyConfig proxyConfig = null;
		
		for (String s : lines){
			proxyConfig = parseProxy(s);
			if (proxyConfig != null){
				proxies.add(proxyConfig);
			}
		}
		
		return proxies;
	}
	
	protected ProxyConfig parseProxy(String proxyString){
		String[] s = proxyString.split(HOST_PORT_SEPARATOR);
		if (s.length != 2) {
			LOG.warn("Failed to parse proxy from " + proxyString);
			return null;
		}
		
		String host = s[0];
		int port = 0;
		
		try {
			port = Integer.parseInt(s[1]);
		} catch (NumberFormatException e) {
			LOG.warn("Failed to parse port from " + s[1]);
			return null;
		}
		
		return  new ProxyConfig(host, port);
	}
}
