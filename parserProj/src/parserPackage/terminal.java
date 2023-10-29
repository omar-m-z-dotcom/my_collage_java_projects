package parserPackage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Pattern;

class Parser {
	void parser() {
		
	}

	public static String commandName;
	public static String[] args;
	public static int redircommand = -1;
	public static Boolean redirMode = false;
	public static String RedircommandName;
	public static Boolean redir_exist = false;
	public static Boolean Parse_sucsses = false;

//This method will divide the input into commandName and args
//where "input" is the string command entered by the user
	public static boolean parse(String input) {
		Parse_sucsses = getCommandName(input);
		return Parse_sucsses;

	}

	public static boolean getCommandName(String input) {
		String[] inputParts = input.split(" ");
		int inputPartsNum = inputParts.length;
		if (inputPartsNum < 1) {
			return false;
		} else if (inputParts[0].equalsIgnoreCase("echo") || inputParts[0].equalsIgnoreCase("pwd")
				|| inputParts[0].equalsIgnoreCase("cd") || inputParts[0].equalsIgnoreCase("ls")
				|| inputParts[0].equalsIgnoreCase("mkdir") || inputParts[0].equalsIgnoreCase("rmdir")
				|| inputParts[0].equalsIgnoreCase("cat") || inputParts[0].equalsIgnoreCase("rm")) {
			commandName = inputParts[0];
			if (Arrays.asList(inputParts).contains(">") || Arrays.asList(inputParts).contains(">>")) {
				for (int i = 0; i < inputParts.length; i++) {
					if (inputParts[i].equalsIgnoreCase(">") || inputParts[i].equalsIgnoreCase(">>")) {
						redircommand = i;
						RedircommandName = inputParts[i];
						redir_exist = true;
						return true;
					}
				}
			}
			return true;
		} else {
			return false;
		}
	}

	public static String[] getArgs(String input) {
		if (redirMode == false) {
			String[] inputParts = input.split(" ");
			int inputPartsNum = inputParts.length;
			if (inputPartsNum == 1) {
				args = new String[] { "" };
				if (redircommand == -1) {
					return null;
				} else {
					redirMode = true;
					return null;
				}
			} else {
				args = new String[inputPartsNum - 1];
				if (redircommand == -1) {
					for (int i = 1, j = 0; i < inputPartsNum; i++, j++) {
						args[j] = inputParts[i];
					}
					if (redircommand == -1) {
						return null;
					} else {
						redirMode = true;
						return null;
					}
				} else {
					for (int i = 1, j = 0; i < redircommand; i++, j++) {
						args[j] = inputParts[i];
					}
					if (redircommand == -1) {
						return null;
					} else {
						redirMode = true;
						return null;
					}
				}
			}
		} else {
			int start_index = redircommand;
			String[] inputParts = input.split(" ");
			int inputPartsNum = inputParts.length;
			if (start_index == inputPartsNum - 1) {
				args = new String[] { "" };
				return null;
			} else {
				args = new String[(inputPartsNum - start_index) - 1];
				for (int i = start_index + 1, j = 0; i < inputParts.length; i++) {
					args[j] = inputParts[i];
				}
				return null;
			}

		}
	}
}

public class terminal {
	public static Boolean isFailed = false;
	static Parser parser = new Parser();
	
	//file c:\einfinfe
	//File file =File(c:\einfinfe)
	//return file

