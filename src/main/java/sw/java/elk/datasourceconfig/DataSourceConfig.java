package sw.java.elk.datasourceconfig;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.TransactionIsolationLevel;
import org.apache.ibatis.transaction.Transaction;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.managed.ManagedTransaction;
import org.apache.ibatis.transaction.managed.ManagedTransactionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.transaction.SpringManagedTransactionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Configuration
@MapperScan(basePackages = "sw.java.elk.dao", sqlSessionFactoryRef = "SqlSessionFactory")
public class DataSourceConfig {
    @Primary
    @Bean(name = "DataSourcePrimary")
    @ConfigurationProperties(prefix = "spring.datasource.primary")
    public DataSource getDateSource1() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "DataSourceSecond")
    @ConfigurationProperties(prefix = "spring.datasource.second")
    public DataSource getDateSource2() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "dynamicDataSource")
    public DynamicDataSource DataSource(@Qualifier("DataSourcePrimary") DataSource test1DataSource,
                                        @Qualifier("DataSourceSecond") DataSource test2DataSource) {
        Map<Object, Object> targetDataSource = new HashMap<>();
        targetDataSource.put(DataSourceType.DataBaseType.DATA01, test1DataSource);
        targetDataSource.put(DataSourceType.DataBaseType.DATA02, test2DataSource);
        DynamicDataSource dataSource = new DynamicDataSource();
        dataSource.setTargetDataSources(targetDataSource);
        dataSource.setDefaultTargetDataSource(test1DataSource);
        return dataSource;
    }
    @Bean(name = "SqlSessionFactory")
    public SqlSessionFactory test1SqlSessionFactory(@Qualifier("dynamicDataSource") DynamicDataSource dynamicDataSource)
            throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dynamicDataSource);
        bean.setMapperLocations(
                new PathMatchingResourcePatternResolver().getResources("classpath*:mapper/*.xml"));
        return bean.getObject();
    }
}
