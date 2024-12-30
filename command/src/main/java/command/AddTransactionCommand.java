package command;

import model.Account;
import model.Transaction;

public class AddTransactionCommand implements Command {
    private final Transaction transactionToAdd;
    private final Account account;

    public AddTransactionCommand(Account account, Transaction transactionToAdd) {
        this.account = account;
        this.transactionToAdd = transactionToAdd;
    }


    @Override
    public void execute() {
        account.addTransaction(transactionToAdd);
    }

    @Override
    public void undo() {
        account.removeTransaction(transactionToAdd);
    }

    @Override
    public void redo() {
        execute();
    }

    @Override
    public String getName() {
        return "New transaction: " + transactionToAdd.toString();
    }
}