	public static File translat_to_file(String arg) {
		String Dotpart;
		String noDotpart;
		File file;
		if (!arg.contains("\\")) {
			return null;
		}
		if (arg.charAt(0) == '.' & arg.length() == 1) {

			return null;
		} else if (arg.charAt(0) == '.' & arg.charAt(1) != '.' & arg.length() != 1) {
			Dotpart = translatedots(arg.substring(0, 1));
			noDotpart = get_after_dot(arg);
			file = new File(Dotpart + noDotpart);
			return file;
		} else if (arg.charAt(0) == '.' & arg.charAt(1) == '.' & arg.length() == 2) {

			return null;

		} else if (arg.charAt(0) == '.' & arg.charAt(1) == '.' & arg.length() != 2) {
			Dotpart = translatedots(arg.substring(0, 2));
			noDotpart = get_after_dot(arg);
			file = new File(Dotpart + noDotpart);
			return file;
		} else {
			file = new File(arg);
			return file;
		}
	}
//..\onidn\fjknfnmrfv.txt
	// passes ..
	public static String translatedots(String arg) { // translates the dots to full path directory
		if (arg.equalsIgnoreCase(".")) {
			return System.getProperty("user.dir");
		} else {
			int lastBackSlash = System.getProperty("user.dir").lastIndexOf('\\');
			return System.getProperty("user.dir").substring(0, lastBackSlash);
		}
	}
//..\onidn\fjknfnmrfv.txt
	public static String get_after_dot(String arg) {// gets what is after the dots
		int index = 0;
		if (arg.charAt(0) == '.') {
			index++;
		}
		if (arg.charAt(1) == '.') {
			index++;
		}
		return arg.substring(index, arg.length());

	}

///////////////////////////////////////////////////////////////////////////////
	
	public static String[] echo(String[] args) {
		if (args.length == 1) {
			System.out.println(args[0]);
			return args;
		} else {
			isFailed = true;
			return new String[] {};

		}
	}

	public static String[] pwd() {
		System.out.println(System.getProperty("user.dir"));
		return new String[] { System.getProperty("user.dir") };
	}
//./dsuifheiu   //..fweferf  /C:\fneoriwnf.txt
	public static boolean touch(String[] args) {
		if (args.length == 1) {
			String DotPart;
			String noDorPart;
			File file;
			if (args[0].equalsIgnoreCase("")) {
				System.out.println("command not found or invalid params are enterd");
				isFailed = true;
				return false;
			}
			if (!args[0].contains("\\")) {
				System.out.println("command not found or invalid params are enterd");
				isFailed = true;
				return false;
			} else if (args[0].charAt(0) == '.' & args[0].length() == 1) {
				System.out.println("command not found or invalid params are enterd");
				isFailed = true;
				return false;
			} else if (args[0].charAt(0) == '.' & args[0].charAt(1) != '.' & args[0].length() != 1) {
				DotPart = translatedots(args[0].substring(0, 1));
				noDorPart = get_after_dot(args[0]);
				file = new File(DotPart + noDorPart);
				try {
					boolean flag = file.createNewFile();
					if (flag == true) {
						return true;
					} else {
						System.out.println("command not found or invalid params are enterd");
						isFailed = true;
						return false;
					}
				} catch (IOException e) {
					System.out.println("command not found or invalid params are enterd");
					isFailed = true;
					return false;
				}
			} else if (args[0].charAt(0) == '.' & args[0].charAt(1) == '.' & args[0].length() == 2) {
				System.out.println("command not found or invalid params are enterd");
				isFailed = true;
				return false;
			} else if (args[0].charAt(0) == '.' & args[0].charAt(1) == '.' & args[0].length() != 2) {
				DotPart = translatedots(args[0].substring(0, 2));
				noDorPart = get_after_dot(args[0]);
				file = new File(DotPart + noDorPart);
				try {
					boolean flag = file.createNewFile();
					if (flag == true) {
						return true;
					} else {
						System.out.println("command not found or invalid params are enterd");
						isFailed = true;
						return false;
					}
				} catch (IOException e) {
					isFailed = true;
					System.out.println("command not found or invalid params are enterd");
					return false;
				}
			} else {//full path
				file = new File(args[0]);
				try {
					boolean flag = file.createNewFile();
					if (flag == true) {
						return true;
					} else {
						isFailed = true;
						System.out.println("command not found or invalid params are enterd");
						return false;
					}
				} catch (IOException e) {
					isFailed = true;
					System.out.println("command not found or invalid params are enterd");
					return false;
				}
			}
		} else {
			isFailed = true;
			System.out.println("command not found or invalid params are enterd");
			return false;
		}
	}
///////////////////////////////////////////////////////////////////////

