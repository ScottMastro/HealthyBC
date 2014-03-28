package ca.ubc.cs310.gwt.healthybc.server;

import java.util.ArrayList;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import ca.ubc.cs310.gwt.healthybc.client.ClinicTabInfo;
import ca.ubc.cs310.gwt.healthybc.client.MapInfo;
import ca.ubc.cs310.gwt.healthybc.client.TabHandler;
import ca.ubc.cs310.gwt.healthybc.client.TableInfo;

@SuppressWarnings("serial")
public class TabHandlerImpl extends RemoteServiceServlet implements TabHandler {

	public TabHandlerImpl(){
	}


	public ArrayList<ClinicTabInfo> clinicTabInfo(TableInfo ti){
		return ClinicManager.getInstance().getClinicTabInfo(ti);
	}


	public ArrayList<ClinicTabInfo> clinicTabInfo(MapInfo mi) {
		return ClinicManager.getInstance().getClinicTabInfo(mi);

	}

	public ArrayList<Boolean> sendEmail(String text, String emailAddress, String title) {
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);
		ArrayList<Boolean> result = new ArrayList<Boolean>();

		try {
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress("theblanksl8@gmail.com", "HealthyBC"));
			msg.addRecipient(Message.RecipientType.TO,
					//DO NOT CHANGE EMAIL TO THE CLINIC'S EMAIL UNLESS YOU WANT THE PEOPLE
					//WORKING AT THE CLINIC TO BE CONFUSED
					new InternetAddress("theblanksl8@gmail.com", "BlankSlate"));
			msg.setSubject(title);
			msg.setText(text);
			Transport.send(msg);

		} catch (Exception e) {
			result.add(false);
			return result;
		}		

		result.add(true);
		return result;
	}

}


