package org.bot.proxy.store;

import java.io.IOException;
import java.util.Collection;

import com.gargoylesoftware.htmlunit.ProxyConfig;

public interface ProxyReader {
	public Collection<ProxyConfig> getProxies() throws IOException;
}
