package model.accounts;

import Main.model.accounts.Account;
import Main.model.accounts.BuyerAccount;
import Main.model.accounts.ManagerAccount;
import Main.model.accounts.SellerAccount;
import Main.model.exceptions.InvalidInputException;
import org.junit.Assert;
import org.junit.Test;

public class BuyerAccountTest {

    @Test(expected = InvalidInputException.class)
    public void invalidNameException() throws InvalidInputException {
        BuyerAccount buyerAccount = new BuyerAccount("userName","firs*tName","las#t Name",
                "sampleEmail@sample.sample","09001112233","password123",100);
    }

    @Test(expected = InvalidInputException.class)
    public void duplicateUserNameException() throws InvalidInputException {
        BuyerAccount buyerAccount = new BuyerAccount("userName","firstName","last Name",
                "sampleEmail@sample.sample","09001112233","password123",100);
        BuyerAccount.addBuyer(buyerAccount);
        BuyerAccount buyerAccount2 = new BuyerAccount("userName","firstName","last Name",
                "sampleEmail@sample.sample","09001112233","password123",100);
    }

    @Test(expected = InvalidInputException.class)
    public void invalidPhoneNumberException() throws InvalidInputException {
        BuyerAccount buyerAccount = new BuyerAccount("userName","firstName","las#t Name",
                "sampleEmail@sample.sample","090011112233","password123",100);
    }

    @Test(expected = InvalidInputException.class)
    public void invalidPasswordException() throws InvalidInputException {
        BuyerAccount buyerAccount = new BuyerAccount("userName","firstName","las#t Name",
                "sampleEmail@sample.sample","09001112233","pass",100);
    }

    @Test
    public void addBuyerAndIsThereBuyerTest () throws InvalidInputException {
        BuyerAccount buyerAccount = new BuyerAccount("username","firstname","lastname"
                ,"example@exp.exp","09000000000","00000000",100);

        BuyerAccount.addBuyer(buyerAccount);

        Assert.assertTrue(BuyerAccount.isThereBuyerWithUserName("username")&&Account.isThereUserWithUserName("username"));
    }

    @Test
    public void viewMeTest () throws InvalidInputException {
        BuyerAccount buyerAccount = new BuyerAccount("userName","firstName","last Name",
                "sampleEmail@sample.sample","09001112233","password123",100);
        BuyerAccount.addBuyer(buyerAccount);

        Assert.assertEquals("Buyer :\n\tfirst name : firstName\n\tlast name : last Name\n\tuser name : userName" +
                "\n\temail : sampleEmail@sample.sample\n\tphone number : 09001112233\n",buyerAccount.viewMe());
    }

    @Test
    public void showBuyerList() throws InvalidInputException {
        BuyerAccount buyerAccount = new BuyerAccount("userName","firstName","last Name",
                "sampleEmail@sample.sample","09001112233","password123",100);
        BuyerAccount.addBuyer(buyerAccount);
        BuyerAccount buyerAccount2 = new BuyerAccount("userName2","firstName2","last Name2",
                "sampleEmail@sample.sample2","09001112234","password124",100);
        BuyerAccount.addBuyer(buyerAccount2);

        Assert.assertEquals("Buyers :\n\tuser name : userName\tfull name : firstName last Name\n\tuser name : " +
                "userName2\tfull name : firstName2 last Name2\n",BuyerAccount.showBuyersList());
    }

    @Test
    public void getBuyerWithNameTest() throws InvalidInputException {
        BuyerAccount buyerAccount = new BuyerAccount("userName","firstName","last Name",
                "sampleEmail@sample.sample","09001112233","password123",100);
        BuyerAccount.addBuyer(buyerAccount);

        Assert.assertEquals(buyerAccount,BuyerAccount.getBuyerWithUserName("userName"));
    }

    @Test
    public void deleteBuyerTest() throws InvalidInputException {
        BuyerAccount buyerAccount = new BuyerAccount("userName","firstName","last Name",
                "sampleEmail@sample.sample","09001112233","password123",100);
        BuyerAccount.addBuyer(buyerAccount);
        BuyerAccount.deleteBuyer(buyerAccount);

        Assert.assertFalse(BuyerAccount.isThereBuyerWithUserName("userName"));
    }

    @Test
    public void decreaseBalanceByTest() throws InvalidInputException {
        BuyerAccount buyerAccount = new BuyerAccount("userName", "firstName", "last Name",
                "sampleEmail@sample.sample", "09001112233", "password123",100);

        buyerAccount.decreaseBalanceBy(20);
        Assert.assertEquals(80,buyerAccount.getBalance(), 0);
    }

    //TODO : write test for : addCartsProductsToBoughtProducts, viewOrders, emptyCart, addLog, removeDiscountCode, addDiscountCod, getLogWithId, hasBuyerBoughtProduct

    @Test
    public void viewBalanceTest() throws InvalidInputException {
        BuyerAccount buyerAccount = new BuyerAccount("userName", "firstName", "last Name",
                "sampleEmail@sample.sample", "09001112233", "password123", 100);

        Assert.assertEquals("balance : 100.0\n",buyerAccount.viewBalance());
    }
}