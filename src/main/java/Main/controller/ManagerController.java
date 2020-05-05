package Main.controller;

import Main.model.Category;
import Main.model.DiscountCode;
import Main.model.Product;
import Main.model.accounts.Account;
import Main.model.accounts.BuyerAccount;
import Main.model.accounts.ManagerAccount;
import Main.model.accounts.SellerAccount;
import Main.model.exceptions.InvalidInputException;
import Main.model.requests.Request;

import java.util.ArrayList;

public class ManagerController {
    public ManagerAccount chiefManager = null;

    public String showUsersList() {
        return ManagerAccount.showManagersList() + SellerAccount.showSellersList() + BuyerAccount.showBuyersList();
    }

    public String viewUserWithUserName(String userName) {
        Account account = Account.getUserWithUserName(userName);
        if (account == null) {
            return "There is no user with given userName !";
        } else if (account instanceof ManagerAccount) {
            ManagerAccount managerAccount = (ManagerAccount) account;
            return managerAccount.viewMe();
        } else if (account instanceof SellerAccount) {
            SellerAccount sellerAccount = (SellerAccount) account;
            return sellerAccount.viewMe();
        } else {
            BuyerAccount buyerAccount = (BuyerAccount) account;
            return buyerAccount.viewOrders();
        }
    }

    public void deleteUserWithUserName(String userName) throws Exception {
        Account account = Account.getUserWithUserName(userName);
        if (account == null) {
            throw new Exception("There is no user with given userName !\n");
        } else if (account instanceof ManagerAccount) {
            ManagerAccount managerAccount = (ManagerAccount) account;
            ManagerAccount.deleteManager(managerAccount);
        } else if (account instanceof SellerAccount) {
            SellerAccount sellerAccount = (SellerAccount) account;
            sellerAccount.deleteSeller();
        } else {
            BuyerAccount buyerAccount = (BuyerAccount) account;
            buyerAccount.deleteBuyer();
        }
    }

    public void createManagerProfile(ArrayList<String> managerInfo) throws InvalidInputException {
        ManagerAccount managerAccount = new ManagerAccount(managerInfo.get(0), managerInfo.get(1), managerInfo.get(2),
                managerInfo.get(3), managerInfo.get(4), managerInfo.get(5));
        ManagerAccount.addManager(managerAccount);
    }

    public void removeProductWithId(String productId) throws Exception {
        Product product = Product.getProductWithId(productId);
        if (product == null) {
            throw new Exception("There is no product with given ID !\n");
        }
        product.removeProduct();
    }

    public void createDiscountCode(ArrayList<String> discountInfo) throws Exception {
        ArrayList<BuyerAccount> buyersList = extractDiscountBuyersList(discountInfo);
        DiscountCode discountCode = new DiscountCode(discountInfo.get(0), discountInfo.get(1), discountInfo.get(2),
                Double.parseDouble(discountInfo.get(3)), Double.parseDouble(discountInfo.get(4)), Integer.parseInt(discountInfo.get(5)),
                buyersList);

        DiscountCode.addDiscountCode(discountCode);
        for (BuyerAccount buyerAccount : buyersList) {
            buyerAccount.addDiscountCode(discountCode);
        }
    }

    private ArrayList<BuyerAccount> extractDiscountBuyersList(ArrayList<String> discountInfo) throws Exception {
        ArrayList<BuyerAccount> buyersList = new ArrayList<BuyerAccount>();

        int discountInfoSize = discountInfo.size();
        for (int i = 6; i < discountInfoSize; i++) {
            BuyerAccount buyerAccount = BuyerAccount.getBuyerWithUserName(discountInfo.get(i));
            if (buyerAccount == null) {
                throw new Exception("There are some invalid user names in the given inforamtion !\n" +
                        "Please check user names and try again !\n");
            } else {
                buyersList.add(buyerAccount);
            }
        }
        return buyersList;
    }

    public String showAllDiscountCodes() {
        return DiscountCode.showAllDiscountCodes();
    }

    public String viewDiscountCodeWithCode(String code) {
        DiscountCode discountCode = DiscountCode.getDiscountCodeWithCode(code);
        if (discountCode == null) {
            return "There is no discount code with given code !";
        }
        return discountCode.viewMeAsManager();
    }

    public void editDiscountCodeWithCode(String code) {
//TODO
    }

    public void removeDiscountCodeWithCode(String code) throws Exception {
        DiscountCode discountCode = DiscountCode.getDiscountCodeWithCode(code);
        if (discountCode == null) {
            throw new Exception("There is no discount code with given code !\n");
        }
        discountCode.removeDiscountCode();
    }

    public String showAllRequests() {
        return Request.showAllRequests();
    }

    public String showRequestDetailsWithId(String requestId) {
        Request request = Request.getRequestWithId(requestId);
        if (request == null) {
            return "There is no request with given ID !\n";
        } else {
            return request.showRequest();
        }
    }

    public void acceptRequestWithId(String requestId) throws Exception {
        Request request = Request.getRequestWithId(requestId);
        if (request == null) {
            throw new Exception("There is no request with given ID !\n");
        } else {
            request.accept();
        }
    }

    public void declineRequestWithId(String requestId) throws Exception {
        Request request = Request.getRequestWithId(requestId);
        if (request == null) {
            throw new Exception("There is no request with given ID !\n");
        } else {
            request.decline();
        }
    }

    public void editCategoryWithId(String categoryId) {
        //TODO
    }

    public void createCategory(String name, ArrayList<String> specialFeatures) {
        Category category = new Category(name, specialFeatures);
        Category.addCategory(category);
    }

    /**
     public void addCategoryWithId(String categoryId) {

     }
     */

    /**
     * public void getCategoryInformation(ArrayList<String> categoryInfo) {
     * <p>
     * }
     */

    public void removeCategoryWithId(String categoryId) throws Exception {
        Category category = Category.getCategoryWithName(categoryId);
        if (category == null) {
            throw new Exception("There is no category with given ID !\n");
        }
        category.removeCategory();
    }

    /**
     public void createDiscountCode(String discountCode) {

     }
     */


}
