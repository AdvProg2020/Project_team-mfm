package Main.client.graphicView.scenes.ManagerPanel;

import Main.client.graphicView.GraphicMain;
import Main.client.graphicView.scenes.MainMenuController;
import Main.client.requestBuilder.DataRequestBuilder;
import Main.client.requestBuilder.GeneralRequestBuilder;
import Main.client.requestBuilder.ManagerRequestBuilder;
import Main.server.controller.GeneralController;
import Main.server.model.Product;
import Main.server.model.accounts.BuyerAccount;
import Main.server.model.logs.BuyLog;
import Main.server.model.logs.DeliveryStatus;
import Main.server.model.logs.Log;
import com.gilecode.yagson.com.google.gson.stream.JsonReader;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

import java.io.*;
import java.util.ArrayList;
import java.util.Optional;

import static java.util.Arrays.asList;

public class ManageOrdersController {
    public static final String FXML_PATH = "src/main/sceneResources/ManagerPanel/ManageAllOrders.fxml";
    public static final String TITLE = "Manager Orders";
    private ArrayList<BuyerAccount> allBuyers = new ArrayList<>();

    @FXML
    private ListView ordersList;

    public void initialize() {
        String allBuyersDataResponse = DataRequestBuilder.buildAllBuyersRequest();
        readAllBuyersData(allBuyersDataResponse);

        ordersList.getItems().clear();
        for (BuyerAccount buyer : allBuyers) {
            ordersList.getItems().addAll(buyer.buyLogsList());
        }
        ordersList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (ordersList.getSelectionModel().getSelectedItem() != null) {
                    String logInfo = ordersList.getSelectionModel().getSelectedItem().toString();
                    String logId = logInfo.substring(1, logInfo.indexOf(" "));
                    ordersList.getSelectionModel().clearSelection();
                    BuyLog buyLog = null;
                    try {
                        String response = DataRequestBuilder.buildLogRequestWithID(logId);
                        buyLog = GeneralController.yagsonMapper.fromJson(response, BuyLog.class);
                    } catch (Exception e) {
                        ManagerPanelController.alertError(e.getMessage());
                    }
                    showLogInfo(buyLog);
                }
            }
        });
    }

    public void readAllBuyersData(String allProductsString) {
        GeneralController.jsonReader = new JsonReader(new StringReader(allProductsString));
        BuyerAccount[] allBuy = GeneralController.yagsonMapper.fromJson(GeneralController.jsonReader, BuyerAccount[].class);
        allBuyers = (allBuy == null) ? new ArrayList<>() : new ArrayList<>(asList(allBuy));
    }

    private void showLogInfo(BuyLog buyLog) {
        if (buyLog.getDeliveryStatus().equals(DeliveryStatus.PENDING_DELIVERY)) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Log Info");
            alert.setHeaderText(buyLog.viewLog());
            alert.setContentText("what do you want to do with this log?");
            alert.getButtonTypes().clear();
            ButtonType cancel = new ButtonType("Cancel");
            alert.getButtonTypes().add(cancel);
            ButtonType mark_as_delivered = new ButtonType("Mark As Delivered");
            alert.getButtonTypes().add(mark_as_delivered);
            Optional<ButtonType> option = alert.showAndWait();
            if (option.get().equals(mark_as_delivered)) {
                String response = ManagerRequestBuilder.buildMarkDeliveredRequest(buyLog.getLogId());
                if (response.equals("success")) {
                    Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                    alert1.setTitle("BuyLog Delivery Status");
                    alert1.setHeaderText(null);
                    if (response.equals("success")) {
                        alert1.setContentText("BuyLog marked as delivered successfully.");
                    } else {
                        alert1.setContentText("BuyLog couldn't be marked as delivered, try a few moments later.");
                    }
                    alert1.showAndWait();
                    initialize();
                } else {
                    showResponseMessage(response);
                }
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Log Info");
            alert.setHeaderText(buyLog.viewLog());
            alert.setContentText("what do you want to do with this log?");
            alert.getButtonTypes().clear();
            ButtonType cancel = new ButtonType("Cancel");
            alert.getButtonTypes().add(cancel);
            Optional<ButtonType> option = alert.showAndWait();
        }
    }

    private void showResponseMessage(String response) {
        if (response.equals("loginNeeded")) {
            GraphicMain.showInformationAlert("you must login first !\nyou'r authentication might be expired !");
        } else {
            GraphicMain.showInformationAlert("problem connecting the server !\n try a few moments later .");
        }
    }


    public void logout() throws IOException {
        //GraphicMain.generalController.logout();
        GeneralRequestBuilder.buildLogoutRequest();
        GraphicMain.token = "0000";
        //goBack();
        GraphicMain.graphicMain.goToPage(MainMenuController.FXML_PATH, MainMenuController.TITLE);
    }

    public void goBack() throws IOException {
        //GraphicMain.buttonSound.stop();
        //GraphicMain.buttonSound.play();
        GraphicMain.graphicMain.back();
    }
}