package org.bot.proxy.eng;

import com.gargoylesoftware.htmlunit.WebClient;

public interface WebAction {
	public void performAction(WebClient webClient);
}
