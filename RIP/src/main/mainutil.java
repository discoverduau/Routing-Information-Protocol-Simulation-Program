package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javabin.net;
import javabin.router;

public class mainutil {
	public router GetRouterById(String RouterId,ArrayList<router> Routers) {
		router Result = null;
		for(int i = 0;i < Routers.size();i++) {
			if(Routers.get(i).getRouterId().equals(RouterId)) {
				Result = Routers.get(i);
				break;
			}
		}
		return Result;
	}
	
	public net GetNetById(String NetId,ArrayList<net> Nets) {
		net Result = null;
		for(int i = 0;i < Nets.size();i++) {
			if(Nets.get(i).getNetId().equals(NetId)) {
				Result = Nets.get(i);
				break;
			}
		}
		return Result;
	}
	
	
	public boolean IsTotallyDifferent(HashMap<String,Integer> ListA,HashMap<String,Integer> ListB) {
		Set<String> A_Keys = ListA.keySet();
		Set<String> B_Keys = ListB.keySet();
		for(String A_Key:A_Keys) {
			for(String B_Key:B_Keys) {
				if(A_Key.equals(B_Key)) 
					return false;
			}
		}
		return true;
	}
	
	public boolean ChangeRouterTable(router Router,ArrayList<router> Routers) {
		boolean IsChange = false;
		for(String N:Router.getNeighbourRouter()) {
			router R = GetRouterById(N,Routers);
			HashMap<String,Integer> NewList = Router.getRouterTable().getList();
			Set<String> Router_Keys = Router.getRouterTable().getList().keySet();//当前路由器的路由表key值
			Set<String> R_Keys = R.getRouterTable().getList().keySet();//相邻路由器的路由表key值
			/*if(IsTotallyDifferent(Router.getRouterTable().getList(),R.getRouterTable().getList())) {
				HashMap<String,Integer> Combine = new HashMap<String,Integer>();
				Combine.putAll(Router.getRouterTable().getList());
				Combine.putAll(R.getRouterTable().getList());
				Router.getRouterTable().setList(Combine);
				IsChange = true;
			}*/
			//else {
				for(String R_Key:R_Keys) {
					int times = 0;
					for(String Router_Key:Router_Keys) {
						if(R_Key.equals(Router_Key)) {
							if(R.getRouterTable().getList().get(R_Key).intValue() + 1 < Router.getRouterTable().getList().get(Router_Key).intValue()) {
								Integer NewValue = new Integer(1 + R.getRouterTable().getList().get(R_Key).intValue());
								IsChange = true;
								NewList.put(Router_Key, NewValue);
							}
						}
						else {
							times++;
						}
						
					}
					if(times == Router_Keys.size()) {
						Integer NewValue = new Integer(R.getRouterTable().getList().get(R_Key).intValue() + 1);
						IsChange = true;
						NewList.put(R_Key, NewValue);
					}
				}
				Router.getRouterTable().setList(NewList);
			//}
			
			
		}
		return IsChange;
	}
	public void PrintAllRouterTable(ArrayList<router> Routers) {
		for(router Router:Routers) {
			System.out.println(Router.getRouterId());
			System.out.println(Router.getRouterTable().getList().toString());
			System.out.println();
		}
		
	}
}
