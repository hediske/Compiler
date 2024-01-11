
package com.mycompany.compiler.lexical_ana;


public class lexic_unit {
    private String unilexid;
    private String rangerid;
    private String type;
    
    public lexic_unit()
    {
    
    }
    public lexic_unit (String type, String rank , String type_s)
    {
        this.unilexid=type;
        this.rangerid=rank;
        this.type=type_s;
    }
    public lexic_unit(String type, Integer rank , String type_s)
    {
        String s=String.valueOf(rank);
        this.unilexid=type;
        this.rangerid=s;
        this.type=type_s;
    }    

    public String getUnilexid() {
        return unilexid;
    }

    public void setUnilexid(String unilexid) {
        this.unilexid = unilexid;
    }

    public String getRangerid() {
        return rangerid;
    }

    public void setRangerid(String rangerid) {
        this.rangerid = rangerid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "lexic_unit{" + "unilexid=" + unilexid + ", rangerid=" + rangerid + ", type=" + type + '}';
    }
    
    
}
