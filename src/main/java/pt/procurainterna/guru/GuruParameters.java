package pt.procurainterna.guru;

public class GuruParameters {
  public final transient String apiToken;
  public final String roleToAssign;

  public GuruParameters(String apiToken, String roleToAssign) {
    this.apiToken = apiToken;
    this.roleToAssign = roleToAssign;
  }
}
