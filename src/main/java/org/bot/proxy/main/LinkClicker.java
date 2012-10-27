package org.bot.proxy.main;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LinkClicker {
	public static final Logger LOG = LoggerFactory.getLogger(LinkClicker.class);

	/**
	 * Opens a given page and clicks on the link specified by a regular
	 * expression. Does this for all proxies in the given proxies list.
	 * 
	 * Command line arguments: -e <linkregexp> -p <proxyfile> -i
	 * <intervalmillis> <page>
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		LOG.info("main");

		Runner runner = new LinkClickerRunner();
		CommandLineParser parser = new GnuParser();
		Options options = runner.getCommandLineOptions();

		try {
			CommandLine cl = parser.parse(runner.getCommandLineOptions(), args);
			if (runner.parseCommandLine(cl)) {
				runner.run();
				return;
			}
		} catch (ParseException e) {

		}

		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("java -jar bit.jar", options);
	}

}
