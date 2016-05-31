import java.util.Arrays;

public class Credits
{
  private String[] _galacticStrings;
  private String _metal;
  private Long _amount;

  public String[] getGalacticStrings()
  {
    return _galacticStrings;
  }

  public String getMetal()
  {
    return _metal;
  }

  public Long getAmount()
  {
    return _amount;
  }

  public Credits( String[] val1_, String metal_, long amount_ )
  {
    _galacticStrings = val1_;
    _metal = metal_;
    _amount = amount_;
  }

  public String toString()
  {
    return Arrays.toString( getGalacticStrings() ) + " " + getMetal() + " " + getAmount();
  }
}
