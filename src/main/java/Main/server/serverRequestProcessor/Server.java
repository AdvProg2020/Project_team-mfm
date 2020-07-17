package Main.server.serverRequestProcessor;

import Main.server.model.accounts.Account;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class Server {
    /**
     * check what the request starts with, guid to relevant request processor
     */
    private HashMap<String, TokenInfo> tokens = new HashMap<>();
    public static int TOKEN_EXPIRATION_MINUTES = 15;
    private ServerSocket serverSocket;
    private static Server serverInstance;

    private Server() {
        try {
            serverSocket = new ServerSocket(0);
            System.out.println(serverSocket.getLocalPort());
        } catch (IOException e) {
            e.printStackTrace();
        }
        serverInstance = this;
    }

    public static Server getServer() {
        if (serverInstance == null) {
            new Server();
        }
        return serverInstance;
    }

    public void start() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Socket clientSocket = serverSocket.accept();
                        new requestHandler(clientSocket).handle();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private class requestHandler extends Thread {
        private Socket clientSocket;
        private DataOutputStream dataOutputStream;
        private DataInputStream dataInputStream;

        public requestHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
            try {
                this.dataOutputStream = new DataOutputStream(new BufferedOutputStream(clientSocket.getOutputStream()));
                this.dataInputStream = new DataInputStream(new BufferedInputStream(clientSocket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void handle() throws Exception {
            String request = null;
            String response = null;
            String[] splitRequest = new String[0];

            request = dataInputStream.readUTF();
            System.out.println("server read " + request);

            splitRequest = request.split("#");
            if (splitRequest.length < 2) {
                response = "invalidRequest";
            } else if (splitRequest[1].equals("manager")) {
                response = ManagerRequestProcessor.process(splitRequest);
            }  else if (splitRequest[1].equals("buyer")) {
                response =BuyerRequestProcessor.process(splitRequest);
            } else if (splitRequest[1].equals("logout")) {
                response = logout(splitRequest);
            } else if (splitRequest[1].equals("login")) {
                response = GeneralRequestProcessor.loginRequestProcessor(splitRequest);
            } else if (splitRequest[1].equals("signUp")) {
                response = GeneralRequestProcessor.signUpRequestProcessor(splitRequest);
            } else if (splitRequest[1].equalsIgnoreCase("signUpSeller")) {
                response = GeneralRequestProcessor.signUpSellerRequestProcessor(splitRequest);
            } else if (splitRequest[1].equalsIgnoreCase("signUpBuyer")) {
                response = GeneralRequestProcessor.signUpBuyerRequestProcessor(splitRequest);
            } else if (splitRequest[1].equalsIgnoreCase("signUpManager")) {
                response = GeneralRequestProcessor.signUpManagerRequestProcessor(splitRequest);
            } else if (splitRequest[1].equalsIgnoreCase("data")) {
                response = DataRequestProcessor.process(splitRequest);
            } else if (splitRequest[1].equals("disconnect")) {
                response = "disconnected";
            } else if (splitRequest[1].equals("buyerBalance")) {
                response = BuyerRequestProcessor.initializeBuyerPanelRequestProcessor(splitRequest);
            } else if (splitRequest[1].equals("buyerPersonalInfo")) {
                response = BuyerRequestProcessor.buyerPersonalInfoRequestProcessor(splitRequest);
            } else if (splitRequest[1].equals("managerPersonalInfo")) {
                response = ManagerRequestProcessor.managerPersonalInfoRequestProcessor(splitRequest);
            } else if (splitRequest[1].equals("editManagerPersonalInfo")) {
                response = ManagerRequestProcessor.editManagerPersonalInformationRequestProcessor(splitRequest);
            } else if (splitRequest[1].equals("initializeManageRequests")) {
                response = ManagerRequestProcessor.initializeManageRequestsRequestProcessor();
            } else if (splitRequest[1].equals("showRequestWithId")) {
                response = ManagerRequestProcessor.showRequestWithIdRequestProcessor(splitRequest);
            } else if (splitRequest[1].equals("acceptRequestWithId")) {
                response = ManagerRequestProcessor.acceptRequestWithIdRequestProcessor(splitRequest);
            } else if (splitRequest[1].equals("declineRequestWithId")) {
                response = ManagerRequestProcessor.declineRequestWithIdRequestProcessor(splitRequest);
            } else if (splitRequest[1].equals("initializeManageProducts")) {
                response = ManagerRequestProcessor.initializeManageProductsRequestProcessor();
            } else if (splitRequest[1].equals("showProductDigestWithId")) {
                response = ManagerRequestProcessor.showProductDigestWithIdRequestProcessor(splitRequest);
            } else if (splitRequest[1].equals("deleteProductWithId")) {
                response = ManagerRequestProcessor.deleteProductWithIdRequestProcessor(splitRequest);
            } else if (splitRequest[1].equals("initializeManageUsers")) {
                response = ManagerRequestProcessor.initializeManageUsersRequestProcessor();
            } else if (splitRequest[1].equals("userViewMeWithUserName")) {
                response = ManagerRequestProcessor.userViewMeWithUserNameRequestProcessor(splitRequest);
            } else if (splitRequest[1].equals("getMyUserName")) {
                response = ManagerRequestProcessor.getMyUserNameRequestProcessor(splitRequest);
            } else if (splitRequest[1].equals("deleteUserWithUserName")) {
                response = ManagerRequestProcessor.deleteUserWithUserNameRequestProcessor(splitRequest);
            }



            else if (splitRequest[1].equals("addComment")) {
                response = SellerRequestProcessor.buildCommentResponse(splitRequest);
            }else if(splitRequest[1].equals("getListItemsForAddOffPage")){
                response = SellerRequestProcessor.getListItemsForAddOffPage(splitRequest[0]);
            }else if(splitRequest[1].equals("addOff")){
                response = SellerRequestProcessor.buildAddOffResponse(splitRequest);
            } else if(splitRequest[1].equals("addProduct")){
                response=SellerRequestProcessor.buildAddProductResponse(splitRequest);
            }else if(splitRequest[2].equals("addSpecialFeatures")){
                response = SellerRequestProcessor.buildAddSpecialFeaturesResponse(splitRequest);
            }

            dataOutputStream.writeUTF(response);
            dataOutputStream.flush();
            System.out.println("server wrote " + response);

            if (response != "disconnected") {
                handle();
            } else {
                removeToken(splitRequest[0]);
                clientSocket.close();
                dataOutputStream.close();
                dataInputStream.close();
            }
        }
    }

    public TokenInfo getTokenInfo(String token) {
        for (String tokenString : tokens.keySet()) {
            if (tokenString.equals(token)) {
                return tokens.get(tokenString);
            }
        }
        return null;
    }

    public void removeToken(String token) {
        if (tokens.containsKey(token)) {
            tokens.remove(token);
        }
    }

    public String addToken(Account account) {
        String token = getRandomString(5);
        TokenInfo tokenInfo = new TokenInfo(account);
        tokens.put(token, tokenInfo);
        return token;
    }

    private String getRandomString(int stringLength) {
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        StringBuilder stringBuilder = new StringBuilder(stringLength);

        for (int i = 0; i < stringLength; i++) {

            int index = (int) (AlphaNumericString.length() * Math.random());
            stringBuilder.append(AlphaNumericString.charAt(index));
        }

        return stringBuilder.toString();
    }

    public boolean validateToken(String token, Class<?> classType) {
        TokenInfo tokenInfo = tokens.get(token);
        if (tokenInfo == null) {
            return false;
        }
        if (tokenInfo.hasTokenExpired()) {
            removeToken(token);
            return false;
        }
        if (tokenInfo.getUser().getClass() == classType || classType == Account.class) {
            return true;
        }
        return false;
    }

    private String logout(String[] splitRequest) {
        tokens.remove(splitRequest[0]);
        return "success";
    }
}
