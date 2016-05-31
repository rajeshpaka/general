
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Solution to Merchant Guide to the Galaxy problem.
 * @author Rajesh Paka
 *
 */
public class Solution
{
  private static String HOWPATTERN = "(?:how)(.*)";
  private static String HOWPATTERNINDEEP = ".*(?:is\\s+)(.*)";
  private static String CREDITPATTERN = "(.*)\\s+(Silver|Gold|Iron)\\s+(?:is)\\s+(\\d+)\\s+(?:Credits)";
  private static String MAPPATTERN = "(glob|prok|tegj|pish)\\s+(?:is\\s+)(\\w+)";

  /**
   * Main method.
   * @param args if input is passed as an argument, it respects it else it will take the default input.txt file.
   */
  public static void main( String[] args )
  {
    List<String> list = readInputFile( args );
    String[] inputs = list.toArray( new String[list.size()] );
    process( inputs );
  }

  /**
   * Method read the input file and returns List of all lines in a file.
   * @param args can be null/an value where the input is expected to be read from.
   * @return List of lines
   */
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

  /**
   * Most of the core logic goes into this method.
   * @param inputs list of input strings.
   */
  
  private static void process( String[] inputs )
  {

    Map<String, Double> metalsCostMap = new HashMap<>();
    List<Question> howQuestions = new ArrayList<>();
    List<Credits> allCredit = new ArrayList<>();
    Map<String, String> galacticToRomanMapping = new HashMap<String, String>();

    for( String input : inputs )
    {
      Pattern howpattrn = Pattern.compile( HOWPATTERN );
      Matcher howmatchr = howpattrn.matcher( input );
      if( howmatchr.matches() )
      {
        Question question = new Question( howmatchr.group( 1 ) );
        howQuestions.add( question );
        continue;
      }

      Pattern creditpattrn = Pattern.compile( CREDITPATTERN );
      Matcher creditmatchr = creditpattrn.matcher( input );

      if( creditmatchr.matches() )
      {
        Credits credit = new Credits( creditmatchr.group( 1 ).split( "\\s+" ), creditmatchr.group( 2 ), Long.parseLong( creditmatchr.group( 3 ) ) );
        allCredit.add( credit );
        continue;
      }

      Pattern galToRomanPattrn = Pattern.compile( MAPPATTERN );
      Matcher galToRomanMatchr = galToRomanPattrn.matcher( input );
      if( galToRomanMatchr.matches() )
      {
        galacticToRomanMapping.put( galToRomanMatchr.group( 1 ), galToRomanMatchr.group( 2 ) );
        continue;
      }
    }

    // Iterate through all the credit rules and form to build the required metalCost maps.
    Iterator<Credits> creditRuleIterator = allCredit.iterator();

    while( creditRuleIterator.hasNext() )
    {
      Credits credit = creditRuleIterator.next();
      double metalCost = calculateMetalCost( credit, galacticToRomanMapping );
      metalsCostMap.put( credit.getMetal(), metalCost );
    }

    // Iterate through all the question one by one and display the result.
    Iterator<Question> howIterator = howQuestions.iterator();
    while( howIterator.hasNext() )
    {
      Question inputQuestion = howIterator.next();
      Pattern p = Pattern.compile( HOWPATTERNINDEEP );
      Matcher m = p.matcher( inputQuestion.toString() );
      String question = null;
      if( m.matches() )
      {
        question = m.group( 1 );
      }
      else
      {
        System.out.println( "I have no idea what you are talking about" );
        // Don't process further if the input question is incorrect.
        continue;
      }

      // Split the question to find out the Galactic String and Convert them to Corresponding Roman
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

      String output = buildOutput( roman, metalCost, question, displayCredits );
      System.out.println( output );
    }
  }

  /**
   * Calculates metal cost from the given Credit.
   * 
   * @param credit
   *          Credit object which has whole credit input statement.
   * @param galacticToRomanMapping
   *          mapping between Galactic to Roman
   * @return metal cost.
   */

  private static double calculateMetalCost( Credits credit, Map<String, String> galacticToRomanMapping )
  {
    String[] galacticStringArray = credit.getGalacticStrings();
    GalacticToRomanConverter galToRoman = new GalacticToRomanConverter();
    long romanNumber = RomanUtil.romanToDecimal( galToRoman.converter( galacticStringArray, galacticToRomanMapping ) );
    double metalCost = credit.getAmount() * 1f / romanNumber;
    return metalCost;
  }

  /**
   * Takes the Roman number and convert them to Decimal system and builds the output.
   * 
   * @param roman
   *          roman number
   * @param metalCost
   *          In case of no metal cost is 1, else metal cost.
   * @param question
   *          Actual question used for build the output string.
   * @param displayCredits
   *          flag to display Credits in the output or not.
   * @return output to be displayed.
   */
  private static String buildOutput( StringBuilder roman, double metalCost, String question, boolean displayCredits )
  {
    long romanNumber = RomanUtil.romanToDecimal( roman.toString() );
    double total = romanNumber * metalCost;

    StringBuilder outputBuilder = new StringBuilder();
    outputBuilder.append( question.replace( "?", "is " ) );
    outputBuilder.append( (total + "").replace( ".0", "" ) );
    if( displayCredits )
    {
      outputBuilder.append( " Credits" );
    }
    return outputBuilder.toString();
  }
}
