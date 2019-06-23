package hr.fer.zemris.java.tecaj.hw07.commands;

import hr.fer.zemris.java.tecaj.hw07.environment.Environment;
import hr.fer.zemris.java.tecaj.hw07.shell.ShellStatus;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class represents a command that copies a file to location.It expects two arguments: source file 
 * name and destination file name (i.e. paths and names)
 * @author Vilim Staroveški
 *
 */
public class CopyShellCommand implements ShellCommand {
	
	/**
	 * Enumeration thath tells what type is destination where we copy.
	 * @author Vilim Staroveški
	 *
	 */
	private static enum DestinationType {
		EXISTING_FILE, NEW_FILE, DIRECTORY;
	}

	/**
	 * Command name
	 */
	private String name;
	
	/**
	 * Description
	 */
	private List<String> description;
	
	/**
	 * Creates new {@link CopyShellCommand}
	 */
	public CopyShellCommand() {

		name = "copy";
		description = new ArrayList<String>();
		description.add("Command expects two arguments: source file name and destination file name (i.e. paths and "+
					"names).");
		description.add("If destination file exists, it will ask You if it is allowed to overwrite.");
		description.add("Example of usage: \'copy \"C:\\test.txt\"  \"C:\\copy\\copy.txt\" \'");
	}
	
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {

		try {
			
			arguments = CommandsUtility.giveMeTwoArgument(arguments, CommandType.COPY);
			
		} catch(IllegalArgumentException e) {
			env.errWriteLn(e.getMessage());
			return ShellStatus.CONTINUE;
		}
		String[] srcAndDest = arguments.split("\" \"");
		
		//checking source and setting input stream
		Path pathToSource = CommandsUtility.parsePath(env, srcAndDest[0]);
		if(pathToSource == null ) {
			env.errWriteLn("Source must be existing file!");
			return ShellStatus.CONTINUE;
		}
		if(Files.isDirectory(pathToSource)) {
			env.errWriteLn("Source must be a file!");
			return ShellStatus.CONTINUE;
		}
		if(!Files.isReadable(pathToSource)) {
			env.errWriteLn("Source file is not readable!");
			return ShellStatus.CONTINUE;
		}
		InputStream is = null;
		try {
			
			 is =  Files.newInputStream(pathToSource, StandardOpenOption.READ);
			
		} catch (IOException e) {
			env.errWriteLn("There was an IOException in opening source file!");
			return ShellStatus.CONTINUE;
		}
		
		//checking destination
		Path pathToDest = CommandsUtility.parseNullablePath(env, srcAndDest[1]);
		if(pathToDest == null ) {
			return ShellStatus.CONTINUE;
		}
		DestinationType destType = null;
		if(Files.isDirectory(pathToDest)) {
			destType = DestinationType.DIRECTORY;
		}
		else if(Files.notExists(pathToDest)) {
			destType = DestinationType.NEW_FILE;
		}
		else if(Files.isWritable(pathToDest)) {
			destType = DestinationType.EXISTING_FILE;
		}
		else {
			env.errWriteLn("Destination is a file that is not writable!");
			return ShellStatus.CONTINUE;
		}
		
		switch(destType) {
		
		case EXISTING_FILE:			try {
										if(Files.isSameFile(pathToSource, pathToDest)) {
											env.errWriteLn("It is the same document, copy wont be executed.");
											return ShellStatus.CONTINUE;
										}
										createNewFile(is, pathToSource, 
												Files.newOutputStream(pathToDest, StandardOpenOption.WRITE), pathToDest, 
												env, true);
										
									} catch (IOException e) {
										env.errWriteLn("There was an error in opening a output file!"+e.getMessage());
									}
									break;
		
		case DIRECTORY:				pathToDest = Paths.get(pathToDest.toString(), pathToSource.getFileName().toString());
									try {
										boolean needsOverwriting = false;
										List<Path> listingOfPath = null;
										try {
											
											listingOfPath = Files.list(pathToDest.getParent())
																 .collect(Collectors.toList());
											
										} catch (IOException e) {
											env.errWriteLn("There was an IOException in listing directory:"+pathToDest.toString());
											return ShellStatus.CONTINUE;
										}
										
										for(Path element : listingOfPath) {
											
											if(element.getFileName().toString().equals(pathToDest.getFileName().toString())) {
												needsOverwriting = true;
												break;
											}
										}
										if(Files.isSameFile(pathToSource, pathToDest)) {
											env.errWriteLn("It is the same document, copy wont be executed.");
											return ShellStatus.CONTINUE;
										}
										createNewFile(is, pathToSource, 
												Files.newOutputStream(pathToDest, StandardOpenOption.CREATE), pathToDest, 
												env, needsOverwriting);
										
									} catch (IOException e) {
										env.errWriteLn("There was an error in opening a output file!"+e.getMessage());
										e.printStackTrace();
									}
									break;
		
		case NEW_FILE:				try {
			
										createNewFile(is, pathToSource, 
													Files.newOutputStream(pathToDest, StandardOpenOption.CREATE), pathToDest, 
													env, false);
										
									} catch (IOException e) {
										env.errWriteLn("There was an error in opening a output file!"+e.getMessage());
									}
									break;
									
		default: 					break;		
		}
		
		return ShellStatus.CONTINUE;
	}

	private void createNewFile(InputStream is, Path pathToSource, OutputStream os, Path pathToDest, Environment env, boolean overwrite) {
		
		Path pathBackUpCopy = null;
		if(overwrite) {
			try {
				
				env.writeln("There is already a file with the same "
						+ "name in this location. Do you want to overwrite it? Y/N");
				String answer = env.readLine();
				if(answer.equalsIgnoreCase("Y")) {

					pathBackUpCopy = Files.copy(pathToSource, pathToSource.getParent().resolve("tempForCopies"));
					is = Files.newInputStream(pathBackUpCopy, StandardOpenOption.READ);
				}
				else if(answer.equalsIgnoreCase("N")) {
					env.writeln("Command wont be executed.");
					return;
				}
				else {
					env.errWriteLn("Not understandable answer. Command wont be executed.");
					return;
				}
			} catch (IOException e) {
				env.errWriteLn("There was an error in writing to output with env!");
			}
					
		}
		
		byte[] buffer = new byte[4*1024];
		int r = 0;
		while(true) {
			
			try {
				r = is.read(buffer);
			} catch (IOException e) {
				env.errWriteLn("There was an error in creating the file! File may be corrupted.");
				if(pathBackUpCopy != null) {
					restoreOld(pathBackUpCopy,  env);
				}
			}
			if(r < 1) {
				break;
			}
			try {
				os.write(buffer);
			} catch (IOException e) {
				env.errWriteLn("There was an error in creating the file! File may be corrupted.");
				if(pathBackUpCopy != null) {
					restoreOld(pathBackUpCopy,  env);
				}
			}
		}
		if(overwrite && Files.exists(pathBackUpCopy)) {
			try {
				is = null;
				Files.delete(pathBackUpCopy);
			} catch (IOException e) {
				env.errWriteLn("Deleting of old file have been interupted!");
				return;
			}
		}
	}

	private void restoreOld(Path pathBackUpCopy, Environment env) {
		
		try {
			Files.copy(pathBackUpCopy, pathBackUpCopy.getParent(), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			env.errWriteLn("There was an error in restoring the original file! It may be corrupted now.");
		}		
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
