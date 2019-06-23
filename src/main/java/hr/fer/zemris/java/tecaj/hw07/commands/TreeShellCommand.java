package hr.fer.zemris.java.tecaj.hw07.commands;

import hr.fer.zemris.java.tecaj.hw07.environment.Environment;
import hr.fer.zemris.java.tecaj.hw07.shell.ShellStatus;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Command that expects a single argument: directory name and prints a tree representation 
 * of this directory to output.
 * @author Vilim Staroveški
 *
 */
public class TreeShellCommand implements ShellCommand {
	
	/**
	 * Private static class that implements {@link FileVisitor} interface.
	 * @author Vilim Staroveški
	 *
	 */
	private static class Tree implements FileVisitor<Path> {
		
		/**
		 * String that contains tree.
		 */
		private String print = "";

		/**
		 * Level where we are.
		 */
		private int indentLevel = 0;
		
		@Override
		public FileVisitResult preVisitDirectory(Path dir,
				BasicFileAttributes attrs) throws IOException {
			
			if(indentLevel == 0) {
				print += dir+"\n";
			}
			else {
				
				print += String.format("%"+indentLevel+"s%s%n", "", dir.getFileName());
			}
			indentLevel += 2;
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
				throws IOException {
			
			print += String.format("%"+indentLevel+"s%s%n", "", file.getFileName());
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult visitFileFailed(Path file, IOException exc)
				throws IOException {
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult postVisitDirectory(Path dir, IOException exc)
				throws IOException {
			indentLevel -= 2;
			return FileVisitResult.CONTINUE;
		}
		
		public String getPrint() {
			return print;
		}
		
	}
	/**
	 * command name
	 */
	private String name;
	/**
	 * description
	 */
	private List<String> description;
	/**
	 * creates new {@link TreeShellCommand}
	 */
	public TreeShellCommand() {
		
		name = "tree";
		description = new ArrayList<String>();
		description.add("Command expects a single argument: directory name and prints a tree representation "
				+ "of this directory to output.");
		description.add("Example of usage: \'tree \"C:\\\"");
	}

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {

		Optional<String> optionalArg;
		try {
			
			optionalArg = Optional.ofNullable(CommandsUtility.giveMeOneArgument(arguments, CommandType.TREE));
			
		} catch(IllegalArgumentException e) {
			env.errWriteLn(e.getMessage());
			return ShellStatus.CONTINUE;
		}
		
		if(!optionalArg.isPresent()) {
			env.errWriteLn("Tree command expects one argument!");
			return ShellStatus.CONTINUE;
		}
		
		Path pathToDir = CommandsUtility.parsePath(env, optionalArg.get());
		if(pathToDir == null ) {
			return ShellStatus.CONTINUE;
		}
		
		if(!Files.isDirectory(pathToDir)) {
			env.errWriteLn("Given path is not a directory!");
			return ShellStatus.CONTINUE;
		}

		Tree tree = null;
		try {
			tree = new Tree();
			Files.walkFileTree(pathToDir, tree);
		} catch (IOException e) {
			env.errWriteLn("There wan an error in writing the tree!");
			return ShellStatus.CONTINUE;
		}
		try {
			env.write(tree.getPrint());
		} catch (IOException e) {
			env.errWriteLn("There was an IO error in printing to output.");
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
