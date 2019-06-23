package hr.fer.zemris.java.tecaj.hw07.commands;

import java.util.List;

import hr.fer.zemris.java.tecaj.hw07.environment.Environment;
import hr.fer.zemris.java.tecaj.hw07.shell.MyShell;
import hr.fer.zemris.java.tecaj.hw07.shell.ShellStatus;

/**
 * Interface for commands made for {@link MyShell}.
 * @author Vilim Staroveški
 *
 */
public interface ShellCommand {

	/**
	 * Executes command.
	 * @param env envrionment in which it is executed.
	 * @param arguments arguments for command.
	 * @return status.
	 */
	ShellStatus executeCommand(Environment env, String arguments);
	/**
	 * Returns command name.
	 * @return command name.
	 */
	String getCommandName();
	/**
	 * Returns command description
	 * @return command description
	 */
	List<String> getCommandDescription();
}
