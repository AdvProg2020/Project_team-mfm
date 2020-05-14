package Main.controller;

import Main.model.Category;
import Main.model.Product;
import Main.model.accounts.SellerAccount;
import Main.model.discountAndOffTypeService.Off;
import Main.model.discountAndOffTypeService.OffStatus;
import Main.model.exceptions.AccountsException;
import Main.model.exceptions.CreateProductException;
import Main.model.exceptions.DiscountAndOffTypeServiceException;
import Main.model.requests.*;

import java.util.ArrayList;

public class SellerController {
    public String viewCompanyInformation() {
        return ((SellerAccount) GeneralController.currentUser).viewCompanyInformation();
    }

    public String viewSalesHistory() {
        return ((SellerAccount) GeneralController.currentUser).viewSalesHistory();
    }

    public String showSellerProducts() {
        return ((SellerAccount) GeneralController.currentUser).showSellerProducts();
    }

    public String viewSellerProductWithId(String productId) {
        return ((SellerAccount) GeneralController.currentUser).viewSellerProductWithId(productId);
    }

    public void editProductWithId(String productId) {

    }

    public String viewBuyersOfProductWithId(String productId) throws Exception {
        return Product.getProductWithId(productId).viewBuyers();
    }

    public void addProduct(ArrayList<String> productInfo) throws CreateProductException.InvalidProductInputInfo,
            CreateProductException.GetCategoryFromUser {
        validateAddProductInfo(productInfo);
        Product product = new Product(productInfo.get(0), productInfo.get(1), Integer.parseInt(productInfo.get(2)),
                productInfo.get(3), Double.parseDouble(productInfo.get(4)));
        Request request = new AddProductRequest(product, "Add product request");
        Request.addRequest(request);

        doesNewProductsHaveInitialCategory(product, productInfo.get(5), productInfo.get(6));
    }

    private void doesNewProductsHaveInitialCategory(Product product, String doesUserWantToAddCategory, String categoryName)
            throws CreateProductException.GetCategoryFromUser {
        if (doesUserWantToAddCategory.trim().equalsIgnoreCase("yes")) {
            Category category = null;
            try {
                category = Category.getCategoryWithName(categoryName);
            } catch (Exception e) {
            }
            product.setCategory(category);
            throw new CreateProductException.GetCategoryFromUser(category, product);
        }
    }

    public void setSpecialFeatures(Product product, ArrayList<String> specialFeatures) {
        product.setSpecialFeatures(specialFeatures);
    }

    private void validateAddProductInfo(ArrayList<String> productInfo) throws CreateProductException.InvalidProductInputInfo {
        String addProductErrors = new String();

        try {
            AccountsException.validateNameTypeInfo("product name", productInfo.get(0));
        } catch (AccountsException e) {
            addProductErrors.concat(e.getErrorMessage());
        }
        try {
            CreateProductException.validateAvailability(productInfo.get(2));
        } catch (Exception e) {
            addProductErrors.concat(e.getMessage());
        }
        try {
            CreateProductException.validatePrice(productInfo.get(4));
        } catch (Exception e) {
            addProductErrors.concat(e.getMessage());
        }
        if (productInfo.get(5).trim().equalsIgnoreCase("yes")) {
            try {
                Category.getCategoryWithName(productInfo.get(6));
            } catch (Exception e) {
                addProductErrors.concat(e.getMessage());
            }
        } else if (!productInfo.get(5).trim().equalsIgnoreCase("no")) {
            addProductErrors.concat("we couldn't recognize if you want to add category or not !\nplease either write 'yes' or 'no' !\n");
        }

        if (!addProductErrors.isEmpty()) {
            throw new CreateProductException.InvalidProductInputInfo("there were some errors in product creation : \n" + addProductErrors);
        }
    }

    public String viewSellerOffs() {
        return ((SellerAccount) GeneralController.currentUser).viewSellerOffs();
    }

    public String viewAllOffs() {
        return Off.viewAllOffs();
    }

    public String viewOffWithId(String offId) throws Exception {
        return Off.getOffWithId(offId).viewMe();
    }

    public EditOffRequest getOffToEdit(String offID) throws Exception {
        Off off = Off.getOffWithId(offID);
        validateOffEditPermission(off);
        return new EditOffRequest(off);
    }

    private void validateOffEditPermission(Off off) throws Exception {
        SellerAccount sellerAccount = (SellerAccount) GeneralController.currentUser;
        if (!sellerAccount.doesSellerHaveOffWithReference(off)) {
            throw new Exception("this off doesn't belong to any of your products !\n");
        }
    }

    public void submitOffEdits(EditOffRequest editOffRequest) throws Exception {
        validateInputEditOffInfo(editOffRequest);
        editOffRequest.getOff().setOffStatus(OffStatus.PENDING_EDIT_OFF);
        Request.addRequest(editOffRequest);
    }

