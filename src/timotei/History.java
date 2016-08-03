/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timotei;

/**
 *
 * @author root
 */
public class History {
    private String start;
    private String end;
    private String objName;
    private int distance;
    private int packageID;
    private int isBroken;
    private int timoteiID;
    private String timestamp;
    private int historyID;
    
    @Override
    public String toString(){
        return historyID+" "+objName;
    }

    public void setHistoryID(int historyID) {
        this.historyID = historyID;
    }

    public int getHistoryID() {
        return historyID;
    }
    
    public String getObjName() {
        return objName;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setObjName(String objName) {
        this.objName = objName;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
    

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public int getDistance() {
        return distance;
    }

    public int getPackageID() {
        return packageID;
    }

    public int getIsBroken() {
        return isBroken;
    }

    public int getTimoteiID() {
        return timoteiID;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public void setPackageID(int packageID) {
        this.packageID = packageID;
    }

    public void setIsBroken(int isBroken) {
        this.isBroken = isBroken;
    }

    public void setTimoteiID(int timoteiID) {
        this.timoteiID = timoteiID;
    }
    
    
}
