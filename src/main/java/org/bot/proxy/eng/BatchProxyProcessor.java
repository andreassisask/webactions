package org.bot.proxy.eng;

import java.util.Collection;

import com.gargoylesoftware.htmlunit.ProxyConfig;

public interface BatchProxyProcessor {
	public void performActions(WebAction webAction,
			Collection<ProxyConfig> proxies);
}