    private void validateInputEditOffInfo(EditOffRequest editOffRequest) throws Exception {
        String esitOffErrors = new String();

        if (editOffRequest.getEditedFieldTitles().isEmpty()) {
            esitOffErrors.concat("you must edit at least one field!\n");
        }
        try {
            DiscountAndOffTypeServiceException.validateInputDate(editOffRequest.getStartDate());
        } catch (Exception e) {
            esitOffErrors.concat("start date is invalid :\n" + e.getMessage());
        }
        try {
            DiscountAndOffTypeServiceException.validateInputDate(editOffRequest.getEndDate());
        } catch (Exception e) {
            esitOffErrors.concat("end date is invalid :\n" + e.getMessage());
        }
        if (esitOffErrors.isEmpty()) {
            try {
                DiscountAndOffTypeServiceException.compareStartAndEndDate(editOffRequest.getStartDate(), editOffRequest.getEndDate());
            } catch (Exception e) {
                esitOffErrors.concat(e.getMessage());
            }
        }
        try {
            DiscountAndOffTypeServiceException.validateInputAmount(editOffRequest.getOffAmount());
        } catch (Exception e) {
            esitOffErrors.concat(e.getMessage());
        }
        try {
            validateEditOffProductsToBeAdded(editOffRequest);
        } catch (Exception e) {
            esitOffErrors.concat(e.getMessage());
        }
        try {
            validateEditOffProductsToBeRemoved(editOffRequest);
        } catch (Exception e) {
            esitOffErrors.concat(e.getMessage());
        }

        if (!esitOffErrors.isEmpty()) {
            throw new Exception("there were some errors in editing off : \n" + esitOffErrors);
        }

    }


    private void validateEditOffProductsToBeAdded(EditOffRequest editOffRequest) throws Exception {
        String invalidIDs = new String();

        SellerAccount sellerAccount = (SellerAccount) GeneralController.currentUser;
        for (String productIDToBeAdded : editOffRequest.getProductIDsToBeAdded()) {
            Product product = null;
            try {
                product = Product.getProductWithId(productIDToBeAdded);
            } catch (Exception e) {
                invalidIDs.concat(e.getMessage());
            }
            if(!sellerAccount.doesSellerHaveProductWithReference(product)){
                invalidIDs.concat("product with ID : " + productIDToBeAdded + " doesn't belong to you!\n");
            }
        }
        if (!invalidIDs.isEmpty()) {
            throw new Exception("there where some errors in adding products : \n" + invalidIDs);
        }
    }

    private void validateEditOffProductsToBeRemoved(EditOffRequest editOffRequest) throws Exception {
        String invalidIDs = new String();
        for (String productIDToBeRemoved : editOffRequest.getProductIDsToBeRemoved()) {
            try {
                Product product = Product.getProductWithId(productIDToBeRemoved);
                if (!editOffRequest.getOff().isThereProductWithReference(product)) {
                    throw new Exception("There is no product with ID : " + productIDToBeRemoved + " in off's product list !\n");
                }
            } catch (Exception e) {
                invalidIDs.concat(e.getMessage());
            }
        }
        if (!invalidIDs.isEmpty()) {
            throw new Exception("there where some errors in removing products : \n" + invalidIDs);
        }
    }

    public void addOff(ArrayList<String> productIDs, ArrayList<String> offInfo) throws Exception {
        validateOffInputInfo(productIDs, offInfo);
        ArrayList<Product> products = extractOffProductsList(productIDs);
        Off off = new Off(products, offInfo.get(0), offInfo.get(1), Double.parseDouble(offInfo.get(2)), SellerAccount.getSellerWithUserName(offInfo.get(3)));
        AddOffRequest addOffRequest = new AddOffRequest(off, "Add New Off Request");
        Request.addRequest(addOffRequest);
    }

    private void validateOffInputInfo(ArrayList<String> productIDs, ArrayList<String> offInfo) throws Exception {
        String addOffErrors = new String();

        try {
            validateAddOffProducts(productIDs);
        } catch (Exception e) {
            addOffErrors.concat(e.getMessage());
        }
        try {
            DiscountAndOffTypeServiceException.validateInputDate(offInfo.get(0));
        } catch (Exception e) {
            addOffErrors.concat("start date is invalid :\n" + e.getMessage());
        }
        try {
            DiscountAndOffTypeServiceException.validateInputDate(offInfo.get(1));
        } catch (Exception e) {
            addOffErrors.concat("end date is invalid :\n" + e.getMessage());
        }
        if (addOffErrors.isEmpty()) {
            try {
                DiscountAndOffTypeServiceException.compareStartAndEndDate(offInfo.get(0), offInfo.get(1));
            } catch (Exception e) {
                addOffErrors.concat(e.getMessage());
            }
        }
        try {
            DiscountAndOffTypeServiceException.validateInputAmount(offInfo.get(2));
        } catch (Exception e) {
            addOffErrors.concat(e.getMessage());
        }
        try {
            SellerAccount.getSellerWithUserName(offInfo.get(3));
        } catch (Exception e) {
            addOffErrors.concat(e.getMessage());
        }

        if (!addOffErrors.isEmpty()) {
            throw new Exception("there were some errors in adding Off : \n" + addOffErrors);
        }
    }

    private ArrayList<Product> extractOffProductsList(ArrayList<String> productIDs) throws Exception {
        ArrayList<Product> products = new ArrayList<>();
        for (String productID : productIDs) {
            products.add(Product.getProductWithId(productID));
        }
        return products;
    }

    private void validateAddOffProducts(ArrayList<String> productIDs) throws Exception {
        String invalidProductsOffErrors = new String();

        for (String productID : productIDs) {
            try {
                Product.getProductWithId(productID);
            } catch (Exception e) {
                invalidProductsOffErrors.concat(e.getMessage());
            }
        }

        if (!invalidProductsOffErrors.isEmpty()) {
            throw new Exception("there were some errors in setting off products : \n" + invalidProductsOffErrors);
        }
    }

    public String viewSellerBalance() {
        return ((SellerAccount) GeneralController.currentUser).viewBalance();
    }
}
