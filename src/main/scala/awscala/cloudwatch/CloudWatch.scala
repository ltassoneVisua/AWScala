package awscala.cloudwatch

import awscala._

import scala.collection.JavaConverters._
import com.amazonaws.services.cloudwatch.model.{ Dimension, GetMetricStatisticsRequest, GetMetricStatisticsResult }
import com.amazonaws.services.{ cloudwatch => aws }
import org.joda.time.DateTime

object CloudWatch {
  def apply(credentials: Credentials)(implicit region: Region): CloudWatch = new CloudWatchClient(BasicCredentialsProvider(credentials.getAWSAccessKeyId, credentials.getAWSSecretKey)).at(region)
  def apply(credentialsProvider: CredentialsProvider = CredentialsLoader.load())(implicit region: Region = Region.default()): CloudWatch = new CloudWatchClient(credentialsProvider).at(region)
  def apply(accessKeyId: String, secretAccessKey: String)(implicit region: Region): CloudWatch = apply(BasicCredentialsProvider(accessKeyId, secretAccessKey)).at(region)

  def at(region: Region): CloudWatch = apply()(region)
}

trait CloudWatch extends aws.AmazonCloudWatch {
  def at(region: Region): CloudWatch = {
    this.setRegion(region)
    this
  }

  def getMetricStats(namespace: String, metricName: String, startTime: DateTime, endTime: DateTime, periodInSeconds: Int, statistics: String, dimensions: List[(String, String)]): CloudWatchMetricStatisticResponse = {
    val metricStatisticsRequest = new GetMetricStatisticsRequest
    metricStatisticsRequest.setNamespace(namespace)
    metricStatisticsRequest.setMetricName(metricName)
    metricStatisticsRequest.setStartTime(startTime.toDate)
    metricStatisticsRequest.setEndTime(endTime.toDate)
    metricStatisticsRequest.setPeriod(periodInSeconds)
    metricStatisticsRequest.setStatistics(List(statistics).asJava)
    val dimensionObjs = dimensions.map(d => {
      val dim = new Dimension
      dim.setName(d._1)
      dim.setValue(d._2)
      dim
    })
    metricStatisticsRequest.setDimensions(dimensionObjs.asJava)

    CloudWatchMetricStatisticResponse(getMetricStatistics(metricStatisticsRequest))
  }
}

class CloudWatchClient(credentialsProvider: CredentialsProvider = CredentialsLoader.load())
  extends aws.AmazonCloudWatchClient(credentialsProvider)
  with CloudWatch