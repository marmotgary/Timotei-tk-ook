/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timotei;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author root
 */
public class FXMLDocumentController implements Initializable {
    private ArrayList<Smartpost> smartList;
    private ArrayList<Smartpost> smartOnMapList;
    private ArrayList<Obj> objList;
    
    private Label label;
    @FXML
    private WebView map;
    @FXML
    private ComboBox<Smartpost> spBox;
    @FXML
    private Button clearMarkers;
    @FXML
    private Button addToMap;
    @FXML
    private ComboBox<Obj> objBox;
    @FXML
    private Tab itemTab;
    @FXML
    private Button createItem;
    @FXML
    private ComboBox<ObjX> packetObjBox;
    @FXML
    private ComboBox<Package> dbPackageBox;
    @FXML
    private Button packButton;
    @FXML
    private ComboBox<ObjX> removeObjBox;
    @FXML
    private ComboBox<PackageX> removePackageBox;
    @FXML
    private Button removeButton;
    Timo timo;
    @FXML
    private TextArea packagingText;
    @FXML
    private TextArea spInfo;
    @FXML
    private ComboBox<Smartpost> spOnMapBox1;
    @FXML
    private ComboBox<Smartpost> spOnMapBox2;
    @FXML
    private TextArea sendPackageInfo;
    @FXML
    private ComboBox<PackageX> sendPackageBox;
    @FXML
    private Button sendPackageButton;
    @FXML
    private ComboBox<History> historyBox;
    @FXML
    private TextArea singleHistoryInfo;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        timo = new Timo();
        if(timo.initXML() == 0){
            try {
                
                Parent root = FXMLLoader.load(getClass().getResource("FXMLError.fxml"));
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                Scene scene = new Scene(root);
                stage.setTitle("Virhe!");
                stage.setScene(scene);
                stage.show();
            } catch (IOException ex) {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        timo.addStorageDB("Yleisvarasto");
        map.getEngine().load(getClass().getResource("index.html").toExternalForm());
        //Initialize comboboxes
        smartList = timo.getSpList();
        spBox.getItems().addAll(smartList);
        smartOnMapList = new ArrayList();
        objList = new ArrayList();
        timo.loadObjects(objList);
        objBox.getItems().addAll(objList);
        timo.packageClassToDB();
        dbPackageBox.getItems().addAll(timo.getPackageClasses());
        
        
        
    }
    
    public void wee(){
        System.out.println("wee");
    }

    @FXML
    private void emptyMarkersAction(ActionEvent event) {
        map.getEngine().executeScript("document.deleteMarkers()");
        map.getEngine().executeScript("document.deletePaths()");
        spOnMapBox1.setValue(null);
        spOnMapBox2.setValue(null);
        smartOnMapList.clear();
    }
    //Lisää smartpostin kartalle
    @FXML
    private void addSpToMapAction(ActionEvent event) {
        if(spBox.getValue()!=null){
        String spOffice = spBox.getValue().toString();
        int index = smartList.indexOf(spBox.getValue());
        Smartpost sp = smartList.get(index);

        System.out.println("lati: " + sp.getLatitude() + " longi: " + sp.getLongitude() + " aukiolo: " + sp.getAukiolo());
        String lati = sp.getLatitude();
        String lng = sp.getLongitude();
        String info = sp.getAukiolo();
        map.getEngine().executeScript("document.createMarker('" + lati + "', ' " + lng + "',' " + info + "')");
        smartOnMapList.add(sp);
        }
    }


    @FXML
    private void createItemAction(ActionEvent event) {
        if(objBox.getValue() != null){
            timo.createItemDB(objBox.getValue());
            objBox.setValue(null);
        }
        
    }
    //Paketoi esine
    @FXML
    private void packAction(ActionEvent event) {
        if(packetObjBox.getValue()!=null && dbPackageBox.getValue()!=null){
            int state = timo.createPackage(packetObjBox.getValue(), dbPackageBox.getValue());
            
            if (state == 1){
                packagingText.setText("Paketti luotu onnistuneesti!");
            } else if (state == 0){
                packagingText.setText("Paketin luonti epäonnistui!\nLuokan "+dbPackageBox.getValue().getLuokka()
                +" paketin tilavuus: "+dbPackageBox.getValue().getTilavuus()+"\n"
                +"Esineen tilavuus: "+packetObjBox.getValue().getTilavuus());
            }
            packetObjBox.setValue(null);
            dbPackageBox.setValue(null);
        }
    }
    //Poistaa joko luodun esineen, paketin tai kummatkin.
    @FXML
    private void removeButtonAction(ActionEvent event) {
        if(removeObjBox.getValue()!=null){
            timo.deleteItemDB(removeObjBox.getValue());
            removeObjBox.setValue(null);
        }
        
        if(removePackageBox.getValue()!=null){
            timo.deletePackageDB(removePackageBox.getValue());
            removePackageBox.setValue(null);
        }
    }

    //Päivittää esineluettelon tietokannasta
    @FXML
    private void dbObjectAction(Event event) {
        packetObjBox.getItems().clear();
        packetObjBox.setValue(null);
        packetObjBox.getItems().addAll(timo.getNoPackageObjList());

    }
    //Päivittää poistokohdan esineluettelon
    @FXML
    private void dbDelObjectAction(Event event) {
        removeObjBox.getItems().clear();
        removeObjBox.setValue(null);
        removeObjBox.getItems().addAll(timo.getNoPackageObjList());
    }
    //päivittää pakettiluettelon tietokannasta
    @FXML
    private void dbPackageAction(Event event) {
        removePackageBox.getItems().clear();
        removePackageBox.setValue(null);
        removePackageBox.getItems().addAll(timo.getPackageList());
    }
    
    @FXML
    private void updatePackageBox(Event event) {
        sendPackageBox.getItems().clear();
        sendPackageBox.setValue(null);
        sendPackageBox.getItems().addAll(timo.getPackageList());
        sendPackageInfo.setText("S/M/A/R/Tpostinator");
    }

    
    @FXML
    private void updateSpOnMapList(Event event) {
        spOnMapBox1.getItems().clear();
        spOnMapBox1.getItems().addAll(smartOnMapList);

    }

    @FXML
    private void updateSpOnMapList2(Event event) {
        spOnMapBox2.getItems().clear();
        spOnMapBox2.getItems().addAll(smartOnMapList);

    }

    @FXML
    private void updateSingleHistoryInfo(ActionEvent event) {
        String isbroken;
        if (historyBox.getValue().getIsBroken() == 1) {
            isbroken = "Paketti rikkoutui kuljetuksen aikana";
        } else if (historyBox.getValue().getIsBroken() == 0) {
            isbroken = "Paketti saapui ehjänä määränpäähän";
        } else {
            isbroken = null;
        }
        singleHistoryInfo.setText("Paketissa: \n" + historyBox.getValue().getObjName() + "\n\n"
                + "Lähetetty: \n" + historyBox.getValue().getStart() + "\n\n"
                + "Vastaanotettu: \n" + historyBox.getValue().getEnd() + "\n\n"
                + "Matkan pituus: \n" + historyBox.getValue().getDistance() + "km\n\n"
                + isbroken+"\n\n"
                + "Aikaleima: "+historyBox.getValue().getTimestamp()+"\n\n"
                + "Paketteja luotu yhteensä: "+timo.getPackageCount());
    }


    @FXML
    private void updateHistoryBox(Event event) {
        historyBox.getItems().clear();
        historyBox.getItems().addAll(timo.getHistoryList());

    }
    
    @FXML
    private void itemChosenAction(ActionEvent event) {
        if(packetObjBox.getValue()!=null){
            packagingText.setText("Esineen tilavuus: "+packetObjBox.getValue().getTilavuus()+"\n"
            + "Paino: "+packetObjBox.getValue().getPaino());
        }
    }

    @FXML
    private void packageClassChosenAction(ActionEvent event) {
        if(dbPackageBox.getValue()!=null){
            if(dbPackageBox.getValue().getLuokka()==1){
            packagingText.setText("Paketin tilavuus: "+dbPackageBox.getValue().getTilavuus()+"\n"
                    + "Maksimi kantama: 150km\nRikkoutuva paketti rikkoutuu matkalla!");
            }else if(dbPackageBox.getValue().getLuokka()==2){
                packagingText.setText("Paketin tilavuus: "+dbPackageBox.getValue().getTilavuus()+"\n"
                        + "maksimi kantama: rajoittamaton\n"
                        + "Turvakuljetus: Paketit eivät rikkoudu matkalla!");
            }else if(dbPackageBox.getValue().getLuokka()==3){
                packagingText.setText("Paketin tilavuus: " + dbPackageBox.getValue().getTilavuus() + "\n"
                        + "maksimi kantama: rajoittamaton\n"
                        + "Rikkoutuvat paketit saattavat rikkoutua..");
            }
        }
    }

    @FXML
    private void showSpInfo(ActionEvent event) {
        spInfo.setText("Kaupunki: "+spBox.getValue().getKaupunki()+"\n"
                +spBox.getValue().getAukiolo()+"\n"
                + "Osoite: "+spBox.getValue().getOsoite());
    }

    @FXML
    private void sendPackageAction(ActionEvent event) {
        long lenght;
        String msg;
        PackageX pckg = sendPackageBox.getValue();
        if(spOnMapBox1.getValue()!=null && spOnMapBox2.getValue()!=null 
                && pckg!=null && spOnMapBox1.getValue() != spOnMapBox2.getValue()){
            int luokka = pckg.getLuokka();
            //Selvitetään reitin pituus
            ArrayList<String> array = new ArrayList();
            array.add(spOnMapBox1.getValue().getLatitude());
            array.add(spOnMapBox1.getValue().getLongitude());
            array.add(spOnMapBox2.getValue().getLatitude());
            array.add(spOnMapBox2.getValue().getLongitude());

            lenght = Math.round((double) map.getEngine().executeScript(
                    "document.pathLength(" + array + ")"));
            
            if(luokka==1 && lenght<=150){
                msg = timo.sendPackage(lenght, spOnMapBox1.getValue(),spOnMapBox2.getValue(), pckg);
                map.getEngine().executeScript("document.createPath("+array+", '#CC33FF',"+(double)pckg.getLuokka()+")");
                sendPackageInfo.setText(msg);
                sendPackageBox.setValue(null);
            }else if (luokka==1 && lenght>150){
                    sendPackageInfo.setText("1. luokan maksimietäisyys: 150km\n"
                            + "Smartpostien välinen etäisyys: "+lenght+"km");
            }else{
                msg = timo.sendPackage(lenght,spOnMapBox1.getValue(),spOnMapBox2.getValue(), pckg);
                map.getEngine().executeScript("document.createPath("+array+", '#CC33FF',"+(double)pckg.getLuokka()+")");
                sendPackageInfo.setText(msg);
                sendPackageBox.setValue(null);
            }
            

        }
    }



    
    public class Timo {
        private int storageInitialized = 0;
        private ArrayList<Smartpost> sp;
        private ArrayList<Obj> objList;
        private ArrayList<ObjX> noPackageObjList;
        private ArrayList<Package> packageClassList;
        private ArrayList<PackageX> packageList;
        private ArrayList<History> historyList;
        private dbControl db;
        String pathToDB;
        
        private Timo() {
            pathToDB = "jdbc:sqlite:/root/TIMOTEI/Documents/db.sqlite3";
        }

        
        public int initXML(){
            sp = new ArrayList();
            //Lataa XML tiedot
            LoadXML XML = new LoadXML();
            int state = XML.loadXML();
            if (state == 1){
                XML.parseCurrentData(sp, pathToDB);
                
            } else if(state == -1){
                System.err.println("Failed to read XML data, unexcepted MalformedURLException");
                
            } else if(state == -2){
                System.err.println("Failed to read XML data, check internet connection / URL!");
                return 0;
            }
            return 1;
        }
        
        public ArrayList getSpList(){
            Package paketti = new Package1();
            return sp;
        }
        
        public void addStorageDB(String name) {
            if(storageInitialized == 0){
                Storage s = new Storage();
                //Mikäli varasto lisätään onnistuneeseti palauttaa 1, jolloin
                //varastoa ei yritetä lisätä uudestaan ohjelmaa seuraavaan kerran käytettäessä
                storageInitialized = s.addStorageDB(name);
            }else {
                System.out.println("varasto on jo kannassa");
            }
        }
        public void loadObjects(ArrayList list){
            Objects o = new Objects();
            o.createObjList(list);
        }
        public void packageClassToDB(){
            
            packageClassList = new ArrayList();
            db = dbControl.getInstance(pathToDB);
            db.packageClasstoDB(packageClassList);
        }
        public ArrayList<Package> getPackageClasses(){
            return packageClassList;
        }
        public void createItemDB(Obj o){
            db.createObject(o);
        }
        public void deleteItemDB(ObjX o){
            db.deleteObject(o);
        }
        public void deletePackageDB(PackageX p){
            db.deletePackage(p);
        }
        public int createPackage(ObjX o, Package p){
            int state = db.createPackage(o, p);
            if (state == 1){
                System.out.println("onnistui");
                return 1;
            }
            else if (state == 0){
                System.out.println("ei onnistunut");
                return 0;
            }
            else{
                System.err.println("sumthing gone wrong");
                return -9000;
            }
        }
        //Palauttaa listan objetkeista jotka eivät ole paketoitu
        public ArrayList getNoPackageObjList(){
            noPackageObjList = new ArrayList();
            noPackageObjList.clear();
            db.noPackageObj(noPackageObjList);
            return noPackageObjList;
        }
        //Palauttaa listan paketeista
        public ArrayList getPackageList(){
            packageList = new ArrayList();
            packageList.clear();
            db.packageList(packageList);
            return packageList;
        }
        public ArrayList getHistoryList(){
            historyList = new ArrayList();
            historyList.clear();
            db.historyList(historyList);
            return historyList;
        }
        public int getPackageCount(){
            return db.packageCount();
        }
        public String sendPackage(long distance, Smartpost start, Smartpost dest, PackageX p){
            
            if(p.getLuokka()==1){
                if(p.getRikkoutuu()==1){
                    int isBroken = 1;
                    int state = db.createHistoryEntry(distance, isBroken, start, dest, p);
                    if(state==1){
                        return "Paketti vaurioitui matkalla!";
                        
                    } return "Jokin meni vikaan!";
                }else{
                    int state = db.createHistoryEntry(distance, 0, start, dest, p);
                    return "Paketti lähetetty onnistuneesti!";
                }
            }
            else if(p.getLuokka()==2){
                int state = db.createHistoryEntry(distance, 0, start, dest, p);
                if (state == 1){
                    return "Paketti lähetetty onnistuneesti!";
                }return "Jotain meni vikaan!";
            }
            
            else if(p.getLuokka()==3){
                int weight = db.getObjectWeight(p);
                //String name = db.getObjectName(p);
                String name = p.getNimi();
                if(p.getRikkoutuu()==2 && weight>=70){
                    db.createHistoryEntry(distance, 0, start, dest, p);
                    return "Paketti lähetetty onnistuneesti!";
                }else if( name.equals("Pikachu")){
                    db.createHistoryEntry(distance, 1, start, dest, p);
                    return "Pikachu ei tykännyt heittelystä ja karkasi";
                }else if(p.getRikkoutuu()==2 && weight<70){
                    db.createHistoryEntry(distance, 1, start, dest, p);
                    return "Paketti hajosi matkalla heittelyn seurauksena";
                }else if(p.getRikkoutuu()==0){
                    db.createHistoryEntry(distance, 0, start, dest, p);
                    return "Paketti lähetetty onnistuneesti!";
                }
            }
            return "Odottamaton virhe lähetyksen yhteydessä";
        }
    }
}
