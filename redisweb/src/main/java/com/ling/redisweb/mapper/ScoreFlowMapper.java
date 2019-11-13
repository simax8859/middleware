package com.ling.redisweb.mapper;

import com.ling.redisweb.domain.ScoreFlow;
import com.ling.redisweb.domain.ScoreFlowExample;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.type.JdbcType;

public interface ScoreFlowMapper {
    @SelectProvider(type=ScoreFlowSqlProvider.class, method="countByExample")
    int countByExample(ScoreFlowExample example);

    @DeleteProvider(type=ScoreFlowSqlProvider.class, method="deleteByExample")
    int deleteByExample(ScoreFlowExample example);

    @Delete({
        "delete from score_flow",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer id);

    @Insert({
        "insert into score_flow (score, user_id, ",
        "user_name)",
        "values (#{score,jdbcType=BIGINT}, #{userId,jdbcType=INTEGER}, ",
        "#{userName,jdbcType=VARCHAR})"
    })
    @Options(useGeneratedKeys=true,keyProperty="id")
    int insert(ScoreFlow record);

    @InsertProvider(type=ScoreFlowSqlProvider.class, method="insertSelective")
    @Options(useGeneratedKeys=true,keyProperty="id")
    int insertSelective(ScoreFlow record);

    @SelectProvider(type=ScoreFlowSqlProvider.class, method="selectByExample")
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="score", property="score", jdbcType=JdbcType.BIGINT),
        @Result(column="user_id", property="userId", jdbcType=JdbcType.INTEGER),
        @Result(column="user_name", property="userName", jdbcType=JdbcType.VARCHAR)
    })
    List<ScoreFlow> selectByExample(ScoreFlowExample example);

    @Select({
        "select",
        "id, score, user_id, user_name",
        "from score_flow",
        "where id = #{id,jdbcType=INTEGER}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="score", property="score", jdbcType=JdbcType.BIGINT),
        @Result(column="user_id", property="userId", jdbcType=JdbcType.INTEGER),
        @Result(column="user_name", property="userName", jdbcType=JdbcType.VARCHAR)
    })
    ScoreFlow selectByPrimaryKey(Integer id);

    @UpdateProvider(type=ScoreFlowSqlProvider.class, method="updateByExampleSelective")
    int updateByExampleSelective(@Param("record") ScoreFlow record, @Param("example") ScoreFlowExample example);

    @UpdateProvider(type=ScoreFlowSqlProvider.class, method="updateByExample")
    int updateByExample(@Param("record") ScoreFlow record, @Param("example") ScoreFlowExample example);

    @UpdateProvider(type=ScoreFlowSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(ScoreFlow record);

    @Update({
        "update score_flow",
        "set score = #{score,jdbcType=BIGINT},",
          "user_id = #{userId,jdbcType=INTEGER},",
          "user_name = #{userName,jdbcType=VARCHAR}",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(ScoreFlow record);
}