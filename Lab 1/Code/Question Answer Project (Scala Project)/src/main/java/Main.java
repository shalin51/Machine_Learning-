/**
 * Created by shalin on 6/10/2017.
 */

import edu.stanford.nlp.dcoref.CorefChain;
import edu.stanford.nlp.dcoref.CorefCoreAnnotations;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.StringUtils;
import org.jetbrains.annotations.NotNull;
import scala.util.control.Exception;
import sun.plugin2.gluegen.runtime.StructAccessor;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {

        try {

            String inputFileName = "E:\\Knowledge Discovery Management\\Datasets\\WikiRef_dataset\\WikiRef_dataset\\WikiRef220\\barack.obama.1.txt";
            String outputFileName = "E:\\Knowledge Discovery Management\\Tutorials\\ScalaTutorial1\\output\\output.txt";
            String processedData = "E:\\Knowledge Discovery Management\\Tutorials\\ScalaTutorial1\\output\\processedData.txt";
//
//            // Print write object to write into file.
//            PrintWriter outWrite= new PrintWriter(outputFileName);
//
//            // Used to create a StanfordCoreNLP object with defined properties
//            Properties props = new Properties();
//            props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
//            StanfordCoreNLP nlpPipe = new StanfordCoreNLP(props);
//
//            // reading input text in the inputText variable
            String inputText = ReadFile(inputFileName, StandardCharsets.UTF_8);
//
//            // create an empty Annotation just with the given text
//            Annotation document = new Annotation(inputText);
//
//
//            nlpPipe.annotate(document); //

            //Used to get all annotation of document and write a file used to manipulate data
            // nlpPipe.prettyPrint(document,outWrite);

            String oneLine="";
            List<String> datasetText= new ArrayList<>();
            FileReader fileReader =
                    new FileReader(outputFileName);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader =
                    new BufferedReader(fileReader);

            while((oneLine = bufferedReader.readLine()) != null) {
               datasetText.add(oneLine);
            }


            Set<String> quatedText=new HashSet<>();
            for(String line:datasetText) {
                Pattern p = Pattern.compile("\"([^\"]*)\"");
                Matcher m = p.matcher(line);
                while (m.find()) {
                    {
                        if(line.contains("->")) {
                            String[] imoortantWords=line.split(":")[1].split("->");
                            for(String w:imoortantWords) {
                                quatedText.add(w);
                            }
                        }
                    }
                }
            }

            WriteToFile(quatedText,processedData);

            System.out.print("Enter your Question: \n");
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String question = br.readLine();
            String Ans=ReadProcessedData(processedData,question);
            System.out.print(Ans);


        } catch (IOException io) {
            System.out.print(io.toString());
        }
    }

    @NotNull
    static String ReadFile(String path, Charset encoding) throws IOException
    {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

    static String ReadProcessedData(String outputFileName,String question){
        String ans="Not able to find answer!!!!";
        String[] splitedQuestion = question.split("\\s+");
        String line="";
        String que="who is President";
        boolean hasWord=false;
        try {
            FileReader fileReader =
                    new FileReader(outputFileName);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader =
                    new BufferedReader(fileReader);
            List<String> ignoreList = GetIgnoreList();
            String[] questionWords = question.split(" ");

            while ((line = bufferedReader.readLine()) != null) {
               String[] anws= line.split("\"");
                for(String queWord:questionWords)
                {
                    for(String anw:anws)
                    {
                        if(anw.contains(queWord))
                        {
                            ans=anw;
                            break;
                        }
                    }
                }


            }
        }
        catch (IOException io) {
        }
        finally {
            return ans;
        }
    }

    static void WriteToFile(Set<String> lst, String path)
    {
        try{
            FileWriter writer= new FileWriter(path);
           for(String line:lst){
               writer.write(line);
           }
            writer.close();
        } catch (IOException e) {
            // do something
        }
    }

    static List<String> GetIgnoreList(){
        List<String> ignoreList= new ArrayList<>();
        ignoreList.add("who");
        ignoreList.add("what");
        ignoreList.add("how");
        ignoreList.add("is");
        ignoreList.add("that");
        ignoreList.add("are");
        ignoreList.add("why");
        ignoreList.add("the");
        ignoreList.add("he");
        ignoreList.add("of");
        ignoreList.add("the");
        ignoreList.add("in");
        ignoreList.add("from");
        ignoreList.add("his");
        ignoreList.add("her");
        ignoreList.add("a");
        return ignoreList;
    }
}
