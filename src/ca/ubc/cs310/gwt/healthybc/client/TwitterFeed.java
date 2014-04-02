package ca.ubc.cs310.gwt.healthybc.client;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.ScriptElement;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class TwitterFeed {
	private static TwitterFeed singleton = null;
	
	private TwitterFeed() {
	}
	
	public static TwitterFeed getInstance() {
		if (singleton == null) {
			singleton = new TwitterFeed();
		}
		return singleton;
	}
	
	public Widget createTwitterFeed() {
		VerticalPanel container = new VerticalPanel();
		container.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		container.add(new HTML(
				"<a class='twitter-timeline' data-dnt='true' href='https://twitter.com/HealthyBCUBC'"
				+ " data-widget-id='451163200428335104'>Tweets by @HealthyBCUBC</a><br />"
				+ "<div id='twitter-timelined'></div>"));
		Document doc = Document.get();
		ScriptElement script = doc.createScriptElement();
		script.setSrc("http://platform.twitter.com/widgets.js");
		script.setType("text/javascript");
		script.setLang("javascript");
		doc.getBody().appendChild(script);
		
		return container;
	}
}