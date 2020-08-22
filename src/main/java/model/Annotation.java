package model;


import org.apache.commons.lang3.StringUtils;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 项健健
 * @time 2020/8/21
 * @comment
 */

@XmlRootElement
public class Annotation {

    public Annotation() {
        this.objects = new ArrayList<>();
    }

    public static Annotation init(File file) {
        Annotation annotation = new Annotation();
        annotation.folder = "VOC2012";
        annotation.filename = StringUtils.substringBefore(file.getName(), ".")+".jpg";
        Source source = new Source();
        source.setAnnotation("PASCAL VOC2007");
        source.setDatabase("The VOC2011 Database");
        source.setImage("flickr");
        annotation.source = source;
        Size size = new Size();
        size.setWidth("1000");
        size.setHeight("600");
        size.setDepth("3");
        annotation.size = size;
        annotation.segmented = "1";
        return annotation;
    }

    @XmlElement
    public String folder;

    @XmlElement
    public String filename;

    @XmlElement
    public Source source;

    @XmlElement
    public Size size;

    @XmlElement
    public String segmented;

    @XmlElement(name = "object")
    public List<Abject> objects;

}
