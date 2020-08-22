import model.Annotation;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author 项健健
 * @time 2020/8/22
 * @comment
 */

public class XmlUtil {

    public static void javaToxml(Object object, String filePath) throws JAXBException, FileNotFoundException {
        // 获取JAXB的上下文环境，需要传入具体的 Java bean -> 这里使用Student
        JAXBContext context = JAXBContext.newInstance(object.getClass());
        // 创建 Marshaller 实例
        Marshaller marshaller = context.createMarshaller();
        // 设置转换参数 -> 这里举例是告诉序列化器是否格式化输出
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        // 构建输出环境 -> 这里使用标准输出，输出到控制台Console
        PrintStream out = new PrintStream(new FileOutputStream(filePath));
//        PrintStream out = System.out;
        // 将所需对象序列化 -> 该方法没有返回值
        marshaller.marshal(object, out);
    }

    public static void addToMap(Map<String, List<String>> path, String name, String filePath) {
        List<String> paths = path.get(name);
        if (paths != null) {
            paths.add(filePath);
            return;
        }
        paths = new ArrayList<>();
        paths.add(filePath);
        path.put(name, paths);
    }

}