	public static void cd(String[] args) {
		if (args.length == 1) {
			// This command changes the current directory .
			try {// relative dir case
				File file = null;
				String DotPart;
				String noDorPart;
				if (!args[0].contains("\\") & !args[0].equalsIgnoreCase("")) {
					isFailed = true;
					System.out.println("command not found or invalid params are enterd");
					return;
				} else if (args[0] == null) {// no input case
					String userHomeDir = System.getProperty("user.home");
					System.setProperty("user.dir", userHomeDir);
					return;
				} else if (args[0].charAt(0) == '.' & args[0].length() == 1) {// single dot case
					return;
				} else if (args[0].charAt(0) == '.' & args[0].charAt(1) != '.' & args[0].length() != 1) {// single dot
																											// with
																											// the rest
																											// of
									//. present folder .. privues folder																		// the
																											// directory
										//./feijvui/																	// path case
					DotPart = translatedots(args[0].substring(0, 1));
					noDorPart = get_after_dot(args[0]);
					file = new File(DotPart + noDorPart);
					if (file.isDirectory() & file.exists()) {
						System.setProperty("user.dir", file.getPath());
						return;
					} else {
						isFailed = true;
						System.out.println("command not found or invalid params are enterd");
						return;
					}
				} else if (args[0].charAt(0) == '.' & args[0].charAt(1) == '.' & args[0].length() == 2) {// double dots
																											// case
					DotPart = translatedots(args[0].substring(0, 2));
					file = new File(DotPart);
					System.setProperty("user.dir", file.getPath());
					return;
				} else if (args[0].charAt(0) == '.' & args[0].charAt(1) == '.' & args[0].length() != 2) {// double dots
																											// with
																											// the rest
												//../hgyugf/iugyiiyg															// the
																											// directory
					// args[0] =. present folder 		args[0] =.. //privous path																				// path case
					DotPart = translatedots(args[0].substring(0, 2));
					noDorPart = get_after_dot(args[0]);
					file = new File(DotPart + noDorPart);
					if (file.isDirectory() & file.exists()) {
						System.setProperty("user.dir", file.getPath());
						return;
					} else {
						isFailed = true;
						System.out.println("command not found or invalid params are enterd");
						return;
					}
				} else {//full path
					file = new File(args[0]);
					if (file.isDirectory() & file.exists()) {
						System.setProperty("user.dir", file.getPath());
						return;
					} else {
						isFailed = true;
						System.out.println("command not found or invalid params are enterd");
						return;
					}
				}

			} catch (Exception e) {
				isFailed = true;
				System.out.println("command not found or invalid params are enterd");
				return;
			}
		} else {
			isFailed = true;
			System.out.println("command not found or invalid params are enterd");
			return;
		}
	}

////////////////////////////////////////////////////////////////////////////////
	public static File[] ls(String option)// These list each given file or directory name.
	{
		File result[] = null;
		String currentDirectory = System.getProperty("user.dir");// gets the current working directory note:
		// must be written this way
		try {
			File folder = new File(currentDirectory);

			File[] fileList;//to store the files and directories that are in the folder we are in
// omar 
			//amr
			//amr omar
			//omar amr
			fileList = folder.listFiles();
			if (option == null) {
				Arrays.sort(fileList, (a, b) -> a.getName().compareTo(b.getName()));// sorts alphabetically
			}

			else if (Pattern.matches("-[r]", option)) {
				Arrays.sort(fileList, (a, b) -> -a.getName().compareTo(b.getName()));// sorts in reverse to alphabetical
			} else {
				isFailed = true;
				System.out.println("command not found or invalid params are enterd");
				return new File[] {};
			}

			// Lists items in folder

			for (File file : fileList) {

				System.out.print(file.getName() + " ");
			}
			result = new File[fileList.length];
			int i = 0;
			for (File file : fileList) {
				result[i] = new File(currentDirectory + "\\" + file.getPath());
				i++;
			}
		} catch (Exception e) {
			isFailed = true;
			System.out.println("command not found or invalid params are enterd");
			return new File[] {};
		}

		return result;
	}

///////////////////////////////////////////////////////////////////////////////boolean flag = dir.mkdir();
	// !(string.contains("\\"))

