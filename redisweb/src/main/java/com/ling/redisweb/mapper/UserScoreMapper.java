package com.ling.redisweb.mapper;

import com.ling.redisweb.domain.UserScore;
import com.ling.redisweb.domain.UserScoreExample;
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

public interface UserScoreMapper {
    @SelectProvider(type=UserScoreSqlProvider.class, method="countByExample")
    int countByExample(UserScoreExample example);

    @DeleteProvider(type=UserScoreSqlProvider.class, method="deleteByExample")
    int deleteByExample(UserScoreExample example);

    @Delete({
        "delete from user_score",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer id);

    @Insert({
        "insert into user_score (user_id, user_score, ",
        "name)",
        "values (#{userId,jdbcType=INTEGER}, #{userScore,jdbcType=BIGINT}, ",
        "#{name,jdbcType=VARCHAR})"
    })
    @Options(useGeneratedKeys=true,keyProperty="id")
    int insert(UserScore record);

    @InsertProvider(type=UserScoreSqlProvider.class, method="insertSelective")
    @Options(useGeneratedKeys=true,keyProperty="id")
    int insertSelective(UserScore record);

    @SelectProvider(type=UserScoreSqlProvider.class, method="selectByExample")
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="user_id", property="userId", jdbcType=JdbcType.INTEGER),
        @Result(column="user_score", property="userScore", jdbcType=JdbcType.BIGINT),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR)
    })
    List<UserScore> selectByExample(UserScoreExample example);

    @Select({
        "select",
        "id, user_id, user_score, name",
        "from user_score",
        "where id = #{id,jdbcType=INTEGER}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="user_id", property="userId", jdbcType=JdbcType.INTEGER),
        @Result(column="user_score", property="userScore", jdbcType=JdbcType.BIGINT),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR)
    })
    UserScore selectByPrimaryKey(Integer id);

    @UpdateProvider(type=UserScoreSqlProvider.class, method="updateByExampleSelective")
    int updateByExampleSelective(@Param("record") UserScore record, @Param("example") UserScoreExample example);

    @UpdateProvider(type=UserScoreSqlProvider.class, method="updateByExample")
    int updateByExample(@Param("record") UserScore record, @Param("example") UserScoreExample example);

    @UpdateProvider(type=UserScoreSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(UserScore record);

    @Update({
        "update user_score",
        "set user_id = #{userId,jdbcType=INTEGER},",
          "user_score = #{userScore,jdbcType=BIGINT},",
          "name = #{name,jdbcType=VARCHAR}",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(UserScore record);
}