package ca.ubc.cs310.gwt.healthybc.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import ca.ubc.cs310.gwt.healthybc.client.ClinicTabInfo;
import ca.ubc.cs310.gwt.healthybc.client.MapInfo;
import ca.ubc.cs310.gwt.healthybc.client.TabFetcher;
import ca.ubc.cs310.gwt.healthybc.client.TableInfo;


@SuppressWarnings("serial")
public class TabFetcherImpl extends RemoteServiceServlet implements TabFetcher {

	public TabFetcherImpl(){
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

	public ArrayList<Double> geoCode(String address) {
		GeoCoder geoCoder = new GeoCoder();
		try {
			GeocodeResponse response = geoCoder.getLocation(address);

			ArrayList<Double> returnVal = new ArrayList<Double>();
			returnVal.add(response.getResults().get(0).getGeometry().getLocation().getLat());
			returnVal.add(response.getResults().get(0).getGeometry().getLocation().getLng());
			return returnVal;

		} catch (Exception e) {
			e.printStackTrace();
		}	return null;
	}


	//Geocoder code
	// --------------------------------------------------------------


	private static class GeoCoder {
		private Gson gson = new Gson();

		private volatile long lastRequest = 0L;

		public GeocodeResponse getLocation(String... addressElements) throws JsonSyntaxException, JsonIOException, MalformedURLException,
		IOException {
			StringBuilder sb = new StringBuilder();
			for (String string : addressElements) {
				if (sb.length() > 0) {
					sb.append('+');
				}
				sb.append(URLEncoder.encode(string.replace(' ', '+'), "UTF-8"));
			}
			String url = "http://maps.googleapis.com/maps/api/geocode/json?sensor=false&address=" + sb.toString();
			// Google limits this web service to 2500/day and 10 requests/s
			synchronized (this) {
				try {
					long elapsed = System.currentTimeMillis() - lastRequest;
					if (elapsed < 100) {
						try {
							Thread.sleep(100 - elapsed);
						} catch (InterruptedException e) {
						}
					}
					return gson.fromJson(new BufferedReader(new InputStreamReader(new URL(url).openStream())), GeocodeResponse.class);
				} finally {
					lastRequest = System.currentTimeMillis();
				}
			}
		}
	}


	private static class Location {
		private double lat;
		private double lng;

		public double getLat() {
			return lat;
		}

		public double getLng() {
			return lng;
		}
	}

	public static class GeocodeResponse {

		public enum Status {
			OK, ZERO_RESULTS, OVER_QUERY_LIMIT, REQUEST_DENIED, INVALID_REQUEST;
		}

		public static class Result {

			public static enum Type {
				street_address,
				route,
				intersection,
				political,
				country,
				administrative_area_level_1,
				administrative_area_level_2,
				administrative_area_level_3,
				colloquial_area,
				locality,
				sublocality,
				neighborhood,
				premise,
				subpremise,
				postal_code,
				natural_feature,
				airport,
				park,
				point_of_interest,
				post_box,
				street_number,
				floor,
				room;
			}

			public static class AddressComponent {

				private String long_name;
				private String short_name;
				private Type[] types;

				public String getLong_name() {
					return long_name;
				}

				public void setLong_name(String long_name) {
					this.long_name = long_name;
				}

				public String getShort_name() {
					return short_name;
				}

				public void setShort_name(String short_name) {
					this.short_name = short_name;
				}

				public Type[] getTypes() {
					return types;
				}

				public void setTypes(Type[] types) {
					this.types = types;
				}
			}

			private String formatted_address;
			private List<AddressComponent> address_components;
			private Geometry geometry;
			private Type[] types;

			public Type[] getTypes() {
				return types;
			}

			public void setTypes(Type[] types) {
				this.types = types;
			}

			public String getFormatted_address() {
				return formatted_address;
			}

			public void setFormatted_address(String formatted_address) {
				this.formatted_address = formatted_address;
			}

			public List<AddressComponent> getAddress_components() {
				return address_components;
			}

			public void setAddress_components(List<AddressComponent> address_components) {
				this.address_components = address_components;
			}

			public Geometry getGeometry() {
				return geometry;
			}

			public void setGeometry(Geometry geometry) {
				this.geometry = geometry;
			}

		}

		public static class Geometry {
			public static enum LocationType {
				ROOFTOP, RANGE_INTERPOLATED, GEOMETRIC_CENTER, APPROXIMATE;
			}

			public static class ViewPort {
				private Location northeast;
				private Location southwest;

				public Location getNortheast() {
					return northeast;
				}

				public void setNortheast(Location northeast) {
					this.northeast = northeast;
				}

				public Location getSouthwest() {
					return southwest;
				}

				public void setSouthwest(Location southwest) {
					this.southwest = southwest;
				}
			}

			private Location location;
			private LocationType location_type;
			private ViewPort viewport;

			public Location getLocation() {
				return location;
			}

			public void setLocation(Location location) {
				this.location = location;
			}

			public LocationType getLocation_type() {
				return location_type;
			}

			public void setLocation_type(LocationType location_type) {
				this.location_type = location_type;
			}

			public ViewPort getViewport() {
				return viewport;
			}

			public void setViewport(ViewPort viewport) {
				this.viewport = viewport;
			}

		}

		private Status status;
		private List<Result> results;

		public Status getStatus() {
			return status;
		}

		public void setStatus(Status status) {
			this.status = status;
		}

		public List<Result> getResults() {
			return results;
		}

		public void setResults(List<Result> results) {
			this.results = results;
		}

	}
}