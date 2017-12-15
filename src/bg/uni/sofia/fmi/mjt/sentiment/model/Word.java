package bg.uni.sofia.fmi.mjt.sentiment.model;

public class Word
{

    private double sentimentScore;

    private int numberOfReviews;


    public Word(int sentimentScore)
    {
        this.numberOfReviews = 0;
        this.sentimentScore = 0;
        this.setSentimentScore(sentimentScore);
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

}
