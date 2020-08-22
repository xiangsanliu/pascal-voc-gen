package model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author 项健健
 * @time 2020/8/21
 * @comment
 */

@Getter
@Setter
public class Abject {

    private String name;

    private String confidence;

    private String pose = "Unspecified";

    private String truncated = "0";

    private String difficult = "0";

    private BndBox bndbox;

}
