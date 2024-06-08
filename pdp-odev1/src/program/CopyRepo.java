/**
* PROGRAMLAMA DİLLERİNİN PRENSİPLERİ - 1. Ödev
* @author Yusuf Taha Ezgin - G221210008 - yusuf.ezgin@ogr.sakarya.edu.tr
* @since 5.04.2024
* <p>
* 		2. Öğretim B 
* 		Dersi Veren: Prof.Dr. AHMET ZENGİN
* </p>
*/

package program;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

public class CopyRepo {
	// Git deposunu klonlayıp *.java dosyalarını al
    static List<File> cloneAndRetrieveJavaFiles(String repoUrl, File tempDir) throws IOException {
        List<File> javaFiles = new ArrayList<>();

        try {
            // Git klonlama işlemi
            Git git = Git.cloneRepository()
                    .setURI(repoUrl)
                    .setDirectory(tempDir)
                    .call();

            // Klonlanmış depodaki *.java dosyalarını al
            addJavaFiles(tempDir, javaFiles);

            // Git kapat
            git.close();
        } catch (GitAPIException e) {
            e.printStackTrace();
        }

        return javaFiles;
    }

    // Geçici bir dizin oluşturma fonksiyonu
    static File createTempDir() {
        File tempDir = null;
        try {
            tempDir = File.createTempFile("GitRepo", "");
            tempDir.delete();
            tempDir.mkdir();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tempDir;
    }

    // Klonlanmış depodaki *.java dosyalarını al
    private static void addJavaFiles(File directory, List<File> javaFiles) {
        for (File file : directory.listFiles()) {
            if (file.isDirectory()) {
                addJavaFiles(file, javaFiles);
            } else if (file.isFile() && file.getName().endsWith(".java")) {
                javaFiles.add(file);
            }
        }
    }

    // Sadece sınıf olan dosyaları ayıkla
    static List<File> filterClassFiles(List<File> javaFiles) {
        List<File> classFiles = new ArrayList<>();
        for (File javaFile : javaFiles) {
            // Dosyanın içeriğinde "class" anahtar kelimesini arayarak sınıf dosyası olup olmadığını kontrol et
            try (Scanner scanner = new Scanner(javaFile)) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine().trim();
                    if (line.startsWith("class") || line.contains("class ")) {
                        classFiles.add(javaFile);
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return classFiles;
    }
}
