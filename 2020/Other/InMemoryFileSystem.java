import java.util.*;
interface FileSystem {
	
	/**
	- validate path
	- Given a path, if the path points to a file, return the name of the file,
	  otherwise, return the names of all the files and directories in the path.
	- validate path and throw an IllegalArgumentException if necessary
	**/
	List<String> ls(String path);


	/**
	- validate path 
	- if path doesn't exist, create a new directory
	- if the directory already exists, do nothing
	**/
	void mkdir(String path);


	/**
	- validate path
	- if file path doesn't exist, create the file at the specified path and add the content
	- if file exists, append the content
	**/
	void addContentToFile(String path, String fileContent);

	/**
	- validate path
	- if file path exists, return its content
	- if file path doesnt exist, return null
	**/
	String readContentFromFile(String path);
}

public class InMemoryFileSystem implements FileSystem {
	private Directory root;
	private final Semaphore semaphore;
	/**
	A helper class that represents each individual directory in the file system
	**/
	class Directory {

		String directoryName;
		Map<String, String> fileToContentMap; // file name mapped to it's content
		Directory leftLink; // a link to all sibling directories whose names are less than this directory name. 
		Directory rightLink; // a link to all sibling directories whose names are less than this directory name.
		Directory forwardLink; // a root link to to all sub directories of this directory.
		final Object lock = new Object();


		public Directory(String directoryName, int numThreads) {
			this.directoryName = directoryName;
			fileToContentMap = new HashMap<>();
			semaphore = new Semaphore(numThreads);
		}


		// appends content to a file in this directory, if it's present
		public void addFile(String name, String content) {
			try {
				synchronized(lock) {
					fileToContentMap.put(name, fileToContentMap.getOrDefault(name, "") +  content);
					lock.signalAll();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			
		}
		//returns a list of all the directories under this directory
		public Set<String> directories() {
			Queue<Directory> children = new LinkedList<>();
			Set<String> siblings = new HashSet<>();
			
			children.add(this); // start with 'this' directory and find all it's siblings
			while (!children.isEmpty()) {
				Directory current = children.poll();
				siblings.add(current.directoryName);
				if (current.leftLink != null) children.add(current.leftLink);
				if (current.rightLink != null) children.add(current.rightLink);
			}
			return siblings;
		}

		public String fileConent(String file) {
			//don't attempt to acquire the lock if it's not locked by a writer thread.
			try {
			
				synchronized (lock) {
					return fileToContentMap.getOrDefault(file, null);
				}
				
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public List<String> contents() {
			//don't attempt to acquire the lock if it's not locked by a writer thread.
			try {
				synchronized (lock) {
					List<String> directoryContents = new ArrayList<>();
			   	 	directoryContents.addAll(directories());
			    	directoryContents.addAll(fileToContentMap.keySet());
			    	return directoryContents;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}


	@Override
	public List<String> ls(String path) {
		validate(path);
		String[] paths = path.split("/");
		try {
			semaphore.acquire();
			Directory targetDirectory = paths.length == 0  ? root : getDrctry(root, paths, 1, paths.length - 1);
			List<String> pathContent = targetDirectory == null ? new ArrayList<>() : targetDirectory.contents();
			return pathContent;
		} catch ( Exception e) {
			e.printStackTrace();
		} finally {
			semaphore.release();
		}
		return null;
		
	}

	private Directory getDrctry(Directory current, String[] paths, int currentIndex, int target) {
		
		if (current == null || target < currentIndex) return null;
		String currentPath  = paths[currentIndex];

		if (currentPath.compareTo(current.directoryName) > 0) return getDrctry(current.rightLink, paths, currentIndex, target);
	    else if (currentPath.compareTo(current.directoryName) < 0) return getDrctry(current.leftLink, paths, currentIndex, target);
	    else if (currentIndex == target) return current; // should come after all siblings have been searched
		else return getDrctry(current.forwardLink, paths, currentIndex + 1, target);
	}

	@Override
	public void mkdir(String path) {
		validate(path);
		String[] paths = path.split("/");
		try {
			semaphore.acquire();
			root = mkdir(root, paths, 1, paths.length - 1);
		} catch (Exception e) {
			e.printStackTrace();		
		} finally {
			semaphore.release();
		}
	}

	private Directory mkdir(Directory current, String[] paths, int currentIndex, int target) {
		if (currentIndex > target) {
			return null;
		}
		String currentPath = paths[currentIndex];
		if (current == null) current = new Directory(currentPath);
		if (currentPath.compareTo(current.directoryName) > 0) current.rightLink = mkdir(current.rightLink, paths, currentIndex, target);
		else if (currentPath.compareTo(current.directoryName) < 0) current.leftLink = mkdir(current.leftLink, paths, currentIndex, target);
		else current.forwardLink = mkdir(current.forwardLink, paths, currentIndex + 1, target);

		return current;
	
	}

	@Override
	public void addContentToFile(String path, String fileContent) {
		validate(path);
		if (fileContent == null) throw new IllegalArgumentException("Invalid file content.");
		String[] paths = path.split("/");
		try {
			semaphore.acquire();
			root = mkdir(root, paths, 1, paths.length - 2); // make directory in case parts don't exist
			Directory targetDirectory = getDrctry(root, paths, 1, paths.length - 2);
			targetDirectory.addFile(paths[paths.length - 1], fileContent);
		} catch (Exception e) {
			e.printStackTrace();		
		} finally {
			semaphore.release();
		}
		
	}
	
	@Override
	public String readContentFromFile(String path) {
		validate(path);
		String[] paths = path.split("/");
		try {
			semaphore.acquire();
			Directory targetDirectory = getDrctry(root, paths, 1, paths.length - 2);
			return targetDirectory.fileConent(paths[paths.length - 1]);
		} catch (Exception e) {
			e.printStackTrace();		
		} finally {
			semaphore.release();
		}
		return null;
		
	}

	private void validate(String path) {
		if (path == null || path.split("/").length == 1 || path.charAt(0) != '/') {
			throw new IllegalArgumentException("Invalid path");
		}

	}

	public static void main(String[] args) {
		FileSystem fileSystem = new InMemoryFileSystem();
		List<String> contents = fileSystem.ls("/");
		print(contents);
		fileSystem.mkdir("/a/b/c");
		fileSystem.addContentToFile("/a/b/c/d", "hello");
		contents = fileSystem.ls("/");
		print(contents);
		String fileContent = fileSystem.readContentFromFile("/a/b/c/d");
		System.out.println(fileContent);
	}
	private static void print(List<String> l) {
		if (l == null || l.size() == 0) {
			return;
		}
		System.out.print("[" + l.get(0));
		for (int i = 1; i < l.size(); i++) {
			System.out.print("," + l.get(i));
		}
		System.out.println("]");
	}
}