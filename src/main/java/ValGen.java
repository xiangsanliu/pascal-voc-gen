import model.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import javax.xml.bind.JAXBException;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author 项健健
 * @time 2020/8/22
 * @comment
 */

public class ValGen {

    private static final String PARENT_DIR =
            "/Users/xiang/Downloads/VOCdevkit-mine/VOC2012";

    private static final String VAL_SOURCE_DIR = "/Annotations/ground-truth/";

    public static void main(String[] args) throws IOException, JAXBException {
        File dir = new File(PARENT_DIR + VAL_SOURCE_DIR);
        Map<String, List<String>> path = new HashMap<>();
        File[] files = dir.listFiles(pathname -> StringUtils.endsWith(pathname.getName(), "txt"));
        String valPath = PARENT_DIR + "/ImageSets/Main/val.txt";
        PrintWriter valWriter = new PrintWriter(new FileOutputStream(valPath));
        for (File file : Objects.requireNonNull(files)) {
            genTrain(file.getAbsolutePath(), path);
            String relativePath = "val/" + StringUtils.substringBefore(file.getName(), ".");
            valWriter.println(relativePath);
        }
        genValPathTxt(path);
        valWriter.close();
//        path.forEach((e, strings) -> {
//            System.out.println('\'' + e + '\'');
//        });
    }

    private static void genTrain(String filePath, Map<String, List<String>> path) throws IOException, JAXBException {
        File file = new File(filePath);
        String fileName = StringUtils.substringBefore(file.getName(), ".");
        Annotation annotation = Annotation.init(file);
        FileInputStream fileInputStream = new FileInputStream(file);
        List<String> lines = IOUtils.readLines(fileInputStream, "utf-8");
        String relativePath = "val/" + StringUtils.substringBefore(file.getName(), ".");
        lines.forEach(e -> {
            String[] strings = StringUtils.split(e, ' ');
            Abject abject = new Abject();
            String name;
            if (StringUtils.contains(strings[0], '_')) {
                name = StringUtils.substringBefore(strings[0], "_");
            } else {
                name = strings[0];
            }
            abject.setName(name);
            BndBox bndBox = new BndBox();
            bndBox.setXmin(strings[1]);
            bndBox.setYmin(strings[2]);
            bndBox.setXmax(strings[3]);
            bndBox.setYmax(strings[4]);
            abject.setBndbox(bndBox);
            annotation.objects.add(abject);
            // jaxb
            XmlUtil.addToMap(path, abject.getName(), relativePath);

        });
        XmlUtil.javaToxml(annotation, PARENT_DIR + "/Annotations/val/" + fileName + ".xml");
    }

    private static void genValPathTxt(Map<String, List<String>> path) throws FileNotFoundException {
        path.forEach((e, strings) -> {
            String filePath = PARENT_DIR + "/ImageSets/Main/" + e + "_val.txt";
            try (PrintStream out = new PrintStream(new FileOutputStream(filePath))) {
                strings.forEach(out::println);
            } catch (FileNotFoundException fileNotFoundException) {
                fileNotFoundException.printStackTrace();
            }
        });
    }


}
