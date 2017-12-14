package bg.uni.sofia.fmi.mjt.sentiment.model;

public class Word implements Comparable<Word>
{

    private final String content;

    private double sentimentScore;

    private int numberOfReviews;


    public Word(String word, int sentimentScore)
    {
        this.content = word;
        this.numberOfReviews = 0;
        this.sentimentScore = 0;
        this.setSentimentScore(sentimentScore);
    }


    @Override
    public int compareTo(Word o)
    {
        return (int) Math.round(this.sentimentScore - o.getSentimentScore());
    }


    public double getSentimentScore()
    {
        return sentimentScore;
    }


    public int getNumberOfReviews()
    {
        return numberOfReviews;
    }


    public void setSentimentScore(double newSentimentScore)
    {
        this.sentimentScore = this.sentimentScore + ((newSentimentScore - this.sentimentScore) / ++numberOfReviews);
    }


    public String getContent()
    {
        return this.content;
    }


    @Override
    public String toString()
    {
        return "[" + this.content + ", " + this.sentimentScore + "]";
    }

}
