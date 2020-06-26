package Main.graphicView.scenes;

import Main.controller.GeneralController;
import Main.graphicView.GraphicMain;
import Main.model.accounts.SellerAccount;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class SellerOffsPage implements Initializable {

    public static final String FXML_PATH = "src/main/sceneResources/SellerPanel/sellerOffsPage.fxml";
    public static final String TITLE = "Seller Offs page";
    public static String selectedOff;

    @FXML
    private ListView list;

    public void goBack(){
        GraphicMain.graphicMain.back();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        SellerAccount sellerAccount = (SellerAccount)GeneralController.currentUser;
        list.getItems().addAll(sellerAccount.getOffIds());
        list.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                selectedOff = (String) list.getSelectionModel().getSelectedItem();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.getButtonTypes().clear();
                alert.setTitle("off information");
                alert.setHeaderText(null);
                alert.setContentText(sellerAccount.getOffWithId(selectedOff).viewMe());
                ButtonType cancel = new ButtonType("cancel");
                ButtonType edit = new ButtonType("edit");
                alert.getButtonTypes().addAll(cancel, edit);
                Optional<ButtonType> option = alert.showAndWait();
                if(option.get().equals(edit)){
                    try {
                        GraphicMain.graphicMain.goToPage(OffEditPage.FXML_PATH, OffEditPage.TITLE);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}