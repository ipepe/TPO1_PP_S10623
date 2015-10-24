package zad1;

import static java.nio.file.FileVisitResult.*;
import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.nio.file.attribute.*;
import java.io.*;

public class MySimpleFileVisitor extends SimpleFileVisitor<Path> {

    private FileChannel output_file_channel;
    private ByteBuffer common_buffer;

    Charset inCharset  = Charset.forName("Cp1250"),
            outCharset = Charset.forName("UTF-8");

    public MySimpleFileVisitor(Path output_file_path) throws IOException {
        this.output_file_channel = FileChannel.open(output_file_path, EnumSet.of(CREATE, APPEND));
    }

    private void recodeForUtf(FileChannel input_file_channel, long buffer_size){
        common_buffer = ByteBuffer.allocate((int)buffer_size +1);
        common_buffer.clear();

        try {

        	input_file_channel.read(common_buffer);
        	common_buffer.flip();
        	CharBuffer cbuf = inCharset.decode(common_buffer);
        	ByteBuffer buf = outCharset.encode(cbuf);

        	while( buf.hasRemaining() ){
        		this.output_file_channel.write(buf);
        	}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
    }

    @Override
    public FileVisitResult visitFile(Path file_path, BasicFileAttributes attr) {
        System.out.format("File: %s ", file_path);
        System.out.println("(" + attr.size() + "bytes)");
        try{
            this.recodeForUtf(FileChannel.open(file_path), attr.size());
        }catch(IOException ex){
            System.out.format("Nie udalo sie otworzyc pliku: %s ", file_path);
        }
        return CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file_path, IOException exc) {
        System.err.println("MySimpleFileVisitor.visitFileFailed: " + exc);
        return CONTINUE;
    }
}
