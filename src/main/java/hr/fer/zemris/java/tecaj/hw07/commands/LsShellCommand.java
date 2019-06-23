package hr.fer.zemris.java.tecaj.hw07.commands;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import hr.fer.zemris.java.tecaj.hw07.environment.Environment;
import hr.fer.zemris.java.tecaj.hw07.shell.ShellStatus;

/**
 * Command that writes a directory listing
 * @author Vilim Staroveški
 *
 */
public class LsShellCommand implements ShellCommand {

	/**
	 * Command name
	 */
	private String name;
	
	/**
	 * Description.
	 */
	private List<String> description;
	
	/**
	 * Creates new {@link LsShellCommand}
	 */
	public LsShellCommand() {

		name = "ls";
		description = new ArrayList<String>();
		description.add("Takes a single argument – directory – and writes a directory listing");
		description.add("Example of usage: \'ls \"C:\\\"\'");
	}
	
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		
		Optional<String> optionalArg;
		try {
			
			optionalArg = Optional.ofNullable(CommandsUtility.giveMeOneArgument(arguments, CommandType.LS));
			
		} catch(IllegalArgumentException e) {
			env.errWriteLn(e.getMessage());
			return ShellStatus.CONTINUE;
		}
		
		if(!optionalArg.isPresent()) {
			env.errWriteLn("Ls command expects one argument!");
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
		
		List<Path> listingOfPath = null;
		try {
			
			listingOfPath = Files.list(pathToDir)
								 .collect(Collectors.toList());
			
		} catch (IOException e) {
			env.errWriteLn("There was an IOException in listing directory:"+pathToDir.toString());
			return ShellStatus.CONTINUE;
		}
		
		for(Path element : listingOfPath) {
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			BasicFileAttributeView view = Files.getFileAttributeView(
					element, BasicFileAttributeView.class, LinkOption.NOFOLLOW_LINKS
					);
			BasicFileAttributes attributes = null;
			try {
				
				attributes = view.readAttributes();
		
			} catch (IOException e) {
				env.errWriteLn("Error in reading attributes for: "+element.getFileName());
				continue;
			}
			FileTime fileTime = attributes.creationTime();
			String formattedDateTime = sdf.format(new Date(fileTime.toMillis()));
			try {
				System.out.format( 	(Files.isDirectory(element) ? "d" : "-") +
									(Files.isReadable(element) ? "r" : "-") +
									(Files.isWritable(element) ? "w" : "-") +
									(Files.isExecutable(element) ? "x" : "-")
								);
				long size = attributes.size();
				System.out.format("%15d ", size);
				env.write(formattedDateTime);
				System.out.format(" %-1s", element.getFileName());
				env.writeln("");
			} catch (IOException e) {
				System.err.println("There was an error in writing to output!");
				return ShellStatus.CONTINUE;
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
