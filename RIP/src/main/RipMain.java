package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import javabin.net;
import javabin.router;
import javabin.routerThread;
import javabin.routertable;

public class RipMain {

	public static void main(String[] args) {
		mainutil mn = new mainutil();
		// TODO Auto-generated method stub
		ArrayList<router> Routers = new ArrayList<router>();
		ArrayList<net> Nets = new ArrayList<net>();
		int VertexNum = 0;												//网络加路由器的总个数
		Scanner sc = new Scanner(System.in);
		VertexNum = sc.nextInt();	
		ArrayList<String> NodeIds = new ArrayList<String>();			//网络或路由器的名字
		for(int i = 0; i < VertexNum; i++) {
			String NodeId = sc.next();
			NodeIds.add(NodeId);
		}
		int[][] Matrix = new int[VertexNum][VertexNum];					
		//构建网络拓扑邻接矩阵，0为不连，1为路由连网络，2为网络连路由
		for(int i = 0; i < VertexNum;i++) {									
			for(int j = 0; j < VertexNum;j++) {
				Matrix[i][j] = sc.nextInt();
			}
		}
		for(int i = 0; i < VertexNum; i++) {							//将网络拓扑实例化
			int NodeKind = 0;											//记录节点为何种类型，1为路由，2为网络
			ArrayList<String> Neighbour = new ArrayList<String>();		
			//若当前节点为路由，则直接相邻网络，若当前节点为网络，则直接相邻路由
			ArrayList<String> SecondNeighbour = new ArrayList<String>();//隔一个相邻的节点，存储相邻路由
			for(int j = 0; j < VertexNum; j++) {
				if(Matrix[i][j] == 1) {//当前节点为路由
					NodeKind = 1;
					for(int k = 0; k < VertexNum; k++) {
						if(Matrix[j][k] == 2 && k != i)
						SecondNeighbour.add(NodeIds.get(k));//得到相邻路由			
					}
					Neighbour.add(NodeIds.get(j));//得到相邻网络
				}
				
				else if(Matrix[i][j] == 2) {//当前节点为网络
					NodeKind = 2;
					for(int k = 0; k < VertexNum; k++) {
						if(Matrix[j][k] == 2 && k != i)
						SecondNeighbour.add(NodeIds.get(k));//得到相邻网络
					}//
					Neighbour.add(NodeIds.get(j));//得到相邻路由
				}
		}
			//实例化路由器
			if(NodeKind == 1) {
				HashMap<String,Integer> List = new HashMap<String,Integer>();
				Integer one = new Integer(1);
				for(int p = 0; p < Neighbour.size(); p++)
				List.put(Neighbour.get(p), one);
				routertable RouterTable = new routertable(NodeIds.get(i),List);
				router Router = new router(NodeIds.get(i),SecondNeighbour,Neighbour,RouterTable);
				Routers.add(Router);
			}
			//实例化网络
			else if(NodeKind == 2) {
				net Net = new net(NodeIds.get(i),Neighbour);
				Nets.add(Net);
			}
			/*for(String u : Neighbour) {
				System.out.println(u);
				
			}
			System.out.println();*/
	}
		mn.PrintAllRouterTable(Routers);//打印初始状态路由表
		
		ArrayList<routerThread> routerThreads = new ArrayList<routerThread>();
		for(router r:Routers) {
			routerThread rt = new routerThread();
			rt.setR(r);
			rt.setRs(Routers);
			rt.setNs(Nets);
			routerThreads.add(rt);
		}
		for(int i = 0;i < routerThreads.size();i++) {
			routerThreads.get(i).setRts(routerThreads);
		}
	//////
		routerThreads.get(0).run();//启动rip
		
		mn.PrintAllRouterTable(Routers);//打印完成后的路由表
}
}
