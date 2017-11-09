package encode;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

import static java.nio.charset.StandardCharsets.UTF_8;

public class ChangeEncode {

	public static void main(String[] args) throws IOException {
		Path startingDir = Paths.get("E:\\news\\resin\\20170904.newsmultipic\\");
		Files.walkFileTree(startingDir, new HandleFile());
	}
}


class HandleFile  implements FileVisitor<Path> {

	private static String dir = "20170904.newsmultipic";

	private BufferedWriter gbkWriter;

	public HandleFile() throws IOException {
		gbkWriter = Files.newBufferedWriter(Paths.get("E:/news.log"), UTF_8);
	}

	@Override
	public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
		System.out.println("preVisitDirectory: " + dir.toString());
		if (dir.startsWith(".")) return FileVisitResult.SKIP_SIBLINGS;
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFile(Path path, BasicFileAttributes attrs)
		throws IOException {
		if (path.getFileName().toString().startsWith(".")) return FileVisitResult.CONTINUE;
		int start = path.toString().indexOf(dir);
		Path convertPath = Paths.get(path.toString().substring(0, start) + "convert" + path.toString().substring(start + dir.length()));


		if (!Files.exists(convertPath.getParent())) convertPath.toFile().getParentFile().mkdirs();
		String tmp = path.getFileName().toString();
		if (!(tmp.endsWith(".java") || tmp.endsWith(".jsp") || tmp.endsWith(".xml"))) {
			Files.copy(path, convertPath, StandardCopyOption.REPLACE_EXISTING);
			return FileVisitResult.CONTINUE;
		}

		InputStream ios = new FileInputStream(path.toFile());
		byte[] b=new byte[3];
		ios.read(b);
		ios.close();
		if (b[0] == -17 && b[1] == -69 && b[2] == -65) {
			System.out.println(path + " is utf-8");
			Files.copy(path, convertPath, StandardCopyOption.REPLACE_EXISTING);
			return FileVisitResult.CONTINUE;
		}

		try (BufferedReader reader = Files.newBufferedReader(path, Charset.forName("GBK"));
			BufferedWriter writer = Files.newBufferedWriter(convertPath, Charset.forName("UTF-8"), StandardOpenOption.CREATE)) {
			String line;
			int lineNum = 0;
			while ((line = reader.readLine()) != null) {
				lineNum ++;
				line = line.replaceAll("pageEncoding=\"GBK\"", "pageEncoding=\"UTF-8\"");
				if (line.toLowerCase().contains("gbk")) {

					gbkWriter.write(path + " [ " + lineNum + "] contains gbk: " + line + "\n");
					gbkWriter.flush();
				}
				writer.write(line + "\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
		System.out.println("visitFile: " + file.getFileName());
		return  FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
		System.out.println("postVisitDirectory: " + dir.getFileName());

		return  FileVisitResult.CONTINUE;
	}

}