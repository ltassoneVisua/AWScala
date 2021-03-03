package awscala.ses

import awscala._
import com.amazonaws.services.simpleemail.model.{ Body, Content, Destination, Message, MessageRejectedException, SendEmailRequest }
import com.amazonaws.services.{ simpleemail => aws }

import scala.collection.JavaConverters._
import java.util
import scala.util.{ Failure, Success, Try }

object SES {
  def apply(credentials: Credentials)(implicit region: Region): SES = new SESClient(BasicCredentialsProvider(credentials.getAWSAccessKeyId, credentials.getAWSSecretKey)).at(region)
  def apply(credentialsProvider: CredentialsProvider = CredentialsLoader.load())(implicit region: Region = Region.default()): SES = new SESClient(credentialsProvider).at(region)
  def apply(accessKeyId: String, secretAccessKey: String)(implicit region: Region): SES = apply(BasicCredentialsProvider(accessKeyId, secretAccessKey)).at(region)

  def at(region: Region): SES = apply()(region)
}

trait SES extends aws.AmazonSimpleEmailService {
  def at(region: Region): SES = {
    this.setRegion(region)
    this
  }

  def sendEmail(toAddresses: List[String], subject: String, body: String, sender: String, ccAddresses: List[String] = List(), bccAddresses: List[String] = List()): Either[String, SESSendEmailResult] = {
    val content = new Content
    content.setData(body)
    content.setCharset("UTF-8")
    val bodyObj = new Body(content)
    val subjectObj = new Content(subject)
    val messageObj = new Message(subjectObj, bodyObj)
    val toAddressesObj = new util.ArrayList[String](toAddresses.asJava)
    val destinationObj = new Destination(toAddressesObj)
    if (ccAddresses.nonEmpty) {
      val ccAddressesObj = new util.ArrayList[String](ccAddresses.asJava)
      destinationObj.setCcAddresses(ccAddressesObj)
    }
    if (bccAddresses.nonEmpty) {
      val bccAddressesObj = new util.ArrayList[String](bccAddresses.asJava)
      destinationObj.setBccAddresses(bccAddressesObj)
    }
    val sendEmailRequest = new SendEmailRequest(sender, destinationObj, messageObj)
    Try(sendEmail(sendEmailRequest)) match {
      case Success(value) => Right(SESSendEmailResult(value))
      case Failure(exception: MessageRejectedException) => Left(exception.getErrorMessage)
      case Failure(exception) => throw exception
    }
  }
}

class SESClient(credentialsProvider: CredentialsProvider = CredentialsLoader.load())
  extends aws.AmazonSimpleEmailServiceClient(credentialsProvider)
  with SES