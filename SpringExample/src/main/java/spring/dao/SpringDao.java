package spring.dao;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Repository
public class SpringDao {

    private static final String INSERT_SQL = "insert into <table> (rule_id, data, match_mode, match_value, secret_level, " +
            "portal, account, url_level_set_id, sogou_rank, important_level, ctime, utime) values (:ruleId, :word, :matchMode, :matchValue, :secretLevel, " +
            ":portal, :account, :urlLevelSetId, :sogouRank, :importantLevel, NOW(), NOW())";

    private static final String INSERT_IMAGE_SQL = "insert into <table> (rule_id, data, match_mode, match_value, secret_level, " +
            "portal, account, url_level_set_id, sogou_rank,image_url, md5, important_level, ctime, utime) values (:ruleId, :word, :matchMode, :matchValue, :secretLevel, " +
            ":portal, :account, :urlLevelSetId, :sogouRank, :imageUrl, :md5, :importantLevel,  NOW(), NOW())";

    private static final String INSERT_URL = "replace into url_set (url_level_set_id, greenurl, ctime, utime) values ";

    private static final String UPDATE_SQL = "";

    private static final String DEL = " delete from <table> where rule_id in ";

    private static final String UpdateVersion = "insert into admin (version) values (?);";

    private JdbcTemplate jdbcTemplate;

    private NamedParameterJdbcTemplate namedJdbcTemplate;

    @Autowired
    public void setDataSource(ComboPooledDataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.namedJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public int queryCount(String table, String where) {
        if (table == null) return 0;
        return this.jdbcTemplate.queryForObject(
                "select count(*) from " + table + (where == null ? "" : where), Integer.class);
    }

    public int queryCountWithBind(String table, String where, String ...condition) {
        if (table == null) return 0;
        if (condition == null) {
            return this.jdbcTemplate.queryForObject(
                    "select count(*) from " + table + (where == null ? "" : where), Integer.class);
        }
        return this.jdbcTemplate.queryForObject(
                "select count(*) from " + table + (where == null ? "" : where), Integer.class, condition);
    }

    public Rule getSingleDomain (String table, String where, String ...condition) {
        return this.jdbcTemplate.queryForObject(
                "select * from " + table + (where == null ? "" : where),
                condition,
                (rs, i) -> {
                    Rule rule = new Rule();
                    rule.setAccount(rs.getString("account"));
                    return rule;
                }
        );
    }

    public List<Rule> findAllRule(String table) {
        return this.jdbcTemplate.query("select * from " + table, new ActorMapper());
    }

    public List<Rule> findRuleByIds(String ids, String table, StringBuffer exception) {
        try {
            System.out.println("SELECT * FROM " + table + " WHERE rule_id IN (" + ids + ")");
            return this.jdbcTemplate.query("SELECT * FROM " + table + " WHERE rule_id IN (" + ids + ")", new ActorMapper());
        } catch (Exception e) {
            exception.append(e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void update(Rule rule, String table) {
        this.jdbcTemplate.update("UPDATE " + table + " SET ");
    }

    public void insert(Rule rule, String table) {
        this.jdbcTemplate.update(INSERT_SQL.replace("<table>", table));
    }

    public void delete(String table) {
        this.jdbcTemplate.update("DELETE FROM " + table);
    }

    public String getVersion() {
        SqlRowSet row = this.jdbcTemplate.queryForRowSet("SELECT  version FROM admin");
        if (row.next()) {
            return row.getString("version");
        }
        return "-1";
    }

    public boolean updateVersion (String version) {
        delete("admin");
        return this.jdbcTemplate.update("INSERT  into admin (version) VALUE (" + version + ")") > 0;
    }

    public boolean batchInsert(final List<Rule> rules, String table, StringBuffer exception) throws Exception{
        SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(rules.toArray());
        try {
            int[] count = namedJdbcTemplate.batchUpdate(INSERT_SQL.replace("<table>", table), batch);
            return count != null && count.length == rules.size();
        } catch (Exception e) {
            exception.append(e.getMessage());
            return false;
        }
    }

    public boolean batchInsertImage(final List<Rule> rules, String table, StringBuffer exception) throws Exception{
        SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(rules.toArray());
        try {
            int[] count = namedJdbcTemplate.batchUpdate(INSERT_IMAGE_SQL.replace("<table>", table), batch);
            return count != null && count.length == rules.size();
        } catch (Exception e) {
            exception.append(e.getMessage());
            return false;
        }
    }

    public void clearTables(Set<String> tables) {
        tables.forEach(this::delete);
    }

    private static final class ActorMapper implements RowMapper<Rule> {
        public Rule mapRow(ResultSet rs, int rowNum) throws SQLException {
            Rule rule = new Rule();
            rule.setRuleId(rs.getString("rule_id"));
            rule.setAccount(rs.getString("account"));
            rule.setImportantLevel(rs.getInt("important_level"));
            rule.setWord(rs.getString("data"));
            rule.setMatchMode(rs.getInt("match_mode"));
            rule.setMatchValue(rs.getInt("match_value"));
            rule.setSecretLevel(rs.getInt("secret_level"));
            rule.setPortal(rs.getString("portal"));
            rule.setUrlLevelSetId(rs.getShort("url_level_set_id"));
            return rule;
        }
    }

}