	public static void mkdir(String[] args)// mkdir creates a directory with each given name.
	{
		if (args.length > 0) {
			try {
				String mkdir_arg = null;
				String currentDir = System.getProperty("user.dir");
				String DotPart;
				String noDorPart;
				for (String string : args) {
					//makdir
					if (string.equalsIgnoreCase("")) {
						isFailed = true;
						System.out.println("command not found or invalid params are enterd");
						return;
					}//iygyugfuyf
					if ((string.contains("\\"))) {
						File file = new File(currentDir + "\\" + string);
						file.mkdir();// returns true if directory was successfully created
						return;
						//mkdir .
					} else if (string.charAt(0) == '.' & string.length() == 1) {
						isFailed = true;
						System.out.println("command not found or invalid params are enterd");
						return;
					} else if (string.charAt(0) == '.' & string.charAt(1) != '.' & string.length() != 1) {
						DotPart = translatedots(string.substring(0, 1));
						noDorPart = get_after_dot(string);
						mkdir_arg = DotPart + noDorPart;
						File file = new File(mkdir_arg);
						boolean flag = file.mkdir();
						if (flag == false) {
							isFailed = true;
							System.out.println("command not found or invalid params are enterd");
							return;
						}
					} else if (string.charAt(0) == '.' & string.charAt(1) == '.' & string.length() == 2) {
						isFailed = true;
						System.out.println("command not found or invalid params are enterd");
						return;
					} else if (string.charAt(0) == '.' & string.charAt(1) == '.' & string.length() != 2) {
						DotPart = translatedots(string.substring(0, 2));
						noDorPart = get_after_dot(string);
						mkdir_arg = DotPart + noDorPart;
						File file = new File(mkdir_arg);
						boolean flag = file.mkdir();
						if (flag == false) {
							isFailed = true;
							System.out.println("command not found or invalid params are enterd");
							return;
						}
					} else {
						File file = new File(args[0]);
						boolean flag = file.mkdir();
						if (flag == false) {
							isFailed = true;
							System.out.println("command not found or invalid params are enterd");
							return;
						}
					}
				}

			} catch (Exception e) {
				isFailed = true;
				System.out.println("command not found or invalid params are enterd");
				return;
			}
		} else {
			isFailed = true;
			System.out.println("command not found or invalid params are enterd");
			return;
		}
	}

///////////////////////////////////////////////////////////////////////////////if (cheakfile.length() > 0) toBeDeleted.delete();
	public static void Rmdir(String[] args) {
		if (args.length == 1) {
			try {
				String currentDirectory = System.getProperty("user.dir");
				if (args[0].equalsIgnoreCase("*")) {
					File[] files = ls(null);
					for (File file : files) {
						if (file.isDirectory() & !(file.length() > 0)) {
							file.delete();
						} else {
							continue;
						}
					}
					return;
					//rmdir erijirrjornjr
				} else if ((args[0].contains("\\"))) {
					isFailed = true;
					System.out.println("command not found or invalid params are enterd");
					return;
					//rmdir .
				} else if (args[0].charAt(0) == '.' & args[0].length() == 1) {
					File file = new File(currentDirectory);
					if (file.length() > 0) {
						isFailed = true;
						System.out.println("command not found or invalid params are enterd");
						return;
					} else {
						String filepath = file.getAbsolutePath();
						cd(new String[] { ".." });
						file = new File(filepath);
						file.delete();
						return;
					}//rmdir .\fjeoirjfferioj
				} else if (args[0].charAt(0) == '.' & args[0].charAt(1) != '.' & args[0].length() != 1) {
					String DotPart = translatedots(args[0].substring(0, 1));
					String noDotPart = get_after_dot(args[0]);
					File file = new File(DotPart + noDotPart);
					if (file.isDirectory() & file.exists() & !(file.length() > 0)) {
						file.delete();
						return;
					} else {
						isFailed = true;
						System.out.println("command not found or invalid params are enterd");
						return;
					}//rmdir ..
				} else if (args[0].charAt(0) == '.' & args[0].charAt(1) == '.' & args[0].length() == 2) {
					isFailed = true;
					System.out.println("command not found or invalid params are enterd");
					return;
					//rmdir ../ghyigfyu/ijgiy
				} else if (args[0].charAt(0) == '.' & args[0].charAt(1) == '.' & args[0].length() != 2) {
					String DotPart = translatedots(args[0].substring(0, 2));
					String noDotPart = get_after_dot(args[0]);
					File file = new File(DotPart + noDotPart);
					if (file.isDirectory() & file.exists() & !(file.length() > 0)) {
						file.delete();
						return;
					} else {
						isFailed = true;
						System.out.println("command not found or invalid params are enterd");
						return;
					}
				} else {//full path
					File file = new File(args[0]);
					if (file.isDirectory() & file.exists() & !(file.length() > 0)) {
						file.delete();
						return;
					} else {
						isFailed = true;
						System.out.println("command not found or invalid params are enterd");
						return;
					}
				}

			} catch (Exception e) {
				isFailed = true;
				System.out.println("command not found or invalid params are enterd");
			}
		} else {
			isFailed = true;
			System.out.println("command not found or invalid params are enterd");
			return;
		}
	}

///////////////////////////////////////////////////////////////////////
	
