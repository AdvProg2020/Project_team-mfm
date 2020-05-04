package Main.model.sorting;

import Main.model.Product;
import Main.model.accounts.Account;

import java.util.Comparator;

public abstract class UsersSort implements Comparator {
    static class productSortByFirstNameAscending implements Comparator<Account> {
        public int compare(Account one, Account two) {
            return one.getFirstName().compareToIgnoreCase(two.getFirstName());
        }
    }

    static class productSortByLastNameAscending implements Comparator<Account> {
        public int compare(Account one, Account two) {
            return one.getLastName().compareToIgnoreCase(two.getLastName());
        }
    }

    static class productSortByFirstNameDescending implements Comparator<Account> {
        public int compare(Account one, Account two) {
            return (-1)*(one.getFirstName().compareToIgnoreCase(two.getFirstName()));
        }
    }

    static class productSortByLastNameDescending implements Comparator<Account> {
        public int compare(Account one, Account two) {
            return (-1)*(one.getLastName().compareToIgnoreCase(two.getLastName()));
        }
    }

    static class productSortByType implements Comparator<Account> {
        public int compare(Account one, Account two) {
            if (one.getType().compareToIgnoreCase(two.getType()) == 0)
                return one.getFirstName().compareToIgnoreCase(two.getFirstName());
            else
                return one.getType().compareToIgnoreCase(two.getType());
        }
    }
}