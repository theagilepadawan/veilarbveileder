package no.nav.fo.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabaseConfig {
    /*@Bean
    public DataSource oracleDataSource() throws ClassNotFoundException, NamingException {
        return new JndiTemplate().lookup("java:/jboss/datasources/veilarbportefolje", DataSource.class);
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) throws NamingException, SQLException, IOException {
        return new JdbcTemplate(dataSource);
    }*/

//    @Bean
//    public DataSourceTransactionManager transactionManager(DataSource dataSource) throws IOException, SQLException {
//        return new DataSourceTransactionManager(dataSource);
//    }

   /* @Bean
    public Pingable dbPinger(final DataSource ds) {
        return () -> {
            try {
                SQL.query(ds, new RowMapper.IntMapper(), "select count(1) from dual");
                return Pingable.Ping.lyktes("DATABASE");
            } catch (Exception e) {
                return Pingable.Ping.feilet("DATABASE", e);
            }
        };
    }*/

}
