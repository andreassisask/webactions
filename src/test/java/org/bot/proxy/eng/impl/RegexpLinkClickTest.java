package org.bot.proxy.eng.impl;

import static org.junit.Assert.assertTrue;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;

public class RegexpLinkClickTest {
	private static final String PATTERN = ".*index\\.php.*";
	private static final String LINK1 = "http://www.google.com/index.php?success=true&fail=false";
	private static final String LINK2 = "./index.php?sid=229e4930d0e0fdbfeb11e00940200082";

	private Pattern pattern;
	@SuppressWarnings("unused")
	private RegexpLinkClick linkClick;

	@SuppressWarnings("unused")
	private HtmlElement start;
	private HtmlAnchor anchor;
	private List<HtmlElement> startChildren;

	private URL url;

	@Before
	public void setUp() throws Exception {
		pattern = Pattern.compile(PATTERN);

		startChildren = new ArrayList<HtmlElement>();
		start = Mockito.mock(HtmlElement.class);
		anchor = Mockito.mock(HtmlAnchor.class);
		startChildren.add(anchor);

		// Mockito.when(start.getChildElements()).thenReturn(startChildren);
		// Mockito.when(anchor.getHrefAttribute()).thenReturn("http://www.google.com/index.php?success=true&fail=false");

		// url = Mockito.mock(URL.class);
		linkClick = new RegexpLinkClick(url, PATTERN);
	}

	@Test
	public void testRegexpLinkClick() {
	}

	@Test
	public void testPerformAction() {
	}

	@Test
	public void testClickLinks() {
	}

	@Test
	public void testFindLinks() {
	}

	@Test
	public void testFindLinksRecursive() {
		// List<HtmlAnchor> links = new ArrayList<HtmlAnchor>();
		// linkClick.findLinksRecursive(start, pattern, links);
		// assertTrue(links.contains(anchor));

		Matcher m1 = pattern.matcher(LINK1);
		assertTrue(m1.find());

		Matcher m2 = pattern.matcher(LINK2);
		assertTrue(m2.find());
	}
}
