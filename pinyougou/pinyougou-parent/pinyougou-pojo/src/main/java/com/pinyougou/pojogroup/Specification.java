package com.pinyougou.pojogroup;

import com.pinyougou.pojo.TbSpecification;
import com.pinyougou.pojo.TbSpecificationOption;


import java.io.Serializable;
import java.util.List;

/**
 * 规格组合实体类
 *
 * @author cong
 */
public class Specification implements Serializable {

    private TbSpecification Specification;

    private List<TbSpecificationOption> SpecificationOptionList;

    public TbSpecification getSpecification() {
        return Specification;
    }

    public void setSpecification(TbSpecification specification) {
        Specification = specification;
    }

    public List<TbSpecificationOption> getSpecificationOptionList() {
        return SpecificationOptionList;
    }

    public void setSpecificationOptionList(List<TbSpecificationOption> specificationOptionList) {
        SpecificationOptionList = specificationOptionList;
    }
}
