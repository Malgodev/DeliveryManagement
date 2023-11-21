/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import java.io.ObjectInputStream;
import java.io.FileInputStream;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author binhp
 */
public class IOFile {
     
    public static <T> List<T> Read(String fname){
        List<T> list = new ArrayList<>();
        try{
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fname));
            list = (List<T>) ois.readObject();
            ois.close();
        }catch(IOException e){
            System.out.println(e);
        }catch(ClassNotFoundException e){
            System.out.println(e);
        }
        return list;
    }

    public static <T> void Write(String fname, String str){
        try{
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fname, true));
            oos.writeObject(str);
            oos.close();
        }catch(IOException e){
            System.out.println(e);
        }
    }
}
