package hr.fer.zemris.java.tecaj.hw07.commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import hr.fer.zemris.java.tecaj.hw07.environment.Environment;
import hr.fer.zemris.java.tecaj.hw07.shell.ShellStatus;

/**
 * Class represents a command that prints basic informations about a certain command if it 
 * is given as argument or for all commands if no arguments were provided.
 * @author Vilim Staroveški
 *
 */
public class HelpShellCommand implements ShellCommand {

	/**
	 * Command name
	 */
	private String name;
	/**
	 * Description
	 */
	private List<String> description;
	/**
	 * Creates new {@link HelpShellCommand}
	 */
	public HelpShellCommand() {

		name = "help";
		description = new ArrayList<String>();
		description.add("Prints basic informations about a certain command if it "
				+ "is given as argument or for all commands if no arguments were provided.");
		
	}
	
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {

		Optional<String> optionalArg;
		try {
			
			optionalArg = Optional.ofNullable(CommandsUtility.giveMeOneArgument(arguments, CommandType.HELP));
			
		} catch(IllegalArgumentException e) {
			env.errWriteLn(e.getMessage());
			return ShellStatus.CONTINUE;
		}
		
		Iterable<ShellCommand> commands = env.commands();
		
		List<ShellCommand> allCommands = new ArrayList<ShellCommand>();
		for(ShellCommand com : commands) {
			allCommands.add(com);
		}
		
		List<ShellCommand> listOfFoundCommands = allCommands.stream().filter(new Predicate<ShellCommand>() {

			@Override
			public boolean test(ShellCommand t) {
				if(optionalArg.isPresent()) {
					return optionalArg.get().equalsIgnoreCase(t.getCommandName());
				}
				else {
					return true;
				}
			}
		}).collect(Collectors.toList());
		
		if(listOfFoundCommands.isEmpty()) {
			
			env.errWriteLn("Command \""+ optionalArg.get()+"\" was not found!");
			return ShellStatus.CONTINUE;
		}
		else {
			
			for(ShellCommand command : listOfFoundCommands) {
				try {
					env.write("["+command.getCommandName()+"]" + " ... ");
					for(String descriptionLine : command.getCommandDescription()) {
							env.write(descriptionLine+" ");
					}
					env.writeln("");
				} catch(IOException e) {
				}
			}
		}
	
		return ShellStatus.CONTINUE;
	}

	@Override
	public String getCommandName() {

		return name;
	}

	@Override
	public List<String> getCommandDescription() {

		return description;
	}

}
