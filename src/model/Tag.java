package model;

import java.io.Serializable;

/**
 * Class of Tag Object
 * @author XiYue Zhang
 * @author ChuXu Song
 *
 */
public class Tag implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String storeDir = "data";
    public static final String storeFile = "users.txt";

    public String name;
    public String value;

    public Tag(String name, String value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj==null || !(obj instanceof Tag)){
            return false;
        }
        Tag obj1 = (Tag) obj;
        return obj1.name.equals(this.name) && obj1.value.equals(this.value);
    }


}
