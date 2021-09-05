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
		int VertexNum = 0;												//�����·�������ܸ���
		Scanner sc = new Scanner(System.in);
		VertexNum = sc.nextInt();	
		ArrayList<String> NodeIds = new ArrayList<String>();			//�����·����������
		for(int i = 0; i < VertexNum; i++) {
			String NodeId = sc.next();
			NodeIds.add(NodeId);
		}
		int[][] Matrix = new int[VertexNum][VertexNum];					
		//�������������ڽӾ���0Ϊ������1Ϊ·�������磬2Ϊ������·��
		for(int i = 0; i < VertexNum;i++) {									
			for(int j = 0; j < VertexNum;j++) {
				Matrix[i][j] = sc.nextInt();
			}
		}
		for(int i = 0; i < VertexNum; i++) {							//����������ʵ����
			int NodeKind = 0;											//��¼�ڵ�Ϊ�������ͣ�1Ϊ·�ɣ�2Ϊ����
			ArrayList<String> Neighbour = new ArrayList<String>();		
			//����ǰ�ڵ�Ϊ·�ɣ���ֱ���������磬����ǰ�ڵ�Ϊ���磬��ֱ������·��
			ArrayList<String> SecondNeighbour = new ArrayList<String>();//��һ�����ڵĽڵ㣬�洢����·��
			for(int j = 0; j < VertexNum; j++) {
				if(Matrix[i][j] == 1) {//��ǰ�ڵ�Ϊ·��
					NodeKind = 1;
					for(int k = 0; k < VertexNum; k++) {
						if(Matrix[j][k] == 2 && k != i)
						SecondNeighbour.add(NodeIds.get(k));//�õ�����·��			
					}
					Neighbour.add(NodeIds.get(j));//�õ���������
				}
				
				else if(Matrix[i][j] == 2) {//��ǰ�ڵ�Ϊ����
					NodeKind = 2;
					for(int k = 0; k < VertexNum; k++) {
						if(Matrix[j][k] == 2 && k != i)
						SecondNeighbour.add(NodeIds.get(k));//�õ���������
					}//
					Neighbour.add(NodeIds.get(j));//�õ�����·��
				}
		}
			//ʵ����·����
			if(NodeKind == 1) {
				HashMap<String,Integer> List = new HashMap<String,Integer>();
				Integer one = new Integer(1);
				for(int p = 0; p < Neighbour.size(); p++)
				List.put(Neighbour.get(p), one);
				routertable RouterTable = new routertable(NodeIds.get(i),List);
				router Router = new router(NodeIds.get(i),SecondNeighbour,Neighbour,RouterTable);
				Routers.add(Router);
			}
			//ʵ��������
			else if(NodeKind == 2) {
				net Net = new net(NodeIds.get(i),Neighbour);
				Nets.add(Net);
			}
			/*for(String u : Neighbour) {
				System.out.println(u);
				
			}
			System.out.println();*/
	}
		mn.PrintAllRouterTable(Routers);//��ӡ��ʼ״̬·�ɱ�
		
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
		routerThreads.get(0).run();//����rip
		
		mn.PrintAllRouterTable(Routers);//��ӡ��ɺ��·�ɱ�
}
}
