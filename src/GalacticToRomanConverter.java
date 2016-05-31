import java.util.Map;

public class GalacticToRomanConverter implements Converter
{

  @Override
  public String converter( String[] string, Map<String, String> mappings )
  {
    StringBuilder sb = new StringBuilder();
    for( String each : string )
    {
      if( mappings.containsKey( each ) )
      {
        sb.append( mappings.get( each ) );
      }
    }
    return sb.toString();
  }
}
