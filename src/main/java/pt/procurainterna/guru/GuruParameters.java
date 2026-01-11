package pt.procurainterna.guru;

public class GuruParameters {
  public final transient String apiToken;
  public final JdbcConfig jdbcConfig;

  public GuruParameters(String apiToken, JdbcConfig jdbcConfig) {
    this.apiToken = apiToken;
    this.jdbcConfig = jdbcConfig;
  }

}
