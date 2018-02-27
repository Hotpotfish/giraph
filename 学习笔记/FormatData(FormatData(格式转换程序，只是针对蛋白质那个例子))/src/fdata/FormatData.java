package fdata;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;





public class FormatData {

	private static List<MyData> data = new ArrayList<MyData>();
	
	public static void readFileToFormat(String fileName) {
		File file = new File(fileName);
		BufferedReader bf = null;
		try { 
			bf = new BufferedReader( new FileReader(file) );
			String tempString = null;
			//略去第一行
			bf.readLine();
			
			while( (tempString = bf.readLine() ) != null ) {
				String[] temp = tempString.split(" |\t");
				
				//截取关键信息
				int key = Integer.parseInt( temp[0].substring(4).substring(0, temp[0].length()-5) );
				int inter =	Integer.parseInt( temp[1].substring(4).substring(0, temp[1].length()-5) );
				//System.out.println(temp[0].substring(4).substring(0, temp[0].length()-5) );
				//System.out.println(temp[1].substring(4).substring(0, temp[1].length()-5));
				
				//flag 0表示两个数都未找到，1表示找到KEY，2表示找到INTER，3表示两个都找到了
				int flag = 0;
				for(MyData tempData : data) {
					if(tempData.key == key) {
						if (flag == 0) {
							flag = 1;
							tempData.interactor.add(inter);
						}
						else if(flag == 2){
							flag = 3;
							tempData.interactor.add(inter);
							break;
						}
					}
					if(tempData.key == inter) {
						if (flag == 0) {
							flag = 2;
							tempData.interactor.add(key);
						}
						else if(flag == 1){
							flag = 3;
							tempData.interactor.add(key);
							break;
						}
					}
				}
				if ( flag == 0) {
					data.add(new MyData(key,inter));
					data.add(new MyData(inter,key));
				}
				else if ( flag == 1 ) {
					data.add(new MyData(inter,key));
				}
				else if ( flag == 2 ) {
					data.add(new MyData(key,inter));
				}
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			if (bf != null) {
				try {
					bf.close();
				}
				catch (IOException e1){}
			}
		}
	}
	
	public static void writeFileToFormat(String fileName) {
		File file = new File(fileName);
		BufferedWriter br = null;
		try {
			br = new BufferedWriter(new FileWriter(file) );
			
			for(MyData tempData : data) {
				br.write(tempData.toString());
				br.newLine();
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			if (br != null) {
				try {
					br.close();
				}
				catch (IOException e1){}
			}
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		readFileToFormat("TestData.txt");
		writeFileToFormat("FormatData.txt");
	}

}
