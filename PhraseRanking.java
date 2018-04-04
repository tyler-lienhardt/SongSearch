/*
 * PhraseRanking.java
 * 
 * This class is provided a search query and lyrics for a single song.
 * If all the words of the query appear in the song in the correct order
 * (but not necessarily consecutively), PhraseRanking returns a rank for
 * the song, based on how many characters are separating the matching words
 * in the lyrics. Lower ranks are closer matches.
 * 
 * If any of the words are not contained in the song in the correct order,
 * the phrase is ranked -1.
 * 
 * Tyler Lienhardt
 * COS 285 Data Structures
 * Fall 2017
 */

package student;

import java.util.*;

public class PhraseRanking {
	static String[] queryArr;
	static String lyrics;
	static String query;
	
	//returns -1 if song does not contain matching phrase
	static int rankPhrase(String lyricsArg, String queryArg) {
		
		query = queryArg.toLowerCase();
		queryArr = query.split("[^a-z]+");
		lyrics = lyricsArg.toLowerCase();
		
		int fromIndex = 0;
		int i = 0;
		int index = 0;
		int head = 0;
		int tail = 0;
		int rank = 99999;
		
		
		for (;;) {
			index = findWord(queryArr[i], fromIndex);
			
			if (index < 0) {
				if (rank == 99999) {
					return -1;
				}
				return rank;
			}
			
			head = (i == 0)? index : head;
			tail = (i == queryArr.length-1)? index + queryArr[i].length() : head;
			
			if (tail > head){
				int distance = tail - head;
				
				if (distance < rank) {
					rank = distance;
				}
				
				fromIndex = head + 1;
				i = 0;
				continue;
			}
			
			fromIndex = index + queryArr[i].length();
			i++;
		}
	}
	
	static int fastRank(String lyricsArg, String[] queryArr, String[] priorityArr) {
				
		lyrics = lyricsArg;
		
		//if the lyrics contain at least two upper case letters in a row,
		//then toLowerCase must be performed before rough search
		if (hasAllCaps(lyrics)){
			lyrics = lyrics.toLowerCase();
		}
		
		//Rough search. Looks for longest words first.
		//first letter of search query is removed to avoid upper case letters
		for (String query : priorityArr) {			
			if (lyrics.indexOf(query) < 0){
				return -1;
			}
		}
		
		lyrics = lyrics.toLowerCase();
		
		int fromIndex = 0;
		int i = 0;
		int index = 0;
		int head = 0;
		int tail = 0;
		int rank = 99999;
		
		//This loop will gather all possible ranks in a song (if any).
		//A word search returning -1 indicates that no more ranks exist in remaining lyrics.
		//If no ranks have been found, the method returns -1.
		//Otherwise, the lowest rank is returned.
		for (;;) {
			index = findWord(queryArr[i], fromIndex);
			
//			//FIXME test
//			if (index > 0 && index != lyrics.length() - queryArr[i].length()){
//				System.out.println(lyrics.substring(index-1, index + queryArr[i].length()+1));
//			}
			
			if (index < 0) {
				if (rank == 99999) {
					return -1;
				}
				return rank;
			}
			
			head = (i == 0)? index : head;
			tail = (i == queryArr.length-1)? index + queryArr[i].length() : head;
			
			if (tail > head){
				int distance = tail - head;
				
				if (distance < rank) {
					rank = distance;
				}
				
				fromIndex = head + 1;
				i = 0;
				continue;
			}
			
			fromIndex = index + queryArr[i].length();
			i++;
		}
	}
	
	//Determines if indexOf match is a complete word or a partial word.
	//Any match which is sandwiched between non-letters or a string boundary is considered a word.
	private static int findWord(String word, int startIndex){
		int wordIndex,
			prevChar,
			nextChar;
		
		for (;;)  {
			wordIndex = lyrics.indexOf(word, startIndex);
			if (wordIndex < 0) return -1;
			
			prevChar = (wordIndex == 0)? '#' : lyrics.charAt(wordIndex - 1);
			nextChar = (wordIndex == lyrics.length() - word.length())? '#' : lyrics.charAt(wordIndex + word.length());
			
			//if neighboring chars are lowercase letters, search again further in lyrics
			if ((prevChar >= 'a' && prevChar <= 'z') || (nextChar >= 'a' && nextChar <= 'z')) {
				startIndex = wordIndex + 1;
				wordIndex = lyrics.indexOf(word, startIndex);
			}
			else {
				return wordIndex;
			}
		}
	}	
	
	private static boolean hasAllCaps(String lyrics){
		
		for (int i = 0; i < lyrics.length()-1; i++){
			char c = lyrics.charAt(i);
			if (c >= 'A' && c <= 'Z'){
				char nextc = lyrics.charAt(i+1);
				if (nextc >= 'A' && nextc <= 'Z'){
					return true;
				}
			}
		}
		
		return false;
	}
	
	//prepares search query for rough search. 
	//-Removes duplicate words,
	//-Removes words < 3 chars,
	//-removes first letter of each word (to avoid case matching),
	//-and puts longest words first.
	public static String[] prioritizeWords(String[] a){
		Comparator<String> cmp = new CmpWord();
		
		//get rid of duplicates and small words
		HashSet<String> hs = new HashSet<>();
		for (String word : a){
			if (word.length() > 3){
				hs.add(word);
			}	
		}
		
		//chop off first letter of word and arrange by length
		PriorityQueue<String> pq = new PriorityQueue<>(a.length, cmp);
		for (String word : hs){			
			pq.add(word.substring(1, word.length()));	
		}
		
		String[] pArr = new String[pq.size()];
		for (int i = 0; i < pArr.length; i++){
			pArr[i] = pq.poll();
			//System.out.println(pArr[i]);
		}
		
		return pArr;
	}

	public static void printResults(LinkedHashMap<Song, Integer> results){
		int i = 0;
		for (Map.Entry<Song, Integer> entry : results.entrySet()) {
			System.out.println(entry.getValue() + " " + ((Song) entry.getKey()).getArtist() + " \"" + ((Song) entry.getKey()).getTitle() + "\"");
			if (++i > 9) break;
		}
		
		System.out.println("... total of " + results.size() + " songs");
	}
	
	public static class CmpWord implements Comparator<String>{
		public int compare(String s1, String s2){
			if (s1.length() < s2.length())
				return 1;
			else if (s1.length() > s2.length())
				return -1;
			else
				return 0;
		}
	}
	
	public static void main(String[] args){		
		Song[] songs;
		LinkedHashMap<Song, Integer> results = new LinkedHashMap<>();
				
		String[] queryArr = args[1].toLowerCase().split("[^a-z]+");
		String[] pArr = prioritizeWords(queryArr);

		SongCollection sc = new SongCollection(args[0]);
		songs = sc.getAllSongs();
			
		//System.out.println("Searching for: " + args[1]);
        for (Song song : songs){        	
			int rank = rankPhrase(song.getLyrics(), args[1]);
        	//int rank = fastRank(song.getLyrics(), queryArr, pArr);       	
        	
			if (rank > 0) {
//				System.out.println(song.getArtist() + " - " + song.getTitle());
				results.put(song, rank);
			}
		}
       
		printResults(results);	
	}
}
