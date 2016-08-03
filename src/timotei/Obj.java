/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timotei;

import java.util.ArrayList;

/**
 *
 * @author root
 */
public abstract class Obj {
    protected String name;
    protected int size;
    protected int paino;
    protected int rikkoutuva;
    protected int packageID;

    protected String getName() {
        return name;
    }

    protected int getTilavuus() {
        return size;
    }

    protected int getPaino() {
        return paino;
    }

    protected int getRikkoutuva() {
        return rikkoutuva;
    }

    public int getPackageID() {
        return packageID;
    }
    
    
    
    @Override
    public String toString(){
        return name;
    }
    
    
}

class Obj1 extends Obj{
    
    public Obj1(){
        name = "Raskas vaasi";
        size = 225;
        paino = 100;
        rikkoutuva = 1;
    }
    
}
class Obj2 extends Obj{
    
    public Obj2(){
        name = "Tietokoneen keskusyksikkö";
        size = 50;
        paino = 5;
        rikkoutuva = 1;
    }
    
}
class Obj3 extends Obj{
    
    public Obj3(){
        name = "Lompakko";
        size = 1;
        paino = 1;
        rikkoutuva = 0;
    }
    
}
class Obj4 extends Obj{
    
    public Obj4(){
        name = "Pikachu";
        size = 20;
        paino = 15;
        rikkoutuva = 0;
    }
    
}

class ObjX extends Obj{

    public void setNimi(String nimi) {
        this.name = nimi;
    }

    public void setTilavuus(int tilavuus) {
        this.size = tilavuus;
    }

    public void setPaino(int paino) {
        this.paino = paino;
    }

    public void setRikkoutuva(int rikkoutuva) {
        this.rikkoutuva = rikkoutuva;
    }

    public void setEsineID(int esineID) {
        this.packageID = esineID;
    }
    
    public int getPackageID(){
        return packageID;
    }
    
}

class Objects extends Obj{
    
    //Lisätään olemassaolevat oletusesineet listaan joita voidaan sitten lisätä tietokantaan
    public void createObjList(ArrayList list){
        Obj o1 = new Obj1();
        list.add(o1);
        
        Obj o2 = new Obj2();
        list.add(o2);
        
        Obj o3 = new Obj3();
        list.add(o3);
        
        Obj o4 = new Obj4();
        list.add(o4);
        
    }
}