	public static String[] cat(String args[]) {
		if (args.length == 1 || args.length == 2) {
			String[] result = { "", "" };
			try {
				int i = 0;
				for (String string : args) {
					//jinviuvrfv
					if (!(args[0].contains("\\"))) {
						isFailed = true;
						System.out.println("command not found or invalid params are enterd");
						return new String[] {};
						//.
					} else if (string.charAt(0) == '.' & string.length() == 1) {
						isFailed = true;
						System.out.println("command not found or invalid params are enterd");
						return new String[] {};
						//./erjnfr.txt
					} else if (string.charAt(0) == '.' & string.charAt(1) != '.' & string.length() != 1) {
						String DotPart = translatedots(args[0].substring(0, 1));
						String noDotPart = get_after_dot(args[0]);
						File file = new File(DotPart + noDotPart);
						if (file.isFile() & file.exists()) {
							Scanner myReader = new Scanner(file);
							String data = "";
							while (myReader.hasNextLine()) {
								data += myReader.nextLine();
							}
							result[i] = data;
							i++;
							System.out.println(data);
							myReader.close();
							return result;
						} else {
							isFailed = true;
							System.out.println("command not found or invalid params are enterd");
							return new String[] {};
						}//..
					} else if (string.charAt(0) == '.' & string.charAt(1) == '.' & string.length() == 2) {
						isFailed = true;
						System.out.println("command not found or invalid params are enterd");
						return new String[] {};
						//../vreioveri.txt
					} else if (string.charAt(0) == '.' & string.charAt(1) == '.' & string.length() != 2) {
						String DotPart = translatedots(string.substring(0, 2));
						String noDotPart = get_after_dot(string);
						File file = new File(DotPart + noDotPart);
						if (file.isFile() & file.exists()) {
							Scanner myReader = new Scanner(file);
							String data = "";
							while (myReader.hasNextLine()) {
								data += myReader.nextLine();
							}
							result[i] = data;
							i++;
							System.out.println(data);
							myReader.close();
							return result;
						} else {
							isFailed = true;
							System.out.println("command not found or invalid params are enterd");
							return new String[] {};
						}
					} else {//full path
						File file = new File(string);
						if (file.isFile() & file.exists()) {
							Scanner myReader = new Scanner(file);
							String data = "";
							while (myReader.hasNextLine()) {
								data += myReader.nextLine();
							}
							result[i] = data;
							i++;
							System.out.println(data);
							myReader.close();
							return result;
						} else {
							isFailed = true;
							System.out.println("command not found or invalid params are enterd");
							return new String[] {};
						}
					}
				}
			} catch (Exception e) {
				isFailed = true;
				System.out.println("command not found or invalid params are enterd");
				return new String[] {};
			}
		} else {
			isFailed = true;
			System.out.println("command not found or invalid params are enterd");
			return new String[] {};
		}
		return new String[] {};
	}

/////////////////////////
	public static void Rm(String args[])// removes each specified file.
	{
		if (args.length == 1) {
			try {
				File file = new File(System.getProperty("user.dir") + "\\" + args[0]);
				if (file.isFile() & file.exists()) {
					file.delete();
					return;
				} else {
					isFailed = true;
					System.out.println("command not found or invalid params are enterd");
					return;
				}
			} catch (Exception e) {
				isFailed = true;
				System.out.println("command not found or invalid params are enterd");
				return;
			}
		} else {
			isFailed = true;
			System.out.println("command not found or invalid params are enterd");
			return;
		}
	}
//comand > file 
	public static void redirect(String[] arg, Boolean append, String filepath) {
		if (!filepath.equalsIgnoreCase("")) {
			if (arg == null) {
				return;
			} else {
				BufferedWriter out;
				boolean flag = touch(new String[] { filepath });
				if (flag == true) {
					File file = translat_to_file(filepath);
					try {
						out = new BufferedWriter(new FileWriter(file, append));
						int i = 0;
						for (String string : arg) {
							if (i == 0) {
								out.write("\n" + string + "\n");
								i++;
							} else {
								out.write(string + "\n");
							}
						}
						out.close();
						return;
					} catch (IOException e) {
						isFailed = true;
						System.out.println("command not found or invalid params are enterd");
						return;
					}
				} else {
					isFailed = true;
					System.out.println("command not found or invalid params are enterd");
					return;
				}
			}
		} else {
			isFailed = true;
			System.out.println("command not found or invalid params are enterd");
			return;
		}
	}

