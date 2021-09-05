package javabin;

import java.util.ArrayList;

public class net {
	private String NetId;
	private ArrayList<String> NeighbourRouter;
	public net(String NetId, ArrayList<String> NeighbourRouter) {
		this.NetId = NetId;
		this.NeighbourRouter = NeighbourRouter;
	}
	public String getNetId() {
		return NetId;
	}
	public void setNetId(String netId) {
		NetId = netId;
	}
	public ArrayList<String> getNeighbourRouter() {
		return NeighbourRouter;
	}
	public void setNeighbourRouter(ArrayList<String> neighbourRouter) {
		NeighbourRouter = neighbourRouter;
	}
	public int GetNeighbourRouterLength() {
		return NeighbourRouter.size();
	}
	
}
