package org.bot.proxy.main;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;

public interface Runner {

	public Options getCommandLineOptions();

	public void run();

	public boolean parseCommandLine(CommandLine cl);
}
