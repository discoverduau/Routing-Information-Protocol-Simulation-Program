package javabin;

import java.util.ArrayList;

import main.mainutil;

public class routerThread extends Thread{
	private router r;
	private ArrayList<router> rs;
	private ArrayList<net> ns;
	private ArrayList<routerThread> rts;
	
	public router getR() {
		return r;
	}
	public void setR(router r) {
		this.r = r;
	}
	public ArrayList<router> getRs() {
		return rs;
	}
	public void setRs(ArrayList<router> rs) {
		this.rs = rs;
	}
	public ArrayList<net> getNs() {
		return ns;
	}
	public void setNs(ArrayList<net> ns) {
		this.ns = ns;
	}
	public void setRts(ArrayList<routerThread> rts) {
		this.rts = rts;
	}
	public ArrayList<routerThread> getRts(){
		return rts;
	}
	/*public routerThread(router r,ArrayList<router> rs,ArrayList<net> ns) {
		this.r = r;
		this.rs = rs;
		this.ns = ns;
	}*/
	public routerThread GetRouterThreadById(String RouterId) {
		routerThread result = null;
		for(int i = 0; i < rts.size();i++) {
			if(rts.get(i).r.getRouterId().equals(RouterId)) {
				result = rts.get(i);
			}
		}
		return result;
	}
	public void run() {
		//synchronized() {
			//System.out.println("sb ");
			mainutil mn = new mainutil();
			boolean IsChange = mn.ChangeRouterTable(r, rs);
			for(String RouterId:r.getNeighbourRouter()) {
				routerThread nextThread = GetRouterThreadById(RouterId);
				if(IsChange) {//||r.getRouterTable().getList().size()<ns.size()
					nextThread.run();
					//发送rip报文
				}
			}
		//}
	
		
	}
	/*public void Connect() {
		//System.out.println("sb ");
		mainutil mn = new mainutil();
		boolean IsChange = mn.ChangeRouterTable(r, rs);
		for(String RouterId:r.getNeighbourRouter()) {
			routerThread nextThread = GetRouterThreadById(RouterId);
			if(IsChange) {
				nextThread.Connect();
				//发送rip报文
			}
		}
	}*/
	
}
