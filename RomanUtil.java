
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RomanUtil
{
  public static boolean isValidRoman( String romanStr )
  {
    return check( romanStr );
  }

  private static final Pattern pattern = Pattern.compile( "(^(?=[MDCLXVI])M*(C[MD]|D?C{0,3})(X[CL]|L?X{0,3})(I[XV]|V?I{0,3})$)" );

  private static boolean check( String romanStr )
  {
    Matcher m = pattern.matcher( romanStr );
    boolean valid = false;
    if( m.matches() )
    {
      valid = true;
    }
    return valid;
  }

  public static int romanToDecimal( String romanNumber )
  {
    int decimal = 0;
    int lastNumber = 0;
    String romanNumeral = romanNumber.toUpperCase();

    if( isValidRoman( romanNumeral ) )
    {
      for( int x = romanNumeral.length() - 1; x >= 0; x-- )
      {
        char convertToDecimal = romanNumeral.charAt( x );

        switch( convertToDecimal )
        {
          case 'M':
            decimal = processDecimal( 1000, lastNumber, decimal );
            lastNumber = 1000;
            break;

          case 'D':
            decimal = processDecimal( 500, lastNumber, decimal );
            lastNumber = 500;
            break;

          case 'C':
            decimal = processDecimal( 100, lastNumber, decimal );
            lastNumber = 100;
            break;

          case 'L':
            decimal = processDecimal( 50, lastNumber, decimal );
            lastNumber = 50;
            break;

          case 'X':
            decimal = processDecimal( 10, lastNumber, decimal );
            lastNumber = 10;
            break;

          case 'V':
            decimal = processDecimal( 5, lastNumber, decimal );
            lastNumber = 5;
            break;

          case 'I':
            decimal = processDecimal( 1, lastNumber, decimal );
            lastNumber = 1;
            break;
        }
      }
    }
    return decimal;
  }

  public static int processDecimal( int decimal, int lastNumber, int lastDecimal )
  {
    if( lastNumber > decimal )
    {
      return lastDecimal - decimal;
    }
    else
    {
      return lastDecimal + decimal;
    }
  }

}
