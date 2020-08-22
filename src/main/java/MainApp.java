import model.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import javax.xml.bind.JAXBException;
import java.io.*;
import java.util.*;

/**
 * @author 项健健
 * @time 2020/8/21
 * @comment
 */

public class MainApp {

    private static final String PARENT_DIR =
            "/Users/xiang/Downloads/VOCdevkit/VOC2012";

    private static final String TRAIN_SOURCE_DIR = "/Annotations/detection-result/";

    public static void main(String[] args) throws IOException, JAXBException {
        File dir = new File(PARENT_DIR + TRAIN_SOURCE_DIR);
        File[] files = dir.listFiles(pathname -> StringUtils.endsWith(pathname.getName(), "txt"));
        Map<String, List<String>> path = new HashMap<>();
        for (File value : Objects.requireNonNull(files)) {
            genTrain(value.getAbsolutePath(), path);
        }
//        genTrain(files[0].getAbsolutePath());
        genTrainPathTxt(path);

    }

    private static void genTrain(String filePath, Map<String, List<String>> path) throws IOException, JAXBException {
        File file = new File(filePath);
        String fileName = StringUtils.substringBefore(file.getName(), ".");
        FileInputStream fileInputStream = new FileInputStream(file);
        Annotation annotation = Annotation.init(file);
        List<String> lines = IOUtils.readLines(fileInputStream, "utf-8");
        String relativePath = "train/" + StringUtils.substringBefore(file.getName(), ".");
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
            abject.setConfidence(strings[1]);
            BndBox bndBox = new BndBox();
            bndBox.setXmin(strings[2]);
            bndBox.setYmin(strings[3]);
            bndBox.setXmax(strings[4]);
            bndBox.setYmax(strings[5]);
            abject.setBndbox(bndBox);
            annotation.objects.add(abject);
            XmlUtil.addToMap(path, abject.getName(), relativePath);
            // jaxb
        });
        XmlUtil.javaToxml(annotation, PARENT_DIR + "/Annotations/train/" + fileName + ".xml");
    }


    private static void genTrainPathTxt(Map<String, List<String>> path) {
        path.forEach((e, strings) -> {
            String filePath = PARENT_DIR + "/ImageSets/Main/" + e + "_train.txt";
            try (PrintStream out = new PrintStream(new FileOutputStream(filePath))) {
                strings.forEach(out::println);
            } catch (FileNotFoundException fileNotFoundException) {
                fileNotFoundException.printStackTrace();
            }
        });
    }


}
