package fdata;

import java.util.HashSet;
import java.util.Set;

public class MyData {
	
	public MyData(int key,int firstnumber) {
		this.key = key;
		interactor.add(firstnumber);
	}
	
	public MyData(){}
	
	@Override
	public String toString(){
		StringBuffer temp = new StringBuffer("[" + this.key + ",0,[" );
		for(int a : interactor) {
			temp.append("[" + a + ",1],");
		}
		temp.deleteCharAt(temp.length()-1);
		temp.append("]]");
		return temp.toString();
	}
	
	public int key;
	public Set<Integer> interactor = new HashSet<Integer>();
}
