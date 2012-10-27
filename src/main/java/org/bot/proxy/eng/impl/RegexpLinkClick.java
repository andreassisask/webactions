package org.bot.proxy.eng.impl;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bot.proxy.eng.WebAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.FrameWindow;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class RegexpLinkClick implements WebAction {
	private static final Logger LOG = LoggerFactory.getLogger(RegexpLinkClick.class);

	private URL pageUrl;
	private Pattern pattern;

	public RegexpLinkClick(URL pageUrl, String linkRegexp) {
		this.pageUrl = pageUrl;
		this.pattern = Pattern.compile(linkRegexp);

		LOG.info("pageUrl = " + pageUrl);
		LOG.info("linkRegexp = " + linkRegexp);
	}

	public void performAction(WebClient webClient) {
		try {
			LOG.info("Opening URL " + pageUrl);
			Page p = webClient.getPage(pageUrl);

			if (p instanceof HtmlPage) {
				HtmlPage hp = (HtmlPage) p;
				List<HtmlAnchor> links = findLinks(hp, pattern);
				clickLinks(links);
			} else {
				LOG.warn("Not an HTML page " + pageUrl);
			}
			LOG.info("Done with page " + pageUrl);
		} catch (Throwable t) {
			LOG.error("Failed to perform action", t);
		}
	}

	public void clickLinks(List<HtmlAnchor> links) throws IOException {
		for (HtmlAnchor a : links) {
			LOG.info("Clicking on link " + a.getHrefAttribute());
			Page result = a.click();
			LOG.info("Click lead to page " + result.getUrl());
		}
	}

	public List<HtmlAnchor> findLinks(HtmlPage htmlPage, Pattern pattern) {
		// Collect all links
		List<HtmlAnchor> links = new ArrayList<HtmlAnchor>();
		findLinksRecursive(htmlPage, pattern, links);
		
		// Remove the ones that do not match
		for (Iterator<HtmlAnchor> i = links.iterator(); i.hasNext(); ){
			if (!matches(pattern, i.next())){
				i.remove();
			}
		}

		return links;
	}
	
	public void findLinksRecursive(HtmlPage htmlPage, Pattern pattern, List<HtmlAnchor> links) {
		LOG.info("Adding links of page " + htmlPage.getUrl());
		links.addAll(htmlPage.getAnchors());
		
		List<FrameWindow> frames = htmlPage.getFrames();
		for (FrameWindow fw : frames){
			Page p = fw.getEnclosedPage();
			if (p instanceof HtmlPage){
				findLinksRecursive((HtmlPage) p, pattern, links);
			}
		}
	}
	
	public boolean matches(Pattern pattern, HtmlAnchor htmlAnchor){
		Matcher m = pattern.matcher(htmlAnchor.getHrefAttribute());
		return m.matches();
	}
	
//	public void findLinksRecursive(HtmlElement start, Pattern pattern, List<HtmlAnchor> links) {
//		LOG.debug("Checking element " + start);
//		if (start instanceof HtmlAnchor) {
//			HtmlAnchor a = (HtmlAnchor) start;
//			Matcher m = pattern.matcher(a.getHrefAttribute());
//			if (m.matches()) {
//				LOG.info("Link matches " + a.getHrefAttribute());
//				links.add(a);
//			} else {
//				LOG.info("Link does not match " + a.getHrefAttribute());
//			}
//		}
//		for (HtmlElement child : start.getChildElements()) {
//			findLinksRecursive(child, pattern, links);
//		}
//	}
}
