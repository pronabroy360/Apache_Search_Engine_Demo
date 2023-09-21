# Apache_Search_Engine_Demo
An localhost Apache Search Engine that includes Apache Tomcat 6.0.53 and Apache Nutch 0.9. It may do the necessary crawls for certain specific web urls and display the results by keywords.

The following steps are needed to implement the Search Engine:
1. First install the Cygwin(Cygwin is a Microsoft Windows OS command-line interface that enables developers to compile and run Linux-like applications) terminal.
2. Then after going to C:\cygwin64\home\Pronab's PC, import the two folders of the repositories(apache-tomcat-6.0.53 & nutch-0.9).
3. Windows search bar>Edit the system environment Variables>Environment variables. On that Environment Variables dialog box add some user variables for your system using the "New.." button:
   Variable            Value
   CLASSPATH           C:\cygwin64\home\Pronab's PC\nutch-0.9\lib\lucene-core-2.1.0.jar
   JAVA_HOME           C:\PROGRA~1\Java\jdk1.6.0_45
  After adding these variables click "Ok" to make sure these to be added on your system.
4. Go to the path: "C:\cygwin64\home\Lenovo\apache-tomcat-6.0.53\webapps\nutch-0.9\WEB-INF\classes" and on that path you will find a file named "nutch-file.xml"
5. Open that file in VS Code/any editor and at line 10 in <value> tag:
    <value>C:\cygwin64\home\Pronab's PC\nutch-0.9\crawl\</value>
   change the Pronab's PC after home\ and put your system's name instead.
6. Go to the path: "C:\cygwin64\home\Pronab's PC\nutch-0.9\urls" and add urls on the nits.txt file as new line.
7. If you have done all the steps from the beginning then your Search engine is ready now. All you need to do right now is put "http://localhost:8081/nutch-0.9" in your browser and put Enter.
8. Now search any of the topic that is related to the web-site urls that you've added now(I have added the website nits.ac.in). 
