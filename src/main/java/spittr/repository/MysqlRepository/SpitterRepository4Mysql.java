package spittr.repository.MysqlRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;

import spittr.model.Spitter;
import spittr.model.Spittle;
import spittr.repository.IRepository.SpitterRepository;

/**
 * 用户仓库实现类，mysql
 * Repository 创建时自动得到JdbcOperations对象
 *
 * @author wq
 *         repository：2016-11-19
 */
@Repository()
public class SpitterRepository4Mysql implements SpitterRepository {

    private NamedParameterJdbcOperations jdbcOperations;

    @Inject
    public SpitterRepository4Mysql(NamedParameterJdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }

    public List<Spitter> findSpitters(int page, int pageSize) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("skip", (page-1)*pageSize);
        paramMap.put("take", pageSize);

        return jdbcOperations.query("select id,username,password,firstName,lastName from spitter limit :skip,:take"
                ,paramMap,new BeanPropertyRowMapper<Spitter>(Spitter.class));
    }

    public int add(Spitter spitter) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("username", spitter.getUsername());
        paramMap.put("password", spitter.getPassword());
        paramMap.put("firstName", spitter.getFirstName());
        paramMap.put("lastName", spitter.getLastName());

        return jdbcOperations.update
                ("insert into spitter(username,password,firstName,lastName)" +
                        " values(:username,:password,:firstName,:lastName); ", paramMap);
    }

    public Spitter findByUsername(String username) {
        if ("24".equals(username)) {
            return new Spitter(24L, "jbauer", "24hours", "Jack", "Bauer");
        } else {
            return new Spitter(12L, "boy", "12hours", "Harry", "Potter");
        }
    }

    public Spitter save(Spitter before) {
        return new Spitter(24L, "jbauer", "24hours", "Jack", "Bauer");
    }

}
