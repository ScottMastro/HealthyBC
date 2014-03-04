package ca.ubc.cs310.gwt.healthybc.server;

import java.util.ArrayList;
import java.util.List;

import ca.ubc.cs310.gwt.healthybc.client.MapInfo;
import ca.ubc.cs310.gwt.healthybc.client.MapService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class MapServiceImpl extends RemoteServiceServlet implements MapService {

	/**
	 * serialization
	 */
	private static final long serialVersionUID = 1L;
	
	private List<MapInfo> mapList;
	
	public MapServiceImpl() {
		mapList = new ArrayList<MapInfo>();
	}
	
	/**
	 * @return an array of MapInfo
	 */
	@Override
	public MapInfo[] getMapList() {
		return (MapInfo[]) mapList.toArray();
	}
}
