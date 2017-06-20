import java.nio.charset.StandardCharsets
import scala.collection._

import com.sun.xml.internal.bind.api.impl.NameConverter.Standard
import org.apache.spark.{SparkConf, SparkContext, rdd}
import scala.collection.JavaConversions._
import scala.io.Source
/**
  * Created by shalin on 6/16/2017.
  */
object SparkTutorial2 {

  def main(args: Array[String]): Unit = {
    val configurationSpark=new SparkConf()
      .setMaster("local[*]")
      .setAppName("Tutorial2-Spark")

    val processedData = "E:\\Knowledge Discovery Management\\Tutorials\\ScalaTutorial1\\output\\processedData.txt";
    val sContext=new SparkContext(configurationSpark)

    val fileData=ReadWriteOperation.ReadFile("E:\\Knowledge Discovery Management\\Datasets\\WikiRef_dataset\\WikiRef_dataset\\WikiRef220\\barack.obama.1.txt",StandardCharsets.UTF_8)

   val nlpOper=new NLPOperations()
    val nlpData=new NLPData()
     val nlpDataList=List[NLPData]()

   val nlpObj= nlpOper.GetNLPObject()
   val annotatedDatadata=nlpOper.AnnotateData(fileData,nlpObj)
   val annotatedSentaces= nlpOper.GetSentencesFromAnnotatedData(annotatedDatadata)

    //Group by
   val lammaWords=sContext.parallelize(Array(nlpOper.GetLemmas(annotatedSentaces)))
   val reducedLemmaWord=lammaWords.groupBy(word=>word(0)).collect().mkString(",")

    val tokens=sContext.parallelize((nlpOper.GetToken(annotatedSentaces))).collect()
    val lemma=sContext.parallelize((nlpOper.GetLemmas(annotatedSentaces))).collect()
    val POSList=sContext.parallelize((nlpOper.GetPOS(annotatedSentaces))).collect()
    val nerList=sContext.parallelize((nlpOper.GetNER(annotatedSentaces))).collect().toList

    ReadWriteOperation.WriteToFile(nerList,processedData)

    //Ask question
    println("Enter your question")
    val question=scala.io.StdIn.readLine()

    //Get question type ex. Who, What, Where
    val questionType=question.split(" ")(0)

    //split and lemmatize the question
    val lemmatisedQuestion=QuestionAns.LemmatiseTheQuestion(question)

    //Formate the question
    val formettedQuestion=QuestionAns.FormatQuestion(question)

    //Get answer depending on the question
    val answer=QuestionAns.FindOutAnswer(lemmatisedQuestion,questionType)

    //Templated Answer
    val finalAnswer=QuestionAns.GetFinalAns(questionType,answer,formettedQuestion)

    //Print Answer
    println("-"*40)
    println("Answer : " + finalAnswer)
  }
}
