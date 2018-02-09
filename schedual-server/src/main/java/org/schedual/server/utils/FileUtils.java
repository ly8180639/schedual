package org.schedual.server.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileUtils {
	public static void writeObject(Map<Object,Object> datas){
        try {
            
            FileOutputStream outStream = new FileOutputStream("E:/1.txt");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                    outStream);
            objectOutputStream.writeObject(datas);
            outStream.close();
            System.out.println("successful");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void readObject(){
        FileInputStream freader;
        try {
            freader = new FileInputStream("E:/1.txt");
            ObjectInputStream objectInputStream = new ObjectInputStream(freader);
            HashMap<String,Object> map = new HashMap<String,Object>();
            
            List<Map<String, Object>> list=(List<Map<String, Object>>)objectInputStream.readObject();
            for (Map<String, Object> map2 : list) {
                System.out.println(map2.toString());
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
}
