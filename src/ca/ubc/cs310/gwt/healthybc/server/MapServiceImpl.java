package ca.ubc.cs310.gwt.healthybc.server;

import ca.ubc.cs310.gwt.healthybc.client.MapInfo;
import ca.ubc.cs310.gwt.healthybc.client.MapService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class MapServiceImpl extends RemoteServiceServlet implements MapService {

	/**
	 * serialization
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public MapInfo[] getMapList() {
		// TODO Auto-generated method stub
		return null;
	}
}
