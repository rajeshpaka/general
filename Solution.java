
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Solution
{
  private static String HOWPATTERN = "(?:how)(.*)";
  private static String CREDITPATTERN = "(.*)\\s+(Silver|Gold|Iron)\\s+(?:is)\\s+(\\d+)\\s+(?:Credits)";
  private static String MAPPATTERN = "(glob|prok|tegj|pish)\\s+(?:is\\s+)(\\w+)";

  public static void main( String[] args )
  {
    List<String> list = readInputFile( args );
    String[] inputs = list.toArray( new String[list.size()] );
    process( inputs );
  }

  private static List<String> readInputFile( String[] args )
  {
    List<String> list = null;
    URI uri = null;
    try
    {
      if( !(args.length > 0) )
      {
        uri = ClassLoader.getSystemResource( "input.txt" ).toURI();
      }
      else
      {
        uri = new File( args[0] ).toURI();
      }

      list = Files.readAllLines( Paths.get( uri ) );
    }
    catch( IOException | URISyntaxException e )
    {
      System.err.println( "File not found exception at " + uri );
    }
    return list;
  }

  private static void process( String[] inputs )
  {

    Map<String, Double> metalsCostMap = new HashMap<>();
    List<Question> howQuestions = new ArrayList<>();
    List<Credits> allCredit = new ArrayList<>();
    Map<String, String> galacticToRomanMapping = new HashMap<String, String>();

    for( String input : inputs )
    {
      Pattern p = Pattern.compile( HOWPATTERN );
      Matcher m = p.matcher( input );
      if( m.matches() )
      {
        Question question = new Question( m.group( 1 ) );
        howQuestions.add( question );
        continue;
      }

      Pattern p1 = Pattern.compile( CREDITPATTERN );
      Matcher m1 = p1.matcher( input );

      if( m1.matches() )
      {
        Credits credit = new Credits( m1.group( 1 ).split( "\\s+" ), m1.group( 2 ), Long.parseLong( m1.group( 3 ) ) );
        allCredit.add( credit );
        continue;
      }

      Pattern p2 = Pattern.compile( MAPPATTERN );
      Matcher m2 = p2.matcher( input );
      if( m2.matches() )
      {
        galacticToRomanMapping.put( m2.group( 1 ), m2.group( 2 ) );
        continue;
      }
    }

    Iterator<Credits> creditRuleIterator = allCredit.iterator();

    while( creditRuleIterator.hasNext() )
    {
      Credits credit = creditRuleIterator.next();
      String[] galacticArray = credit.getGalacticNumbers();
      GalacticToRomanToDecimalConverter converter = new GalacticToRomanToDecimalConverter();
      long romanNumber = converter.converter( galacticArray, galacticToRomanMapping );
      // System.out.println(Arrays.toString(galacticArray) + " ::: " + romanNumber);
      double metalCost = credit.getAmount() * 1f / romanNumber;
      metalsCostMap.put( credit.getMetal(), metalCost );
    }

    Iterator<Question> howIterator = howQuestions.iterator();

    while( howIterator.hasNext() )
    {
      Question inputQuestion = howIterator.next();
      Pattern p = Pattern.compile( ".*(?:is\\s+)(.*)" );
      Matcher m = p.matcher( inputQuestion.toString() );
      String question = null;
      if( m.matches() )
      {
        question = m.group( 1 );
      }
      else
      {
        System.out.println( "I have no idea what you are talking about" );
        // Don't process further if the question is incorrect.
        continue;
      }
      
      String[] questionArr = question.split( "\\s+" );
      StringBuilder roman = new StringBuilder();
      double metalCost = 1;
      // Display 'Credits' in the output in case of Metal.
      boolean displayCredits = false;
      for( String str : questionArr )
      {
        if( galacticToRomanMapping.containsKey( str ) )
        {
          roman.append( galacticToRomanMapping.get( str ) );
        }
        else if( metalsCostMap.containsKey( str ) )
        {
          metalCost = metalsCostMap.get( str );
          displayCredits = true;
        }
      }

      long romanNumber = RomanUtil.romanToDecimal( roman.toString() );
      double total = romanNumber * metalCost;

      StringBuilder outputBuilder = new StringBuilder();
      outputBuilder.append( question.replace( "?", "is " ) );
      outputBuilder.append( (total + "").replace( ".0", "" ) );
      if( displayCredits )
      {
        outputBuilder.append( " Credits" );
      }
      System.out.println( outputBuilder );
    }

    // System.out.println(mapping);
    // System.out.println(allCredit);
    // System.out.println(metalsCostMap);
    // System.out.println(howQuestions);
  }
}

interface Converter
{
  public int converter( String[] string, Map<String, String> mappings );
}

class GalacticToRomanToDecimalConverter implements Converter
{

  @Override
  public int converter( String[] string, Map<String, String> mappings )
  {
    StringBuilder sb = new StringBuilder();
    for( String each : string )
    {
      if( mappings.containsKey( each ) )
      {
        sb.append( mappings.get( each ) );
      }
    }
    return RomanUtil.romanToDecimal( sb.toString() );
  }
}

class Credits
{
  private String[] _galacticNumbers;
  private String _metal;
  private Long _amount;

  public String[] getGalacticNumbers()
  {
    return _galacticNumbers;
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
    _galacticNumbers = val1_;
    _metal = metal_;
    _amount = amount_;
  }

  public String toString()
  {
    return Arrays.toString( getGalacticNumbers() ) + " " + getMetal() + " " + getAmount();
  }
}

class Question
{
  private String _question;

  public Question( String question )
  {
    _question = question;
  }

  public String toString()
  {
    return _question;
  }
}