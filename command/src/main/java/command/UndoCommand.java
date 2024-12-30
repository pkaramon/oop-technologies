package command;

import java.util.List;

public class UndoCommand implements Command {
    private List<Command> commands;

    public UndoCommand(List<Command> commands) {
        this.commands = commands;
    }

    @Override
    public void execute() {
        // Do nothing
    }


    @Override
    public void undo() {
        for (int i = commands.size() - 1; i >= 0; i--) {
            commands.get(i).redo();
        }
    }

    @Override
    public void redo() {
        for (int i = commands.size() - 1; i >= 0; i--) {
            commands.get(i).undo();
        }
    }

    @Override
    public String getName() {
        return "";
    }
}