	public static void redirect(File[] arg, Boolean append, String filepath) {
		if (!filepath.equalsIgnoreCase("")) {
			if (arg == null) {
				return;
			} else {
				BufferedWriter out;
				boolean flag = touch(new String[] { filepath });
				if (flag == true) {
					File file = translat_to_file(filepath);
					try {
						out = new BufferedWriter(new FileWriter(file, append));
						int i = 0;
						for (File string : arg) {
							if (i == 0) {
								out.write("\n" + string.getPath() + "\n");
								i++;
							} else {
								out.write(string.getPath() + "\n");
							}
						}
						out.close();
						return;
					} catch (IOException e) {
						isFailed = true;
						System.out.println("command not found or invalid params are enterd");
						return;
					}
				} else {
					isFailed = true;
					System.out.println("command not found or invalid params are enterd");
					return;
				}
			}
		} else {
			isFailed = true;
			System.out.println("command not found or invalid params are enterd");
			return;
		}
	}

	static String[] past_ops_result_in_String = null;
	static File[] past_ops_result_in_File = null;

//System.out.println(Pattern.matches("-[a-zA-Z]", "-r"))
	public static void main(String[] args) {
		System.setProperty("user.dir", System.getProperty("user.home"));
		while (true) {
			isFailed = false;
			System.out.print(">");
			Scanner sc = new Scanner(System.in);
			String str = sc.nextLine();
			if (str.equalsIgnoreCase("exit")) {
				System.exit(0);
			} else {
				Boolean parse_success = parser.parse(str);
				if (parse_success == false) {
					System.out.println("command not found or invalid params are enterd");
					continue;
				} else {
					parser.getArgs(str);
					String command = parser.commandName;
					if (command.equalsIgnoreCase("ls")) {
						String[] lsargs = parser.args;
						if (lsargs.length > 1) {
							System.out.println("command not found or invalid params are enterd");
							continue;
						} else if (lsargs.length == 0) {
							past_ops_result_in_File = ls(null);
							if (isFailed == true)
								;
							continue;
						} else {
							past_ops_result_in_File = ls(parser.args[0]);
							if (isFailed == true)
								;
							continue;
						}
					}
					if (command.equalsIgnoreCase("ls")) {
						if (parser.redir_exist == true) {
							parser.getArgs(str);
							String[] fe = parser.args;
							if (fe.length == 0 || fe.length > 1) {
								System.out.println("command not found or invalid params are enterd");
								continue;
							} else {
								if (parser.RedircommandName.equalsIgnoreCase(">")) {
									redirect(past_ops_result_in_File, false, parser.args[0]);
									continue;
								}
								if (parser.RedircommandName.equalsIgnoreCase(">>")) {
									redirect(past_ops_result_in_File, true, parser.args[0]);
									continue;
								}
							}
						}
					}
					if (!command.equalsIgnoreCase("ls")) {
						String[] jl = parser.args;
						if (command.equalsIgnoreCase("echo")) {
							past_ops_result_in_String = echo(jl);
							if (isFailed == true) {
								continue;
							}
							if (parser.redir_exist == true) {
								parser.getArgs(str);
								String[] hkl = parser.args;
								if (hkl.length > 1 || hkl.length == 0) {
									System.out.println("command not found or invalid params are enterd");
									continue;
								} else {
									if (parser.RedircommandName.equalsIgnoreCase(">")) {
										redirect(past_ops_result_in_String, false, parser.args[0]);
										continue;
									}
									if (parser.RedircommandName.equalsIgnoreCase(">>")) {
										redirect(past_ops_result_in_String, true, parser.args[0]);
										continue;
									}
								}
							}
						}
						if (command.equalsIgnoreCase("pwd")) {
							past_ops_result_in_String = pwd();
							if (isFailed == true) {
								continue;
							}

							if (parser.redir_exist == true) {
								parser.getArgs(str);
								String[] hkl = parser.args;
								if (hkl.length > 1 || hkl.length == 0) {
									System.out.println("command not found or invalid params are enterd");
									continue;
								} else {
									if (parser.RedircommandName.equalsIgnoreCase(">")) {
										redirect(past_ops_result_in_String, false, parser.args[0]);
										continue;
									}
									if (parser.RedircommandName.equalsIgnoreCase(">>")) {
										redirect(past_ops_result_in_String, true, parser.args[0]);
										continue;
									}
								}
							}
						}
						if (command.equalsIgnoreCase("touch")) {
							touch(jl);
							if (isFailed == true) {
								continue;
							}
							if (parser.redir_exist == true) {
								System.out.println("command not found or invalid params are enterd");
								continue;
							}
							continue;
						}
						if (command.equalsIgnoreCase("cd")) {
							cd(jl);
							if (isFailed == true) {
								continue;
							}
							if (parser.redir_exist == true) {
								System.out.println("command not found or invalid params are enterd");
								continue;
							}
							continue;
						}
						if (command.equalsIgnoreCase("mkdir")) {
							mkdir(jl);
							if (isFailed == true) {
								continue;
							}
							if (parser.redir_exist == true) {
								System.out.println("command not found or invalid params are enterd");
								continue;
							}
							continue;
						}
						if (command.equalsIgnoreCase("Rmdir")) {
							Rmdir(jl);
							if (isFailed == true) {
								continue;
							}
							if (parser.redir_exist == true) {
								System.out.println("command not found or invalid params are enterd");
								continue;
							}
							continue;
						}
						if (command.equalsIgnoreCase("cat")) {
							past_ops_result_in_String = cat(jl);
							if (isFailed == true) {
								continue;
							}

							if (parser.redir_exist == true) {
								parser.getArgs(str);
								String[] hkl = parser.args;
								if (hkl.length > 1 || hkl.length == 0) {
									System.out.println("command not found or invalid params are enterd");
									continue;
								} else {
									if (parser.RedircommandName.equalsIgnoreCase(">")) {
										redirect(past_ops_result_in_String, false, parser.args[0]);
										continue;
									}
									if (parser.RedircommandName.equalsIgnoreCase(">>")) {
										redirect(past_ops_result_in_String, true, parser.args[0]);
										continue;
									}
								}
							}
						}
						if (command.equalsIgnoreCase("Rm")) {
							Rm(jl);
							if (isFailed == true) {
								continue;
							}
							if (parser.redir_exist == true) {
								System.out.println("command not found or invalid params are enterd");
								continue;
							}
							continue;
						}
					}

				}
			}
		}

	}

}
