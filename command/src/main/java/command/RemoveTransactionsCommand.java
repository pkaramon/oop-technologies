package command;

import model.Account;
import model.Transaction;

import java.util.List;

public class RemoveTransactionsCommand implements Command {
    private final Account account;

    private final List<Transaction> toDelete;

    public RemoveTransactionsCommand(Account account, List<Transaction> toDelete) {
        this.account = account;
        this.toDelete = toDelete;
    }


    @Override
    public void execute() {
        for (Transaction transaction : toDelete) {
            account.removeTransaction(transaction);
        }
    }

    @Override
    public void undo() {
        for (Transaction transaction : toDelete) {
            account.addTransaction(transaction);
        }
    }

    @Override
    public void redo() {
        execute();
    }

    @Override
    public String getName() {
        return "Remove transactions" +
                toDelete.stream().map(Transaction::toString).reduce("", (a, b) -> a + ", " + b);

    }
}
