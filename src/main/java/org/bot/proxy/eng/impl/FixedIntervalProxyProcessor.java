package org.bot.proxy.eng.impl;

import java.util.Collection;

import org.bot.proxy.eng.BatchProxyProcessor;
import org.bot.proxy.eng.WebAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gargoylesoftware.htmlunit.ProxyConfig;
import com.gargoylesoftware.htmlunit.WebClient;

public class FixedIntervalProxyProcessor implements BatchProxyProcessor {
	private static final Logger LOG = LoggerFactory.getLogger(FixedIntervalProxyProcessor.class);
	private long interval = 10000;

	public FixedIntervalProxyProcessor(long interval) {
		this.interval = interval;
	}

	public void performActions(WebAction webAction, Collection<ProxyConfig> proxies) {
		WebClient wc = new WebClient();

		for (ProxyConfig pc : proxies) {
			LOG.info("Performing action using proxy " + pc);

			wc.setProxyConfig(pc);
			webAction.performAction(wc);

			LOG.info("Action completed using proxy " + pc);
			try {
				Thread.sleep(interval);
			} catch (InterruptedException e) {
				LOG.error("Failed during sleep ", e);
			}
		}
	}
}
