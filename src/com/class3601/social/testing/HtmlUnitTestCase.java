package com.class3601.social.testing;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

public class HtmlUnitTestCase  {

	@Test
	public void homePage() throws Exception {
		final WebClient webClient = new WebClient();
		final HtmlPage page = webClient.getPage("http://htmlunit.sourceforge.net");
		assertEquals("HtmlUnit - Welcome to HtmlUnit", page.getTitleText());

		final String pageAsXml = page.asXml();
		assertTrue(pageAsXml.contains("<body class=\"composite\">"));

		final String pageAsText = page.asText();
		assertTrue(pageAsText.contains("Support for the HTTP and HTTPS protocols"));

		webClient.closeAllWindows();
	}
	
	@Test
	public void homePage_Firefox() throws Exception {
	    final WebClient webClient = new WebClient(BrowserVersion.FIREFOX_10);
	    final HtmlPage page = webClient.getPage("http://htmlunit.sourceforge.net");
	    assertEquals("HtmlUnit - Welcome to HtmlUnit", page.getTitleText());

	    webClient.closeAllWindows();
	}

	@SuppressWarnings("unused")
	@Test
	public void getElements() throws Exception {
	    final WebClient webClient = new WebClient();
	    final HtmlPage page = webClient.getPage("http://kai.deliriousapps.com");
	    final HtmlDivision div = page.getHtmlElementById("content");
	    //final HtmlAnchor anchor = page.getAnchorByName("anchor_name");

	    webClient.closeAllWindows();
	}
	
	@Test
	public void submittingForm() throws Exception {
	    final WebClient webClient = new WebClient();

	    // Get the first page
	    final HtmlPage page1 = webClient.getPage("file:///Users/Andrew/Desktop/test.html");

	    // Get the form that we are dealing with and within that form, 
	    // find the submit button and the field that we want to change.
	    final HtmlForm form = page1.getFormByName("searchformname");

	    final HtmlSubmitInput button = form.getInputByName("searchbuttonname");
	    final HtmlTextInput textField = form.getInputByName("searchfieldname");

	    // Change the value of the text field
	    textField.setValueAttribute("hello");

	    // Now submit the form by clicking the button and get back the second page.
	    @SuppressWarnings("unused")
		final HtmlPage page2 = button.click();

	    webClient.closeAllWindows();
	}
}
