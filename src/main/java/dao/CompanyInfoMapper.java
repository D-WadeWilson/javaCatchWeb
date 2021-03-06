package dao;

import dataobject.CompanyInfo;
import dataobject.Criteria;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

public interface CompanyInfoMapper {
    /**
     * 根据条件查询记录总数
     */
    int countByExample(Criteria example);

    /**
     * 根据条件删除记录
     */
    int deleteByExample(Criteria example);

    /**
     * 根据主键删除记录
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * 保存记录,不管记录里面的属性是否为空
     */
    int insert(CompanyInfo record);

    /**
     * 保存属性不为空的记录
     */
    int insertSelective(CompanyInfo record);

    /**
     * 根据条件查询记录集
     */
    List<CompanyInfo> selectByExample(Criteria example);

    /**
     * 根据主键查询记录
     */
    CompanyInfo selectByPrimaryKey(Integer id);

    /**
     * 根据条件更新属性不为空的记录
     */
    int updateByExampleSelective(@Param("record") CompanyInfo record, @Param("condition") Map<String, Object> condition);

    /**
     * 根据条件更新记录
     */
    int updateByExample(@Param("record") CompanyInfo record, @Param("condition") Map<String, Object> condition);

    /**
     * 根据主键更新属性不为空的记录
     */
    int updateByPrimaryKeySelective(CompanyInfo record);

    /**
     * 根据主键更新记录
     */
    int updateByPrimaryKey(CompanyInfo record);
}