# Index Creation
This project focuses on making an inverted index and the comparative analysis of performance of index made using two different approaches.
An inverted index (also referred to as a postings file or inverted file) is a database index storing a mapping from content, such as words or numbers, to its locations in a table, or in a document or a set of documents (named in contrast to a forward index, which maps from documents to content). The purpose of an inverted index is to allow fast full-text searches, at a cost of increased processing when a document is added to the database. It is the most popular Data Structure used in document retrieval systems.

# Purpose
The purpose of this project was to make an inverted index from the corpus, which contains list of documents. It will establish the understanding of how the web makes an index from all the web pages. And how do we get the documents after writing a query on search engine. 

It contains three parts:

Part 1. Making an index using Hashmaps or dictionary

Part 2. Making an index without Hashmaps or dictionary ( by sorting termid-docid pairs). 
The difference in time of the execution of above two parts makes it evident that termid-docid pairs should be sorted.

Part 3. Write a word as command line argument. We will get the termid, frequency of term in corpus and no of documents containing the term.

![image](https://user-images.githubusercontent.com/55246052/120069101-8b09b600-c09d-11eb-8142-a5c02a9731fd.png)


# Execution Instructions
Use Netbeans/Eclipse/IntelliJ IDE and attach the libraries from libraries folder.

Java 8 or higher

## Part 1 ##
1.First give corpus path as command line argument.(for part 2 and 3 i have hardcoded file path)

2.If corpus exists then it will read all files in corpus and assign termids and docids.

3.It will write terms and their termids in termids.txt 

4.It will write documents and their ids in docids.txt 

5.The path of stoplist.txt,termids.txt,docids.txt are hard coded. Please change them while runing code.

## Part 2 ##
1. Run main for Part2_UsingHashMap.txt to create invertedIndex by using Hashmap.

2. Run main for Part2_WithoutHashMap.txt to create invertedIndex by using Sorting of termid,docid pairs.

## Part 3 ##
Give to word to be searched as command line argument.

It is with respest to Part2_UsingHashMap.txt



# Internal Working

## Step 1: Creating termids.txt and docids.txt ##

1. Accept a directory name as a command line argument, and process all files found in that
directory

2. Extract the document text with an HTML parsing library, ignoring the headers at the
beginning of the file and all HTML tags

3. Split the text into tokens (You can use some library for regular expression matching. To
learn about regular expressions go to this link http://www.rexegg.com/regexquickstart.html)

4. Convert all tokens to lowercase 

6. Apply stemming to the document using any standard algorithm – Porter, Snowball, and
KStem stemmers are appropriate. You should use a stemming library for this step.

7. The tokenizer will write two files:

docids.txt – A file mapping a document's filename (without path) to a unique
integer, its DOCID. Each line should be formatted with a DOCID and filename
separated by a tab, as follows:
1234\t32435

termids.txt – A file mapping a token found during tokenization to a unique
integer, its TERMID. Each line should be formatted with a TERMID and token
separated by a tab, as follows:
567\tapple

## Step 2: Inverted Index ##

The file term_index.txt – An inverted index containing the file position for each occurrence of
each term in the collection. Each line should contain the complete inverted list for a
single term. Each line should contain a list of DOCID,POSITION values. Each line of
this file should contain a TERMID followed by a space-separated list of properties as
follows:

347 1542 567 432,43 456,33 456,41
1. 347: TERMID
2. 1542: Total number of occurrences of the term in the entire corpus
4. 567: Total number of documents in which the term appears
5. 432: Document Id in which term appears
6. 43: Position of term in document 432
 
In order to support more efficient compression, delta encoding is applied to the inverted list.

