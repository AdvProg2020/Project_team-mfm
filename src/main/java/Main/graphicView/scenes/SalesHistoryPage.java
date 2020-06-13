package Main.graphicView.scenes;

import Main.controller.SellerController;
import Main.graphicView.GraphicMain;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SalesHistoryPage implements Initializable {

    public static final String FXML_PATH = "src/main/sceneResources/SellerPanel/salesHistoryPage.fxml";
    public static final String TITLE = "Sales History";

    @FXML
    private VBox logIdBox;

    public ArrayList<Label> getLabels(){
        ArrayList<Label> idList = new ArrayList<>();
        for(int i=0;i<10;i++){
            Label label =(Label) logIdBox.getChildren().get(i);
            idList.add(label);
        }
        return idList;
    }

    public void goBack(){
        GraphicMain.graphicMain.back();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        FillLogVBox();
    }

    public void FillLogVBox() {
        if(GraphicMain.sellerController.getSellLogIds().equals(null)){
            getLabels().get(0).setText("no sales yet!");
        } else{
            for(int i=0; i<GraphicMain.sellerController.getSellLogIds().size(); i++){
                getLabels().get(i).setText(GraphicMain.sellerController.getSellLogIds().get(i));
            }
        }
    }

    public void showLogDetails(){
        //TODO complete process
    }
}
