package javabin;

import java.util.ArrayList;

import main.mainutil;

public class router{
	private String RouterId;
	private ArrayList<String> NeighbourRouter;
	private ArrayList<String> NeighbourNet;
	private routertable RouterTable;
	public router(String RouterId,ArrayList<String> NeighbourRouter,ArrayList<String> NeighbourNet,routertable RouterTable) {
		this.RouterId = RouterId;
		this.NeighbourRouter = NeighbourRouter;
		this.NeighbourNet = NeighbourNet;
		this.RouterTable = RouterTable;
	}
	public String getRouterId() {
		return RouterId;
	}
	public void setRouterId(String routerId) {
		RouterId = routerId;
	}
	public ArrayList<String> getNeighbourRouter() {
		return NeighbourRouter;
	}
	public void setNeighbourRouter(ArrayList<String> neighbourRouter) {
		NeighbourRouter = neighbourRouter;
	}
	public ArrayList<String> getNeighbourNet() {
		return NeighbourNet;
	}
	public void setNeighbourNet(ArrayList<String> neighbourNet) {
		NeighbourNet = neighbourNet;
	}
	public routertable getRouterTable() {
		return RouterTable;
	}
	public void setRouterTable(routertable routerTable) {
		RouterTable = routerTable;
	}

	
}
