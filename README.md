# CNNScrape

Dependencies:
jsoup version: 1.9.2

Alternativaly, you could begin a gradle project in your favorite IDE, clone this repository, 
and in your build.gradle file under dependencies place the following line of code: 
`compile 'org.jsoup:jsoup:1.9.2'`

After adding the dependencies, the program's main is located in the Test.java file. If you run that, 
(and assuming CNN hasn't changed the format of the html on the website), the program will print 
the link, title, images, and body text of all articles on the CNN main website :)

You can leverage these classes to your own use by setting the correct tags/css selectors (this will require research 
on a site's html layout) in the NewsWebsite class as the instance 
variables (described in the source), you can scrape the
articles of any news website for links to major images related to the article, the article title, 
the article's actual body text, and the link to the actual article on the site. 

This information could be used for data mining purposes or displayed in a news application (or some other, 
more creative application). With a little machine learning and/or string parsing/searching, you could make a program 
that displays the top ten articles you would most like to read for the day. I will probably use it for this purpose as 
I don't really like spending 30 minutes a day browsing for and reading news articles that I find insightful 
or interesting :) Hope it's useful to you. 

Finally and not unsurprisingly, not all companies are okay with you scraping their information from their websites.
As such I am NOT responsible for any copyright infringement incurred in using of these programs. I leave it soley to 
the user of this program do decide if scraping a given site is a violation of copyright. 

