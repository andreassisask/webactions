package org.bot.proxy.main;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.bot.proxy.eng.BatchProxyProcessor;
import org.bot.proxy.eng.WebAction;
import org.bot.proxy.eng.impl.FixedIntervalProxyProcessor;
import org.bot.proxy.eng.impl.RegexpLinkClick;
import org.bot.proxy.store.ProxyReader;
import org.bot.proxy.store.impl.FileProxyReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gargoylesoftware.htmlunit.ProxyConfig;

public class LinkClickerRunner implements Runner {
	private static final Logger LOG = LoggerFactory.getLogger(LinkClickerRunner.class);

	public static final String OPTION_REGEXP = "e";
	public static final String OPTION_INTERVAL = "i";
	public static final String OPTION_PROXY_FILE = "p";

	private URL pageUrl;
	private String linkRegexp;
	private Integer interval;
	private File proxyFile;

	public Options getCommandLineOptions() {
		Options o = new Options();
		o.addOption(OPTION_REGEXP, true, "Regular expression to find the link to click");
		o.addOption(OPTION_INTERVAL, true, "Interval in milliseconds between proxies");
		o.addOption(OPTION_PROXY_FILE, true, "File where to read proxies from");
		return o;
	}

	public void run() {
		try {
			runInternal(pageUrl, linkRegexp, interval, proxyFile);
		} catch (IOException e) {
			LOG.error("Exception while execution", e);
		}
	}

	protected void runInternal(URL pageUrl, String linkRegexp, int interval, File proxyFile) throws IOException {

		ProxyReader reader = new FileProxyReader(proxyFile);
		Collection<ProxyConfig> proxies = reader.getProxies();
		WebAction wa = new RegexpLinkClick(pageUrl, linkRegexp);
		BatchProxyProcessor processor = new FixedIntervalProxyProcessor(interval);

		processor.performActions(wa, proxies);
	}

	public boolean parseCommandLine(CommandLine cl) {
		pageUrl = parseUrl(cl);
		if (pageUrl == null)
			return false;

		linkRegexp = parseLinkRegexp(cl);
		if (linkRegexp == null)
			return false;

		interval = parseInterval(cl);
		if (interval == null)
			return false;

		proxyFile = parseProxyFile(cl);
		if (proxyFile == null)
			return false;

		return true;
	}

	public File parseProxyFile(CommandLine cl) {
		String s = cl.getOptionValue(OPTION_PROXY_FILE);
		if (s == null) {
			return null;
		}

		return new File(s);
	}

	public String parseLinkRegexp(CommandLine cl) {
		return cl.getOptionValue(OPTION_REGEXP);
	}

	public Integer parseInterval(CommandLine cl) {
		String s = cl.getOptionValue(OPTION_INTERVAL);
		if (s == null)
			return null;

		try {
			return Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return null;
		}
	}

	public URL parseUrl(CommandLine cl) {
		String[] s = cl.getArgs();
		if (s.length != 1) {
			return null;
		}

		try {
			return new URL(s[0]);
		} catch (MalformedURLException e) {
			return null;
		}
	}
}
