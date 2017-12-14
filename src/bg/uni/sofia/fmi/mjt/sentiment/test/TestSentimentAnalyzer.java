package bg.uni.sofia.fmi.mjt.sentiment.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

import bg.uni.sofia.fmi.mjt.sentiment.MovieReviewSentimentAnalyzer;
import bg.uni.sofia.fmi.mjt.sentiment.model.Word;


public class TestSentimentAnalyzer
{

    private static final String REVIEWS_FILENAME = "movieReviews.txt";

    private static final String STOPWORDS_FILENAME = "stopwords.txt";

    private static final String[] REAL_STOPWORDS = new String[] { "too", "wasn't", "you'll" };

    private static final String[] NOT_STOPWORDS = new String[] { "within", "anywhere", "bee" };

    private static MovieReviewSentimentAnalyzer mrsa;


    @BeforeClass
    public static void initialize()
    {
        mrsa = new MovieReviewSentimentAnalyzer(REVIEWS_FILENAME, STOPWORDS_FILENAME);
    }


    @Test
    public void testWordSentimentScoreDefault()
    {
        Word theodora = new Word("Theodora", 5);
        theodora.setSentimentScore(4);
        theodora.setSentimentScore(5);
        theodora.setSentimentScore(3);
        theodora.setSentimentScore(2);
        assertEquals("Correct math?", 3.8, theodora.getSentimentScore(), 0.01);
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
        assertEquals("Correct math self-reflection?", 2.5, mrsa.getWordSentiment("self-reflection"), 0.01);
        assertEquals("Correct math dated?", 1.5, mrsa.getWordSentiment("dated"), 0.01);
        assertEquals("Correct math dated?", 3, mrsa.getWordSentiment("updated"), 0.01);
        assertEquals("Correct math dated?", 1.8, mrsa.getWordSentiment("commercial"), 0.01);
    }


    @Test
    public void testReviewSentimentScore()
    {
        assertEquals("negative", mrsa.getReviewSentimentAsName("Dire disappointment: dull and unamusing freakshow"));
        assertEquals("positive", mrsa.getReviewSentimentAsName("Immersive ecstasy: energizing artwork!"));
        assertEquals("somewhat positive", mrsa.getReviewSentimentAsName("artfully! Ghandi: tattoos?"));
    }


    @Test
    public void testMostNegativeWords()
    {
        System.out.println("Negative: ");
        System.out.println(mrsa.getMostNegativeWords(3));
        System.out.println(mrsa.getMostNegativeWords(40));
    }


    @Test
    public void testMostPositiveWords()
    {
        System.out.println("Positive: ");
        System.out.println(mrsa.getMostPositiveWords(3));
        System.out.println(mrsa.getMostPositiveWords(40));
    }


    @Test
    public void testMostFrequentWords()
    {
        System.out.println("Frequent: ");
        System.out.println(mrsa.getMostFrequentWords(3));
        System.out.println(mrsa.getMostFrequentWords(40));
    }
}
