import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.*;

/**
 * @author 项健健
 * @time 2020/8/25
 * @comment
 */

public class Select {

    private static final String PARENT_DIR =
            "/Volumes/Data/VOCdevkit-mine/VOC2012";

    private static final String TRAIN_SOURCE_DIR = "/Annotations/detection-result/";

    private static final String VAL_SOURCE_DIR = "/Annotations/ground-truth/";


    public static void main(String[] args) throws IOException {
        File dir = new File(PARENT_DIR + VAL_SOURCE_DIR);
        File[] files = dir.listFiles(pathname -> StringUtils.endsWith(pathname.getName(), "txt") && !StringUtils.startsWith(pathname.getName(), "."));
        Map<String, Set<String>> classification = new HashMap<>();
        for (File file : Objects.requireNonNull(files)) {
            genStatus(classification, file);
        }
        Map<String, String> pathName = new HashMap<>();

        classification.forEach((e, set) -> {
            if (set.size() == 1) {
                for (String s : set) {
                    pathName.put(e, s);
                }
            }
        });
        addToPath(pathName);

    }

    private static void genStatus(Map<String, Set<String>> classification, File file) throws IOException {
        FileInputStream inputStream = new FileInputStream(file);
        List<String> lines = IOUtils.readLines(inputStream, "utf-8");
        Set<String> name = new HashSet<>();
        lines.forEach(e -> {
            String[] strings = StringUtils.split(e, ' ');
            name.add(strings[0]);
        });
        classification.put(file.getAbsolutePath(), name);
    }

    private static void addToPath(Map<String, String> namePath) {
        namePath.forEach((path, name) -> {
            String dirPath = PARENT_DIR + "/class/" + name;
            File dir = new File(dirPath);
            dir.mkdir();
            String fileName = StringUtils.substringAfterLast(path, "/");
            try {
                if (compare(fileName)) {
                    copyTo(path, dirPath + "/" + fileName);
                    genCopyCMD(name, fileName);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private static void copyTo(String origin, String dist) throws IOException {
        FileInputStream inputStream = new FileInputStream(origin);
        FileOutputStream outputStream = new FileOutputStream(dist);
        PrintWriter printWriter = new PrintWriter(outputStream);
        List<String> lines = IOUtils.readLines(inputStream, "utf-8");
        lines.forEach(printWriter::println);
        inputStream.close();
        printWriter.close();
    }

    private static boolean compare(String fileName) throws IOException {
        String result = PARENT_DIR + TRAIN_SOURCE_DIR + fileName;
        String val = PARENT_DIR + VAL_SOURCE_DIR + fileName;
        return compare(result, val);
    }

    private static boolean compare(String result, String val) throws IOException {
        FileInputStream resultInputStream = new FileInputStream(result);
        FileInputStream valInputStream = new FileInputStream(val);
        List<String> resultLines = IOUtils.readLines(resultInputStream, "utf-8");
        List<String> valLines = IOUtils.readLines(valInputStream, "utf-8");
        return resultLines.size() == valLines.size();
    }

    private static void genCopyCMD(String name, String fileName) {
        fileName = StringUtils.substringBefore(fileName, ".") + ".jpg";
        String dirPath = PARENT_DIR + "/img/" + name;
        File dir = new File(dirPath);
        dir.mkdir();
        System.out.println("cp " + PARENT_DIR + "/JPEGImages/val/" + fileName + " " + dir.getAbsolutePath() + '/' + fileName);
    }

}
