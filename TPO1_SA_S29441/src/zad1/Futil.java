package zad1;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class Futil {
    private static Charset cp1250 = Charset.forName("Cp1250");
    private static Charset utf8 = StandardCharsets.UTF_8;
    public static void processDir(String dirName, String resultFileName) {
        Path resultFile = Paths.get(resultFileName);
        try {

            Files.write(resultFile, new byte[0], StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            Files.walkFileTree(Paths.get(dirName), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    try (
                            FileChannel fileChannel = FileChannel.open(file, StandardOpenOption.READ);
                            FileChannel outPutChannel = FileChannel.open(resultFile, StandardOpenOption.WRITE, StandardOpenOption.APPEND)
                    ) {
                        ByteBuffer buffor = ByteBuffer.allocate((int) fileChannel.size());
                        fileChannel.read(buffor);
                        buffor.flip();

                        CharBuffer cbuf = cp1250.decode(buffor);
                        buffor = utf8.encode(cbuf);

                        outPutChannel.write(buffor);
                    }
                    return super.visitFile(file, attrs);
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}