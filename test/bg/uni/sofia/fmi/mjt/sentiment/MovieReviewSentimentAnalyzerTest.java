package bg.uni.sofia.fmi.mjt.sentiment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

import bg.uni.sofia.fmi.mjt.sentiment.model.Word;


public class MovieReviewSentimentAnalyzerTest
{

    private static final double EPSILON = 0.01;

    private static final String REVIEWS_FILENAME = "movieReviews.txt";

    private static final String STOPWORDS_FILENAME = "stopwords.txt";

    private static final String[] REAL_STOPWORDS = new String[] { "too", "wasn't", "you'll" };

    private static final String[] NOT_STOPWORDS = new String[] { "within", "anywhere", "bee" };

    private static MovieReviewSentimentAnalyzer mrsa;


    @BeforeClass
    public static void initialize()
    {
        // first with wrong names, after with real
        mrsa = new MovieReviewSentimentAnalyzer("wrong.txt", "wrong.txt");
        mrsa = new MovieReviewSentimentAnalyzer(REVIEWS_FILENAME, STOPWORDS_FILENAME);
    }


    @Test
    public void testWordSentimentScoreDefault()
    {
        Word theodora = new Word(5);
        theodora.setSentimentScore(4);
        theodora.setSentimentScore(5);
        theodora.setSentimentScore(3);
        theodora.setSentimentScore(2);
        assertEquals("Correct math?", 3.8, theodora.getSentimentScore(), EPSILON);
    }


    @Test
    public void testRealStopWords()
    {
        for (String stopWords : REAL_STOPWORDS)
        {
            assertTrue(mrsa.isStopWord(stopWords));
        }
    }


    @Test
    public void testNotStopWords()
    {
        for (String stopWords : NOT_STOPWORDS)
        {
            assertFalse(mrsa.isStopWord(stopWords));
        }
    }


    @Test
    public void testWordSentimentScore()
    {
        assertEquals("Correct math self-reflection?", 2.5, mrsa.getWordSentiment("self-reflection"), EPSILON);
        assertEquals("Correct math dated?", 1.5, mrsa.getWordSentiment("dated"), EPSILON);
        assertEquals("Correct math updated?", 3, mrsa.getWordSentiment("updated"), EPSILON);
        assertEquals("Correct math commercial?", 1.8, mrsa.getWordSentiment("commercial"), EPSILON);

        assertEquals("Correct math skateboards?", 4.0, mrsa.getWordSentiment("skateboards"), EPSILON);
        assertEquals("Correct math staggering?", 4.0, mrsa.getWordSentiment("staggering"), EPSILON);
        assertEquals("Correct math achievements?", 3.5, mrsa.getWordSentiment("achievements"), EPSILON);
        assertEquals("Correct math popular?", 3.25, mrsa.getWordSentiment("popular"), EPSILON);
        assertEquals("Correct math spells?", 3.0, mrsa.getWordSentiment("spells"), EPSILON);
        assertEquals("Correct math international?", 2.6, mrsa.getWordSentiment("international"), EPSILON);
        assertEquals("Correct math snowball?", 2.0, mrsa.getWordSentiment("snowball"), EPSILON);
        assertEquals("Correct math dude?", 1.75, mrsa.getWordSentiment("dude"), EPSILON);
        assertEquals("Correct math mediterranean?", 1.0, mrsa.getWordSentiment("mediterranean"), EPSILON);
        assertEquals("Correct math cash?", 0.0, mrsa.getWordSentiment("cash"), EPSILON);
    }


    @Test
    public void testReviewSentimentScoreAsText()
    {
        assertEquals("negative", mrsa.getReviewSentimentAsName("Dire disappointment: dull and unamusing freakshow"));
        assertEquals("positive", mrsa.getReviewSentimentAsName("Immersive ecstasy: energizing artwork!"));
        assertEquals("somewhat positive", mrsa.getReviewSentimentAsName("artfully! Ghandi: tattoos?"));
        assertEquals("somewhat negative", mrsa.getReviewSentimentAsName("weak"));
        assertEquals("neutral", mrsa.getReviewSentimentAsName("quick"));
        assertEquals("", mrsa.getReviewSentimentAsName("hfhfhfhfhhfhfhfhfhfhhfhfhfhfhfh"));
    }


    @Test
    public void testReviewSentimentScore()
    {
        System.out.println(mrsa.getReviewSentiment("A weak script that ends with a quick and boring finale."));
    }


    @Test
    public void testMostNegativeWords()
    {
        System.out.println(mrsa.getMostNegativeWords(3));
    }


    @Test
    public void testMostPositiveWords()
    {
        System.out.println(mrsa.getMostPositiveWords(3));
    }


    @Test
    public void testMostFrequentWords()
    {
        System.out.println(mrsa.getMostFrequentWords(3));
    }


    @Test
    public void testMostFrequentWordsTopTen()
    {
        assertTrue(mrsa.getMostFrequentWords(10).contains("movie"));
    }


    @Test
    public void testMostPositivetWordsTopThousand()
    {
        for (String word : mrsa.getMostPositiveWords(1000))
        {
            assertEquals(4.0, mrsa.getWordSentiment(word), EPSILON);
        }
    }


    @Test
    public void testMostNegativetWordsTopThousand()
    {
        for (String word : mrsa.getMostNegativeWords(1000))
        {
            assertEquals(0.0, mrsa.getWordSentiment(word), EPSILON);
        }
    }


    @Test
    public void testSizeOfWordDictionary()
    {
        int size = mrsa.getSentimentDictionarySize() + 1;
        mrsa.getMostNegativeWords(size);
        mrsa.getMostFrequentWords(size);
        mrsa.getMostPositiveWords(size);
    }
}
