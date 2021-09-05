package javabin;
import java.util.HashMap;
public class routertable {
	private String RouterId;
	private HashMap<String,Integer> List;
	public routertable(String RouterId, HashMap<String,Integer> List) {
		this.RouterId = RouterId;
		this.List = List;
	}
	public String getRouterId() {
		return RouterId;
	}
	public void setRouterId(String routerId) {
		RouterId = routerId;
	}
	public HashMap<String, Integer> getList() {
		return List;
	}
	public void setList(HashMap<String, Integer> list) {
		List = list;
	}
}
