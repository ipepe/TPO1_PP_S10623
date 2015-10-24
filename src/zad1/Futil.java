package zad1;

import java.nio.file.*;

public class Futil {
	
	public static void processDir(String input_directory_str, String output_file_str){
		
		Path output_file_path = Paths.get("C:\\Users\\Patryk\\TPO1res.txt");
        Path starting_directory = Paths.get(input_directory_str);

        try{
            MySimpleFileVisitor visitor = new MySimpleFileVisitor(output_file_path);
            Files.walkFileTree(starting_directory, visitor);
        }catch(Exception ex){
            System.out.println(ex);
        }
		
	}

}
