package bg.uni.sofia.fmi.mjt.sentiment;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import bg.uni.sofia.fmi.mjt.sentiment.model.Word;


public class MovieReviewSentimentAnalyzer implements SentimentAnalyzer
{

    Set<String> stopWords;

    Map<String, Word> sentimentDictionary;


    public MovieReviewSentimentAnalyzer(String reviewsFileName, String stopWordsFileName)
    {
        this.readStopWords(stopWordsFileName);
        this.readReviewsFileName(reviewsFileName);
    }


    private void readReviewsFileName(String reviewsFileName)
    {
        sentimentDictionary = new HashMap<String, Word>();
        List<String> reviews;
        try (Stream<String> stream = Files.lines(Paths.get(reviewsFileName)))
        {
            reviews = stream.map(String::toLowerCase).map(l -> l.replaceAll("[.,?!;:`'\"]", "")).map(String::trim)
                    .collect(Collectors.toList());
            for (String review : reviews)
            {
                putWordsFromReviewInDictionary(review);
            }
        }
        catch (IOException e)
        {
            System.out.println("Error reading reviews.");
        }
    }


    private void putWordsFromReviewInDictionary(String review)
    {
        String[] wordsInReview = review.split("[ ]");
        int score = Integer.parseInt(wordsInReview[0]);
        for (int i = 1; i < wordsInReview.length; ++i)
        {
            wordsInReview[i] = wordsInReview[i].trim();
            if (!stopWords.contains(wordsInReview[i]) && !wordsInReview[i].isEmpty())
            {
                if (sentimentDictionary.containsKey(wordsInReview[i]))
                {
                    sentimentDictionary.get(wordsInReview[i]).setSentimentScore(score);
                }
                else
                {
                    sentimentDictionary.put(wordsInReview[i], new Word(wordsInReview[i], score));
                }
            }
        }
    }


    private void readStopWords(String stopWordsFileName)
    {
        try (Stream<String> stream = Files.lines(Paths.get(stopWordsFileName)))
        {
            stopWords = stream.map(String::toLowerCase).map(String::trim).map(l -> l.replace("\'", ""))
                    .collect(Collectors.toSet());
        }
        catch (IOException e)
        {
            System.out.println("Error reading stopwords.");
        }
    }


    @Override
    public double getReviewSentiment(String review)
    {
        List<String> wordsWithReviews = getWordsWithReviews(review);
        System.out.println(wordsWithReviews);
        double score = 0;
        for (String word : wordsWithReviews)
        {
            if (sentimentDictionary.containsKey(word))
            {
                score += sentimentDictionary.get(word).getSentimentScore();
            }
        }
        return wordsWithReviews.isEmpty() ? -1 : score / wordsWithReviews.size();
    }


    private List<String> getWordsWithReviews(String review)
    {
        String words = review.toLowerCase().replaceAll("[.,?!;:`'\"]", "").trim();
        String delimiter = " ";
        return Pattern.compile(delimiter).splitAsStream(words).filter(s -> !stopWords.contains(s))
                .collect(Collectors.toList());
    }


    @Override
    public String getReviewSentimentAsName(String review)
    {
        double sentimentScore = getReviewSentiment(review);
        switch ((int) Math.round(sentimentScore))
        {
            case 0:
                return "negative";
            case 1:
                return "somewhat negative";
            case 2:
                return "neutral";
            case 3:
                return "somewhat positive";
            case 4:
                return "positive";
            default:
                return "";
        }
    }


    @Override
    public double getWordSentiment(String word)
    {
        return sentimentDictionary.containsKey(word.toLowerCase())
                ? sentimentDictionary.get(word.toLowerCase()).getSentimentScore()
                : -1;
    }


    @Override
    public Collection<String> getMostFrequentWords(int n)
    {
        if (n >= sentimentDictionary.size())
        {
            return new ArrayList<String>();
        }

        return sentimentDictionary.entrySet().stream().sorted(new Comparator<Entry<String, Word>>()
        {

            @Override
            public int compare(Entry<String, Word> entryOne, Entry<String, Word> entryTwo)
            {
                if (entryTwo.getValue().getNumberOfReviews() < entryOne.getValue().getNumberOfReviews())
                {
                    return -1;
                }
                if (entryTwo.getValue().getNumberOfReviews() > entryOne.getValue().getNumberOfReviews())
                {
                    return 1;
                }
                return 0;
            }

        }).map(Map.Entry::getKey).limit(n).collect(Collectors.toCollection(ArrayList::new));
    }


    @Override
    public Collection<String> getMostPositiveWords(int n)
    {
        if (n >= sentimentDictionary.size())
        {
            return new ArrayList<String>();
        }

        return sentimentDictionary.entrySet().stream().sorted(new Comparator<Entry<String, Word>>()
        {

            @Override
            public int compare(Entry<String, Word> entryOne, Entry<String, Word> entryTwo)
            {
                if (entryTwo.getValue().getSentimentScore() < entryOne.getValue().getSentimentScore())
                {
                    return -1;
                }
                if (entryTwo.getValue().getSentimentScore() > entryOne.getValue().getSentimentScore())
                {
                    return 1;
                }
                return 0;
            }

        }).map(Map.Entry::getKey).limit(n).collect(Collectors.toCollection(ArrayList::new));
    }


    @Override
    public Collection<String> getMostNegativeWords(int n)
    {
        if (n >= sentimentDictionary.size())
        {
            return new ArrayList<String>();
        }

        return sentimentDictionary.entrySet().stream().sorted(new Comparator<Entry<String, Word>>()
        {

            @Override
            public int compare(Entry<String, Word> entryOne, Entry<String, Word> entryTwo)
            {
                if (entryTwo.getValue().getSentimentScore() > entryOne.getValue().getSentimentScore())
                {
                    return -1;
                }
                if (entryTwo.getValue().getSentimentScore() < entryOne.getValue().getSentimentScore())
                {
                    return 1;
                }
                return 0;
            }

        }).map(Map.Entry::getKey).limit(n).collect(Collectors.toCollection(ArrayList::new));
    }


    @Override
    public int getSentimentDictionarySize()
    {
        return sentimentDictionary.size();
    }


    @Override
    public boolean isStopWord(String word)
    {
        word = word.toLowerCase().replaceAll("'", "").trim();
        return stopWords.contains(word);
    }

}
