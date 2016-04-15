package com.hzih.community.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class Region {

    private Long id;
    private String name;
    private String enName;
    private String code;

    private Region parentRegion;  //parentId

    private Set<Region> childRegions; //child

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public Region getParentRegion() {
        return parentRegion;
    }

    public void setParentRegion(Region parentRegion) {
        this.parentRegion = parentRegion;
    }

    public Set<Region> getChildRegions() {
        return childRegions;
    }

    public void setChildRegions(Set<Region> childRegions) {
        this.childRegions = childRegions;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Region() {
    }

    public static String getName(Region region) {
        List<String> list = new ArrayList<>();
        while (region != region.getParentRegion()) {
            list.add(region.getName());
            region = region.getParentRegion();
        }
        if (list != null && list.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (int i = (list.size() - 1); i >= 0; i--) {
                sb.append(list.get(i));
            }
            return sb.toString();
        }
        return null;
    }
}